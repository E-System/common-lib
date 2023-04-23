package com.eslibs.common.date;

import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class DateFormatter extends DateProcessor {

    public DateFormatter(ZoneId zoneId, Locale locale) {
        super(zoneId, locale);
    }

    public String format(Date date, String format, String defaultValue) {
        if (date == null) {
            return defaultValue;
        }
        return createDateFormat(format).format(date);
    }

    public String format(Date date, String format) {
        return format(date, format, null);
    }

    public String format(Date date, boolean withTime) {
        return format(date, getDefaultPattern(withTime));
    }

    public String formatSortable(Date date, boolean withTime) {
        return format(date, getSortablePattern(withTime));
    }

    public String now(boolean withTime) {
        return now(getDefaultPattern(withTime));
    }

    public String now(String format) {
        return format(Dates.generator(zoneId).now(), format);
    }

    public String yearIndex(Date date) {
        return format(date, "yy");
    }
}
