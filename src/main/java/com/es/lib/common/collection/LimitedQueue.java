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

package com.es.lib.common.collection;

import lombok.RequiredArgsConstructor;

import java.util.LinkedList;

/**
 * Collection with max N elements
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 18.10.15
 */
@RequiredArgsConstructor
public class LimitedQueue<E> extends LinkedList<E> {

    private final int limit;

    @Override
    public boolean add(E o) {
        boolean added = super.add(o);
        while (added && size() > limit) {
            super.remove();
        }
        return added;
    }
}
