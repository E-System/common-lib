package com.es.lib.common;

import java.io.*;

public class SerializationUtil {

    /**
     * Deserialize object
     *
     * @param arr - исходный массив байт
     * @return десериализованный объект
     */
    public static <T> T toObject(byte[] arr, Class<T> clz) {
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
    public static byte[] toArray(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception ex) {
            return null;
        }
    }

    public static String removeDelimiters(String value) {
        return replaceDelimiters(value, "");
    }

    public static String replaceDelimiters(String value, String target) {
        if (value != null) {
            value = value.replaceAll(",|\\.", target);
        }
        return value;
    }
}
