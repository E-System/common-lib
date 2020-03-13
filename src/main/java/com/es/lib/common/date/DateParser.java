package com.es.lib.common.date;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class DateParser extends DateProcessor {

    public DateParser(ZoneId zoneId, Locale locale) {
        super(zoneId, locale);
    }

    public Date parse(String date, String format) throws ParseException {
        if (date == null) {
            return null;
        }
        return createDateFormat(format).parse(date);
    }

    public Date parse(String date, boolean withTime) throws ParseException {
        return parse(date, getDefaultPattern(withTime));
    }
}
