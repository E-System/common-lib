package com.es.lib.common.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AttrsCredentials extends Credentials {

    public static final String ATTR_URI = "URI";

    private final Map<String, String> attrs;

    public AttrsCredentials(String login, String password, Collection<Map.Entry<String, String>> attrs) {
        this(login, password, attrs != null ? attrs.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null);
    }

    @JsonCreator
    public AttrsCredentials(
        @JsonProperty("login") String login,
        @JsonProperty("password") String password,
        @JsonProperty("attrs") Map<String, String> attrs
    ) {
        super(login, password);
        this.attrs = attrs != null ? attrs : new HashMap<>();
    }

    public Object clone() throws CloneNotSupportedException {
        return new AttrsCredentials(getLogin(), getPassword(), attrs);
    }
}