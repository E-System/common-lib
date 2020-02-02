/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class FileUtil {

    /**
     * Размер буфера для операций чтения
     */
    public static final int BUFFER_SIZE = 1024;

    private FileUtil() { }

    public static long crc32(String string) throws UnsupportedEncodingException {
        return crc32(string.getBytes(Constant.DEFAULT_ENCODING));
    }

    public static long crc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }


    public static Map.Entry<String, Long> readCrc32(String fileName) throws IOException {
        return readCrc32(new FileInputStream(new File(fileName)));
    }

    public static Map.Entry<String, Long> readCrc32(File file) throws IOException {
        return readCrc32(new FileInputStream(file));
    }

    public static Map.Entry<String, Long> readCrc32(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (CheckedInputStream cis = new CheckedInputStream(inputStream, new CRC32())) {
            byte[] buf = new byte[BUFFER_SIZE];
            while (cis.read(buf) >= 0) {
                sb.append(Arrays.toString(buf));
            }
            return Pair.of(sb.toString(), cis.getChecksum().getValue());
        }
    }

    private static long copyWithCrc32(InputStream from, File to) throws IOException {
        CheckedInputStream checkedInputStream = new CheckedInputStream(from, new CRC32());
        FileUtils.copyInputStreamToFile(
            checkedInputStream,
            to
        );
        return checkedInputStream.getChecksum().getValue();
    }


    /**
     * Прочитать поток в строку
     *
     * @param is       поток для чтения
     * @param encoding кодировка
     * @return строка с данными из потока
     * @throws IOException исключение чтения из потока
     */
    public static String read(InputStream is, String encoding) throws IOException {
        byte[] data = new byte[is.available()];
        is.read(data);
        is.close();
        return new String(data, encoding);
    }

    public static String readBuffered(InputStream inputStream) throws IOException {
        return readBuffered(inputStream, Constant.DEFAULT_ENCODING);
    }

    public static String readBuffered(InputStream inputStream, String encoding) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, encoding));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static byte[] readBufferedByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final byte[] bytes = new byte[BUFFER_SIZE];
        int read;
        while ((read = inputStream.read(bytes)) != -1) {
            baos.write(bytes, 0, read);
        }
        baos.close();
        return baos.toByteArray();
    }
}
