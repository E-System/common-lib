/*
 * Copyright (c) E-System LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by E-System team (https://ext-system.com), 2016
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
