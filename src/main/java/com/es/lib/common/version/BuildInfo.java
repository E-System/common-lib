package com.es.lib.common.version;

import com.es.lib.common.HashUtil;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 29.06.17
 */
public class BuildInfo implements Serializable {

    private String name;
    private String version;
    private String date;

    public BuildInfo() { }

    public BuildInfo(String name, String version, String date) {
        this.name = name;
        this.version = version;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHash() {
        return HashUtil.md5(name + version + date);
    }

    public Map<String, String> asMap() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("name", getName());
        result.put("date", getDate());
        result.put("hash", getHash());
        result.put("version", getVersion());
        return result;
    }

    @Override
    public String toString() {
        return "BuildInfo{" +
               "name='" + name + '\'' +
               ", version='" + version + '\'' +
               ", date='" + date + '\'' +
               '}';
    }
}