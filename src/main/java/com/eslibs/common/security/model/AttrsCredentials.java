package com.eslibs.common.security.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString(callSuper = true)
public class AttrsCredentials extends Credentials {

    public static final String ATTR_URI = "URI";

    private final Map<String, String> attrs;

    public AttrsCredentials(String login, String password, Collection<Map.Entry<String, String>> attrs) {
        this(login, password, attrs != null ? attrs.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null);
    }

    public AttrsCredentials(String login, String password, Map<String, String> attrs) {
        super(login, password);
        this.attrs = attrs != null ? attrs : new HashMap<>();
    }

    public Object clone() throws CloneNotSupportedException {
        return new AttrsCredentials(getLogin(), getPassword(), attrs);
    }
}
