package com.es.lib.common.model;

import com.es.lib.common.DateUtil;
import com.es.lib.common.text.TextUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;
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

    private final String fn;
    private final String fp;
    private final String doc;
    private final int sum;
    private final Date date;
    private final Type type;

    public static FiscalQr of(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (value.contains("?")) {
            value = value.substring(value.indexOf("?") + 1);
        }
        Map<String, String> params = TextUtil.splitBy("&", true).splitBy("=", true).toPairs(value).stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue
        ));
        return new FiscalQr(
            params.get(FN),
            params.get(FP),
            params.get(DOC),
            (int) Math.round(Double.parseDouble(params.get(SUM)) * 100.0),
            parse(params.get(TIME)),
            Type.of(Integer.parseInt(params.get(TYPE)))
        );
    }

    private static Date parse(String value) {
        try {
            return DateUtil.parse(value, "yyyyMMdd'T'HHmm");
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
