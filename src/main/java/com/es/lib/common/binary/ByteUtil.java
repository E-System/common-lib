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

package com.es.lib.common.binary;

import java.io.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 12.06.15
 */
public class ByteUtil {

    /**
     * Deserialize object
     *
     * @param arr - исходный массив байт
     * @return десериализованный объект
     */
    public static <T> T deserialize(byte[] arr, Class<T> clz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        try (ObjectInput in = new ObjectInputStream(bis)) {
            return (T) in.readObject();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Serialize object
     *
     * @param obj Object to serialize
     * @return Byte array with serialized object
     */
    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception ex) {
            return null;
        }
    }

    public static int xor(byte[] data) { return xor(data, 0, 0); }

    public static int xor(byte[] data, int skipIndex, int skipLen) {
        return xor(data, skipIndex, skipLen, data.length);
    }

    public static int xor(byte[] data, int skipIndex, int skipLen, int lastIdx) {
        byte res = 0x00;
        for (int i = 0; i < lastIdx; i++) {
            if (i >= skipIndex && i < skipIndex + skipLen) {
                continue;
            }
            res ^= data[i];
        }
        return res;
    }

    /**
     * Форматирует байт в hex значении
     *
     * @param b - байт - значение
     * @return строку hex значения
     */
    public static String toHex(byte b) {
        return String.format("%02X", b & 0xFF);
    }

    /**
     * Форматирует байты в hex значении объединяя пробелом
     *
     * @param b - мастив байтов
     * @return строку hex значений байтов объединенных пробелом
     */
    public static String toHex(byte... b) {
        Objects.requireNonNull(b);
        return IntStream.range(0, b.length)
                        .mapToObj(i -> String.format("%02X", b[i] & 0xFF))
                        .collect(Collectors.joining(" "));
    }

    /**
     * Преобразует double значение с 2мя знаками после запятой в массив байтов BCD вида
     *
     * @param value - значение
     * @param width - кол-во байтов результата
     * @return массив байтов BCD вида
     * @throws IllegalArgumentException если width меньше 1 или value меньше 0 или если значение не может уместиться в width байт
     */
    public static byte[] doubleToBCD(double value, int width) {
        return doubleToBCD(value, 2, width);
    }

    /**
     * Преобразует double значение с 2мя знаками после запятой в массив байтов BCD вида
     *
     * @param value        - значение
     * @param decimalCount - кол-во знаком после запятой
     * @param width        - кол-во байтов результата
     * @return массив байтов BCD вида
     * @throws IllegalArgumentException если width&lt;=0 или value&lt;0 или если значение не может уместиться в width байт
     */
    public static byte[] doubleToBCD(double value, int decimalCount, int width) {
        return longToBCD((long) (value * Math.pow(10, decimalCount)), width);
    }

    /**
     * Преобразует int значение в массив байтов BCD вида
     *
     * @param value - значение
     * @param width - кол-во байтов результата
     * @return массив байтов BCD вида
     * @throws IllegalArgumentException если width&lt;=0 или value&lt;0 или если значение не может уместиться в width байт
     */
    public static byte[] intToBCD(int value, int width) {
        return longToBCD(value, width);
    }

    /**
     * Преобразует int значение в байт BCD вида
     *
     * @param value - значение
     * @return байт BCD вида
     * @throws IllegalArgumentException если value&lt;0 или value&gt;55
     */
    public static byte intToBCDByte(int value) {
        if (value > 255) {
            throw new IllegalArgumentException("Value must be <= 255");
        }
        return longToBCD(value, 1)[0];
    }

    /**
     * Преобразует long значение в массив байтов BCD вида
     *
     * @param value - значение
     * @param width - кол-во байтов результата
     * @return массив байтов BCD вида
     * @throws IllegalArgumentException если width&lt;=0 или value&lt;0 или если значение не может уместиться в width байт
     */
    public static byte[] longToBCD(long value, int width) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be >= 0");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be > 0");
        }
        String s = String.format("%0" + width * 2 + "d", value);
        if (s.length() > width * 2) {
            throw new IllegalArgumentException("Too big value(" + value + ") for width(" + width + ")");
        }
        byte[] r = new byte[width];
        for (int i = 0; i < width; i++) {
            r[i] = (byte) (Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16) & 0xFF);
        }
        return r;
    }

    /**
     * Read short value from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return short
     */
    public static short getShort(byte[] b, int index) {
        return (short) ((b[index] & 255) << 8 | b[index + 1] & 255);
    }

    /**
     * Read short value in LE format from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return short
     */

    public static short getShortLE(byte[] b, int index) {
        return (short) (b[index] & 255 | (b[index + 1] & 255) << 8);
    }

    /**
     * Read unsigned short (int) value from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return unsigned short (int) value
     */

    public static int getUnsignedShort(byte[] b, int index) {
        return getShort(b, index) & 0xFFFF;
    }


    /**
     * Read short value in LE format from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return unsigned short (int) value
     */
    public static int getUnsignedShortLE(byte[] b, int index) {
        return getShortLE(b, index) & 0xFFFF;
    }


    /**
     * Read 3byte int value from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return 3byte int value
     */
    public static int getMedium(byte[] b, int index) {
        return (getShort(b, index) & 0xFFFF) << 8 | b[index + 2] & 255;
    }

    /**
     * Read 3byte int value in LE format from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return 3byte int value
     */
    public static int getMediumLE(byte[] b, int index) {
        return getShortLE(b, index) & 0xFFFF | (b[index + 2] & 255) << 16;
    }

    /**
     * Read int value from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return int value
     */
    public static int getInt(byte[] b, int index) {
        return (getShort(b, index) & 0xFFFF) << 16 | getShort(b, index + 2) & 0xFFFF;
    }

    /**
     * Read int value in LE format from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return int value
     */
    public static int getIntLE(byte[] b, int index) {
        return getShortLE(b, index) & 0xFFFF | (getShortLE(b, index + 2) & 0xFFFF) << 16;
    }

    /**
     * Read long value from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return long value
     */
    public static long getLong(byte[] b, int index) {
        return ((long) getInt(b, index) & 4294967295L) << 32 | (long) getInt(b, index + 4) & 4294967295L;
    }

    /**
     * Read long value in LE format from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @return long value
     */
    public static long getLongLE(byte[] b, int index) {
        return (long) getIntLE(b, index) & 4294967295L | ((long) getIntLE(b, index + 4) & 4294967295L) << 32;
    }

    /**
     * Read custom len long value from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @param count - bytes count of value
     * @return long value
     */
    public static long getLong(byte[] b, int index, int count) {
        byte[] data = new byte[8];
        System.arraycopy(b, index, data, 8 - count, count);
        return getLong(data, 0);
    }

    /**
     * Read custom len long value in LE format from array
     *
     * @param b     - source byte array
     * @param index - start position index
     * @param count - bytes count of value
     * @return long value
     */
    public static long getLongLE(byte[] b, int index, int count) {
        byte[] data = new byte[8];
        System.arraycopy(b, index, data, 0, count);
        return getLongLE(data, 0);
    }

    /**
     * Get int16 bytes
     *
     * @param value - value to bytes
     * @return byte[]
     */
    public static byte[] getInt16Bytes(int value) {
        return new byte[]{(byte) ((value >>> 8) & 0xFF), (byte) (value & 0xFF)};
    }

    /**
     * Get int16 bytes LE
     *
     * @param value - value to bytes
     * @return byte[] LE
     */
    public static byte[] getInt16BytesLE(int value) {
        return new byte[]{(byte) (value & 0xFF), (byte) ((value >>> 8) & 0xFF)};
    }

    /**
     * Get int32 bytes
     *
     * @param value - value to bytes
     * @return byte[]
     */
    public static byte[] getInt32Bytes(int value) {
        return new byte[]{
            (byte) ((value >>> 24) & 0xFF),
            (byte) ((value >>> 16) & 0xFF),
            (byte) ((value >>> 8) & 0xFF),
            (byte) (value & 0xFF)
        };
    }

    /**
     * Get int32 bytes LE
     *
     * @param value - value
     * @return byte[] LE
     */
    public static byte[] getInt32BytesLE(int value) {
        return new byte[]{
            (byte) (value & 0xFF),
            (byte) ((value >>> 8) & 0xFF),
            (byte) ((value >>> 16) & 0xFF),
            (byte) ((value >>> 24) & 0xFF),
        };
    }

    /**
     * Get long bytes
     *
     * @param value - value to bytes
     * @return byte[]
     */
    public static byte[] getLongBytes(long value) {
        return new byte[]{
            (byte) ((value >>> 56) & 0xFF),
            (byte) ((value >>> 48) & 0xFF),
            (byte) ((value >>> 40) & 0xFF),
            (byte) ((value >>> 32) & 0xFF),
            (byte) ((value >>> 24) & 0xFF),
            (byte) ((value >>> 16) & 0xFF),
            (byte) ((value >>> 8) & 0xFF),
            (byte) (value & 0xFF)
        };
    }

    /**
     * Get long bytes LE
     *
     * @param value - value to bytes
     * @return byte[] LE
     */
    public static byte[] getLongBytesLE(long value) {
        return new byte[]{
            (byte) (value & 0xFF),
            (byte) ((value >>> 8) & 0xFF),
            (byte) ((value >>> 16) & 0xFF),
            (byte) ((value >>> 24) & 0xFF),
            (byte) ((value >>> 32) & 0xFF),
            (byte) ((value >>> 40) & 0xFF),
            (byte) ((value >>> 48) & 0xFF),
            (byte) ((value >>> 56) & 0xFF),
        };
    }

}
