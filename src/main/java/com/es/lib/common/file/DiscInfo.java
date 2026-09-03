package com.es.lib.common.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

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
}