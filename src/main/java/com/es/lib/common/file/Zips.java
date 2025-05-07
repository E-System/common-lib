package com.es.lib.common.file;

import com.es.lib.common.file.output.ByteData;
import com.es.lib.common.file.output.FileData;
import com.es.lib.common.file.output.OutputData;
import com.es.lib.common.file.output.StreamData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Zips {

    public static byte[] compress(Collection<OutputData> items) throws IOException {
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream(); ZipOutputStream zipOut = new ZipOutputStream(result)) {
            for (OutputData item : items) {
                ZipEntry zipEntry = new ZipEntry(item.getFileName());
                zipOut.putNextEntry(zipEntry);
                if (item.isFile()) {
                    FileData fileData = (FileData) item;
                    FileUtils.copyFile(fileData.getContent().toFile(), zipOut);
                } else if (item.isBytes()) {
                    ByteData byteData = (ByteData) item;
                    zipOut.write(byteData.getContent());
                } else if (item.isStream()) {
                    StreamData streamData = (StreamData) item;
                    IOUtils.copy(streamData.getContent(), zipOut);
                }
                zipOut.closeEntry();
            }
            return result.toByteArray();
        }
    }

    public static void compress(OutputStream outputStream, File file) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
            zipFile(file, file.getName(), zipOut);
        }
    }

    public static void decompress(InputStream inputStream, File destDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }


    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
