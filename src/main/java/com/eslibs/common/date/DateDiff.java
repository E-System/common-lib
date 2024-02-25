package com.eslibs.common.date;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DateDiff {

    protected final ChronoUnit unit;
    protected final ZoneId zoneId;

    public long get(Temporal start) {
        return get(start, null);
    }

    public long get(Temporal start, Temporal end) {
        return unit.between(start, end != null ? end : now());
    }

    private Temporal now() {
        return zoneId != null ? LocalDateTime.now(zoneId) : LocalDateTime.now();
    }
}
