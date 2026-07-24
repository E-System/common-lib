package com.eslibs.common.model;

import com.eslibs.common.collection.Items;
import com.eslibs.common.number.Numbers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public record FiscalQr(
    String fn,
    String fp,
    String doc,
    int sum,
    LocalDateTime date,
    Type type
) {

    private static final String TIME = "t";
    private static final String SUM = "s";
    private static final String FN = "fn";
    private static final String DOC = "i";
    private static final String FP = "fp";
    private static final String TYPE = "n";
    private static final Collection<String> AllToParse = Arrays.asList(TIME, SUM, FN, DOC, FP, TYPE);
    private static final String DATE_FORMAT = "yyyyMMdd'T'HHmm";

    public String asString() {
        return asString(null);
    }

    public String asString(String url) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put(TIME, date.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        params.put(SUM, Numbers.formatter(".").money(sum));
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
        if (Items.isEmpty(params)) {
            return null;
        }
        try {
            return new FiscalQr(
                params.get(FN),
                params.get(FP),
                params.get(DOC),
                (int) Math.round(Double.parseDouble(params.get(SUM)) * 100.0),
                parse(params.get(TIME)),
                Type.of(Integer.parseInt(params.get(TYPE)))
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
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

    private static LocalDateTime parse(String value) {
        try {
            return DateTimeFormatter.ofPattern(DATE_FORMAT).parse(value, LocalDateTime::from);
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
