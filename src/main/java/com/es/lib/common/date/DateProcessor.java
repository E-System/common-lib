package com.es.lib.common.date;

import com.es.lib.common.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class DateProcessor {

    protected final ZoneId zoneId;
    protected final Locale locale;

    protected SimpleDateFormat createDateFormat(String format) {
        SimpleDateFormat result = locale != null ? new SimpleDateFormat(format, locale) : new SimpleDateFormat(format);
        if (zoneId != null) {
            result.setTimeZone(TimeZone.getTimeZone(zoneId));
        }
        return result;
    }

    protected String getDefaultPattern(boolean withTime) {
        return withTime ? Constant.DEFAULT_DATE_TIME_PATTERN : Constant.DEFAULT_DATE_PATTERN;
    }

    protected String getSortablePattern(boolean withTime) {
        return withTime ? Constant.SORTABLE_DATE_TIME_PATTERN : Constant.SORTABLE_DATE_PATTERN;
    }
}
