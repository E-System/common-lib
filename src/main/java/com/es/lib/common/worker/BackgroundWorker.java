package com.es.lib.common.worker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public abstract class BackgroundWorker implements Runnable {

    protected void process() {
        Info info = getInfo();
        getLogger().trace("Start worker [{}]", info);
        try {
            doWork();
        } catch (Throwable t) {
            getLogger().error("Worker error [{}]", info, t);
            doOnError(t);
        }
        getLogger().trace("End worker [{}]", info);
    }

    protected abstract void doWork();

    protected void doOnError(Throwable throwable) {}

    protected abstract Info getInfo();

    protected Info nameInfo(String name) {
        return new Info(name);
    }

    protected Logger getLogger(){
        return log;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Info {

        private final String name;
    }
}
