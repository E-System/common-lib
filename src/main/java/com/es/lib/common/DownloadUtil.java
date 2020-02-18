package com.es.lib.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DownloadUtil {

    /**
     * Download file
     *
     * @param srcUrl   Url to file
     * @param target   Path to save file
     * @param showInfo Flag to show info
     */
    public static void downloadFile(String srcUrl, Path target, boolean showInfo) throws Exception {
        URL url = new URL(srcUrl);
        long len = getFileSize(url);
        try (InputStream is = url.openStream(); OutputStream output = Files.newOutputStream(target, StandardOpenOption.CREATE)) {
            long count = 0;
            int n;
            byte[] buffer = new byte[1024 * 1024];
            System.out.println("Start downloading " + srcUrl);
            while (-1 != (n = is.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
                if (showInfo && len > 0) {
                    System.out.println(String.format("\rDownloaded: %.2f%%       ", ((double) count / len * 100)));
                }
            }
            System.out.println("\rDownloaded: 100%          ");
        }
    }

    /**
     * Read file size
     *
     * @param url Url to file
     * @return File size
     */
    private static long getFileSize(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            return Long.parseLong(conn.getHeaderField("Content-Length"));
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception ignore) { }
        }
    }

}