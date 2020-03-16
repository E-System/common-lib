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

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Утилитарные методы по работе с сетью (ip адреса, ....)
 *
 * @author uchonyy
 */
public class NetUtil {

    private static final Collection<Pair<Long, Long>> LOCAL_SUBNET = Arrays.asList(
        Pair.of(ip4ToLong("10.0.0.0"), ip4ToLong("10.255.255.255")),
        Pair.of(ip4ToLong("172.16.0.0"), ip4ToLong("172.31.255.255")),
        Pair.of(ip4ToLong("192.168.0.0"), ip4ToLong("192.168.255.255"))
    );


    private NetUtil() { }

    /**
     * Конвертировать строковый ip в числовой
     *
     * @param ip строковое представление
     * @return числовое представление
     */
    public static long ip4ToLong(String ip) {
        long result = 0;
        String[] octs = ip.split("\\.", 4);
        result += Long.parseLong(octs[0]) * (256 << 16);
        result += Long.parseLong(octs[1]) * (256 << 8);
        result += Long.parseLong(octs[2]) * 256;
        result += Long.parseLong(octs[3]);
        return result;
    }

    /**
     * Конвертировать строковый ip в числовой
     *
     * @param ip строковое представление
     * @return числовое представление
     */
    public static String longToIp4(long ip) {
        StringJoiner join = new StringJoiner(".");
        join.add(String.valueOf((ip >> 24) & 0xFF));
        join.add(String.valueOf((ip >> 16) & 0xFF));
        join.add(String.valueOf((ip >> 8) & 0xFF));
        join.add(String.valueOf(ip & 0xFF));
        return join.toString();
    }

    /**
     * Получить ip адрес из адреса подсети
     *
     * @param subnet адрес подсети типа 255.255.255.255/24
     * @return числовое представление адреса подсети
     */
    public static long getNetwork(String subnet) {
        Objects.requireNonNull(subnet, "Parameter is null");
        if (!subnet.contains("/")) {
            throw new IllegalArgumentException("Parameter not contains '/'");
        }
        return ip4ToLong(subnet.substring(0, subnet.indexOf("/")));
    }

    /**
     * Получить широковещательный адрес для подсети
     *
     * @param subnet адрес подсети типа 255.255.255.255/24
     * @return числовое представление широковещательного адреса для подсети
     */
    public static long getBroadcast(String subnet) {
        Objects.requireNonNull(subnet, "Parameter is null");
        if (!subnet.contains("/")) {
            throw new IllegalArgumentException("Parameter not contains '/'");
        }
        long ip = getNetwork(subnet);
        int mask = Integer.parseInt(subnet.substring(subnet.indexOf("/") + 1));
        return getBroadcast(ip, mask);
    }

    /**
     * Получить широковещательный адрес для подсети
     *
     * @param subnet адрес подсети
     * @param mask   маска
     * @return числовое представление широковещательного адреса для подсети
     */
    public static long getBroadcast(long subnet, int mask) {
        return subnet + (2 << 31 - mask) - 1;
    }

    /**
     * Проверить ip адрес на принадлежность локальной сети
     *
     * @param ip ip адрес для проверки
     * @return true - если ip адрес попадает в 10.0.0.0 — 10.255.255.255, 172.16.0.0 — 172.31.255.255, 192.168.0.0 — 192.168.255.255
     */
    public static boolean isLocalNetwork(String ip) {
        long ipNumber = NetUtil.ip4ToLong(ip);
        return LOCAL_SUBNET.stream().anyMatch(v -> inRange(ipNumber, v.getLeft(), v.getRight()));
    }

    /**
     * Проверить на вхождение ip адреса в интервал
     *
     * @param ip    ip адрес для проверки
     * @param begin начало интервала
     * @param end   конец интервала
     * @return true - если ip адрес находится в интервале
     */
    public static boolean inRange(String ip, String begin, String end) {
        return inRange(ip4ToLong(ip), begin, end);
    }

    /**
     * Проверить на вхождение ip адреса в интервал
     *
     * @param ip    числовое представление ip адреса
     * @param begin начало интервала
     * @param end   конец интервала
     * @return true - если ip адрес находится в интервале
     */
    public static boolean inRange(long ip, String begin, String end) {
        long beginNumber = NetUtil.ip4ToLong(begin);
        if (ip < beginNumber) {
            return false;
        }
        long endNumber = NetUtil.ip4ToLong(end);
        return ip <= endNumber;
    }

    /**
     * Проверить на вхождение ip адреса в интервал
     *
     * @param ip    числовое представление ip адреса
     * @param begin числовое представление начала интервала
     * @param end   числовое представление конца интервала
     * @return true - если ip адрес находится в интервале
     */
    public static boolean inRange(long ip, long begin, long end) {
        return ip >= begin && ip <= end;
    }
}
