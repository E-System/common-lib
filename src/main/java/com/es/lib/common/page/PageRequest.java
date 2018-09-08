package com.es.lib.common.page;

import java.io.Serializable;

public class PageRequest implements Serializable {

    private int offset;
    private int limit;

    public PageRequest(int countOnPage) {
        if (countOnPage <= 0) {
            throw new IllegalArgumentException("countOnPage must be greater than zero");
        }
        this.offset = 0;
        this.limit = countOnPage;
    }

    public PageRequest(int countOnPage, int page) {
        this(countOnPage);
        if (page <= 0) {
            page = 1;
        }
        this.offset = (page - 1) * countOnPage;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
               "offset=" + offset +
               ", limit=" + limit +
               '}';
    }
}
