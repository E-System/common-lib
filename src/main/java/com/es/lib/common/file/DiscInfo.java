package com.es.lib.common.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.NumberFormat;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class DiscInfo {

    private final long total;
    private final long free;

    public static DiscInfo create() {
        try {
            long total = 0;
            long free = 0;
            for (File file : File.listRoots()) {
                total += file.getTotalSpace();
                free += file.getFreeSpace();
            }
            return new DiscInfo(total, free);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static DiscInfo create(String total, String free) {
        if (StringUtils.isNotBlank(total) && StringUtils.isNotBlank(free)) {
            return new DiscInfo(Long.parseLong(total), Long.parseLong(free));
        }
        return null;
    }

    public String asString() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        double totalGb = ((double) total / (1024 * 1024 * 1024));
        double freeGb = ((double) free / (1024 * 1024 * 1024));
        return numberFormat.format(freeGb) + "/" + numberFormat.format(totalGb);
    }
}