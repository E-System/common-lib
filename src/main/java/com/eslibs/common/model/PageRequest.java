/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.eslibs.common.model;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 08.09.18
 */
@Getter
@ToString
public class PageRequest implements Serializable {

    private final int limit;
    private int offset;
    private int page;

    public PageRequest(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be greater than zero");
        }
        this.limit = limit;

        this.page = 1;
        this.offset = 0;
    }

    public PageRequest(int countOnPage, int page) {
        this(countOnPage);
        if (page <= 0) {
            page = 1;
        }
        this.page = page;
        this.offset = (page - 1) * countOnPage;
    }
}
