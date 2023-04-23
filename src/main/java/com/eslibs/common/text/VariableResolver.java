package com.eslibs.common.text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VariableResolver {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(".*(\\$\\{(.*)}).*");

    private VariableResolver() { }

    private static Map<String, String> extract(CharSequence sequence) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(sequence);
        Map<String, String> result = new HashMap<>(2);
        while (matcher.find()) {
            result.put(matcher.group(1), matcher.group(2));
        }
        return result;
    }

    static Object resolve(Object value, Function<String, Object> variableResolver) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof CharSequence)) {
            return value;
        }
        Map<String, String> vars = extract((CharSequence) value);
        if (vars.isEmpty()) {
            return value;
        }
        String result = (String) value;
        for (Map.Entry<String, String> var : vars.entrySet()) {
            Object varValue = variableResolver.apply(var.getValue());
            if (varValue != null) {
                result = result.replaceAll(Pattern.quote(var.getKey()), (String) varValue);
            }
        }
        return result;
    }
}