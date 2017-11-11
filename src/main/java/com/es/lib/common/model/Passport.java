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

package com.es.lib.common.model;

import java.util.Date;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 23.09.16
 */
public class Passport {

    private String serial;
    private String number;
    private String issued;
    private Date issuedDate;
    private String divisionCode;

    public Passport(String serial, String number, String issued, Date issuedDate, String divisionCode) {
        this.serial = serial;
        this.number = number;
        this.issued = issued;
        this.issuedDate = issuedDate;
        this.divisionCode = divisionCode;
    }

    public String getSerial() {
        return serial;
    }

    public String getNumber() {
        return number;
    }

    public String getIssued() {
        return issued;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    @Override
    public String toString() {
        return "Passport{" +
               "serial='" + serial + '\'' +
               ", number='" + number + '\'' +
               ", issued='" + issued + '\'' +
               ", issuedDate=" + issuedDate +
               ", divisionCode='" + divisionCode + '\'' +
               '}';
    }
}
