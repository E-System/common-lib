package com.eslibs.common.text;

final class Plural {

    static String convert(long value, String str1, String str2, String str3) {
        if ((value % 10 == 1) && (value % 100 != 11)) {
            return str1;
        } else if ((value % 10 >= 2) && (value % 10 <= 4) && (value % 100 < 10 || value % 100 >= 20)) {
            return str2;
        }
        return str3;
    }
}
