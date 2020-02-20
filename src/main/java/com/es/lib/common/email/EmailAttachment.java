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

package com.es.lib.common.email;

import com.es.lib.common.model.data.OutputData;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Getter
@ToString(callSuper = true)
public class EmailAttachment extends EmailBaseAttachment {

    private String cid;

    public EmailAttachment(OutputData data) {
        super(data);
    }

    public EmailAttachment(String cid, OutputData data) {
        super(data);
        this.cid = cid;
    }
}
