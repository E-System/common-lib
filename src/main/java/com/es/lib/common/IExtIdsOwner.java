package com.es.lib.common;

import java.util.HashMap;
import java.util.Map;

public interface IExtIdsOwner {

    Map<String, String> getExtIds();

    void setExtIds(Map<String, String> extIds);

    default void setExtId(String name, String value) {
        Map<String, String> items = getExtIds();
        if (items == null) {
            items = new HashMap<>();
            setExtIds(items);
        }
        items.put(name, value);
    }

    default void setExtId(Enum<?> name, String value) {
        setExtId(name.name(), value);
    }
}