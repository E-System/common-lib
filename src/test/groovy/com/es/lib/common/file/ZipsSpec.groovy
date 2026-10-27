package com.es.lib.common.file

import com.es.lib.common.file.output.OutputData
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipsSpec extends Specification {

    def "Compress OutputData items"() {
        given:
        Path tempDir = Files.createTempDirectory("zips_spec_test")
        Path tempFile = tempDir.resolve("temp.txt")
        Files.write(tempFile, "FileContent".bytes)

        def items = [
                OutputData.create("test1.txt", 'text/plain', 'Hello1'.bytes),
                OutputData.create("test2.txt", tempDir.toString(), tempFile),
                OutputData.create("test3.txt", 'text/plain', new ByteArrayInputStream('Hello3'.bytes))
        ]

        when:
        byte[] zipBytes = Zips.compress(items)

        then:
        zipBytes != null
        zipBytes.length > 0

        when:
        Path outDir = tempDir.resolve("extracted")
        Files.createDirectories(outDir)
        Zips.decompress(new ByteArrayInputStream(zipBytes), outDir.toFile())

        then:
        new String(Files.readAllBytes(outDir.resolve("test1.txt"))) == "Hello1"
        new String(Files.readAllBytes(outDir.resolve("test2.txt"))) == "FileContent"
        new String(Files.readAllBytes(outDir.resolve("test3.txt"))) == "Hello3"

        cleanup:
        tempDir.toFile().deleteDir()
    }

    def "Compress single file and directory to OutputStream and Decompress"() {
        given:
        Path tempDir = Files.createTempDirectory("zips_dir_test")
        Path subDir = tempDir.resolve("sub")
        Files.createDirectories(subDir)
        Path file1 = tempDir.resolve("f1.txt")
        Path file2 = subDir.resolve("f2.txt")
        Path hiddenFile = tempDir.resolve(".hidden.txt")
        Files.write(file1, "Content1".bytes)
        Files.write(file2, "Content2".bytes)
        Files.write(hiddenFile, "HiddenContent".bytes)

        Path zipFile = Files.createTempFile("archive", ".zip")

        when:
        zipFile.toFile().withOutputStream { os ->
            Zips.compress(os, tempDir.toFile())
        }

        then:
        Files.exists(zipFile)
        Files.size(zipFile) > 0

        when:
        Path extractDir = Files.createTempDirectory("zips_extract_test")
        zipFile.toFile().withInputStream { is ->
            Zips.decompress(is, extractDir.toFile())
        }

        then:
        Path rootInExtract = extractDir.resolve(tempDir.toFile().name)
        Files.exists(rootInExtract.resolve("f1.txt"))
        new String(Files.readAllBytes(rootInExtract.resolve("f1.txt"))) == "Content1"
        Files.exists(rootInExtract.resolve("sub/f2.txt"))
        new String(Files.readAllBytes(rootInExtract.resolve("sub/f2.txt"))) == "Content2"
        !Files.exists(rootInExtract.resolve(".hidden.txt"))

        cleanup:
        tempDir.toFile().deleteDir()
        extractDir.toFile().deleteDir()
        Files.deleteIfExists(zipFile)
    }

    def "Compress single file to OutputStream"() {
        given:
        Path tempFile = Files.createTempFile("single", ".txt")
        Files.write(tempFile, "SingleFileContent".bytes)
        Path zipFile = Files.createTempFile("single_archive", ".zip")

        when:
        zipFile.toFile().withOutputStream { os ->
            Zips.compress(os, tempFile.toFile())
        }

        then:
        Files.size(zipFile) > 0

        when:
        Path extractDir = Files.createTempDirectory("single_extract")
        zipFile.toFile().withInputStream { is ->
            Zips.decompress(is, extractDir.toFile())
        }

        then:
        Files.exists(extractDir.resolve(tempFile.toFile().name))
        new String(Files.readAllBytes(extractDir.resolve(tempFile.toFile().name))) == "SingleFileContent"

        cleanup:
        Files.deleteIfExists(tempFile)
        Files.deleteIfExists(zipFile)
        extractDir.toFile().deleteDir()
    }

    def "Decompress ZipSlip protection throws IOException"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ZipOutputStream zos = new ZipOutputStream(baos)
        zos.putNextEntry(new ZipEntry("../evil.txt"))
        zos.write("evil".bytes)
        zos.closeEntry()
        zos.close()

        Path extractDir = Files.createTempDirectory("zipslip_test")

        when:
        Zips.decompress(new ByteArrayInputStream(baos.toByteArray()), extractDir.toFile())

        then:
        thrown(IOException)

        cleanup:
        extractDir.toFile().deleteDir()
    }
}
