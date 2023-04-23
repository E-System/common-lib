package com.eslibs.common.text;

class StringRowSplitter {

    private StringRowSplitter() {}

    /**
     * Разбивает строку на 2 с условием длины первой строки
     *
     * @param src         исходная строка
     * @param spaceSymbol символ пустой строки
     * @param firstLength длина первой строки
     * @return массив из двух строк
     */
    static String[] split(String src, String spaceSymbol, int firstLength) {
        if (src == null) {
            return new String[]{spaceSymbol, spaceSymbol};
        }
        src = src.trim();
        if (src.length() <= firstLength) {
            return new String[]{src, spaceSymbol};
        }
        int strLen = findSplitPosition(src, firstLength);
        if (strLen < 0) {
            return new String[]{spaceSymbol, src};
        }
        return new String[]{src.substring(0, strLen), src.substring(strLen + 1)};
    }

    /**
     * Разбивает строку на 3 с условием длины первой и второй строки
     *
     * @param src          исходная строка
     * @param spaceSymbol  символ пустой строки
     * @param firstLength  длина первой строки
     * @param secondLength длина второй строки
     * @return массив из трех строк
     */
    static String[] split(String src, String spaceSymbol, int firstLength, int secondLength) {
        String[] res = split(src, spaceSymbol, firstLength);
        String next = res[1];
        if (next.replace(spaceSymbol, "").isEmpty()) {
            return new String[]{res[0], res[1], spaceSymbol};
        }
        String[] res2 = split(next, spaceSymbol, secondLength);
        return new String[]{res[0], res2[0], res2[1]};
    }

    private static int findSplitPosition(String src, int firstLength) {
        int result = -1;
        for (int i = 0; i < src.length() - 1; ++i) {
            if (i > firstLength) {
                break;
            }
            if (i < firstLength) {
                if (src.charAt(i) == ' ') {
                    result = i;
                } else if (src.charAt(i + 1) == ' ') {
                    result = i + 1;
                }
            }
        }
        return result;
    }
}