package com.es.lib.common.model;

import com.es.lib.common.DateUtil;
import com.es.lib.common.NumberFormatUtil;
import com.es.lib.common.collection.CollectionUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class FiscalQr {

    private static final String TIME = "t";
    private static final String SUM = "s";
    private static final String FN = "fn";
    private static final String DOC = "i";
    private static final String FP = "fp";
    private static final String TYPE = "n";
    private static final Collection<String> AllToParse = Arrays.asList(TIME, SUM, FN, DOC, FP, TYPE);
    private static final String DATE_FORMAT = "yyyyMMdd'T'HHmm";

    private final String fn;
    private final String fp;
    private final String doc;
    private final int sum;
    private final Date date;
    private final Type type;

    public String asString() {
        return asString(null);
    }

    public String asString(String url) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put(TIME, DateUtil.format(date, DATE_FORMAT));
        params.put(SUM, NumberFormatUtil.f22(sum, '.'));
        params.put(FN, fn);
        params.put(DOC, doc);
        params.put(FP, fp);
        params.put(TYPE, String.valueOf(type.value));
        return (StringUtils.isNotBlank(url) ? (url + "?") : "")
               + params.entrySet().stream().map(v -> v.getKey() + "=" + v.getValue()).collect(Collectors.joining("&"));
    }

    public static FiscalQr of(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (value.contains("?")) {
            value = value.substring(value.indexOf("?") + 1);
        }
        Map<String, String> params = extract(value);
        if (CollectionUtil.isEmpty(params)) {
            return null;
        }
        return new FiscalQr(
            params.get(FN),
            params.get(FP),
            params.get(DOC),
            (int) Math.round(Double.parseDouble(params.get(SUM)) * 100.0),
            parse(params.get(TIME)),
            Type.of(Integer.parseInt(params.get(TYPE)))
        );
    }

    private static Map<String, String> extract(String value) {
        return AllToParse.stream().map(v -> Pair.of(v, find(v, value)))
            .filter(v -> v.getValue() != null)
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private static String find(String name, String value) {
        Matcher matcher = Pattern.compile("&" + name + "=([\\d.T]*)").matcher("&" + value);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static Date parse(String value) {
        try {
            return DateUtil.parse(value, DATE_FORMAT);
        } catch (Exception e) {
            log.error("ERROR PARSE QR DATE [{}]:{}", value, e.getMessage(), e);
        }
        return null;
    }

    @RequiredArgsConstructor
    public enum Type {
        //<option value="1">Приход</option>
        SELL(1),
        //<option value="2">Возврат прихода</option>
        SELL_RETURN(2),
        //<option value="3">Расход</option>
        BUY(3),
        //<option value="4">Возврат расхода</option>
        BUY_RETURN(4);

        private final int value;

        public static Type of(int value) {
            for (Type type : Type.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }

    }
}
