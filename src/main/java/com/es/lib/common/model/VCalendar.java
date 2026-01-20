package com.es.lib.common.model;

import com.es.lib.common.collection.Items;
import com.es.lib.common.date.Dates;
import com.es.lib.common.text.Texts;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class VCalendar {

    private final String prod;
    private final String version;
    private final String scale;
    private final String method;
    private final ZoneId zoneId;
    private final Collection<Event> events;

    public static VCalendar deserialize(InputStream source) throws IOException {
        return deserialize(IOUtils.toString(source, StandardCharsets.UTF_8));
    }

    public static VCalendar deserialize(String source) {
        if (!source.contains(Token.VCALENDAR)) {
            return null;
        }
        Collection<Token> tokens = tokens(source);
        State state = null;
        Map<String, Token> calendarTokens = new HashMap<>();
        Collection<Map<String, Token>> eventsTokens = new ArrayList<>();
        Map<String, Token> eventTokens = null;
        for (Token t : tokens) {
            if (Token.BEGIN.equals(t.name)) {
                if (Token.VCALENDAR.equals(t.value)) {
                    state = State.CALENDAR;
                } else if (State.CALENDAR.equals(state) && Token.VEVENT.equals(t.value)) {
                    state = State.EVENT;
                    eventTokens = new HashMap<>();
                }
            } else if (Token.END.equals(t.name)) {
                if (Token.VCALENDAR.equals(t.value)) {
                    state = null;
                } else if (State.EVENT.equals(state) && Token.VEVENT.equals(t.value)) {
                    eventsTokens.add(eventTokens);
                    state = State.CALENDAR;
                }
            } else {
                if (state != null) {
                    if (State.CALENDAR.equals(state)) {
                        calendarTokens.put(t.name, t);
                    } else {
                        eventTokens.put(t.name, t);
                    }
                }
            }
        }
        System.out.println(calendarTokens);
        eventsTokens.forEach(System.out::println);
        Collection<Event> events = new ArrayList<>();
        ZoneId zoneId = calendarTokens.getOrDefault(Token.X_WR_TIMEZONE, Token.NULL).zoneId();
        for (Map<String, Token> eTokens : eventsTokens) {
            events.add(new Event(
                eTokens.getOrDefault(Token.UID, Token.NULL).value,
                eTokens.getOrDefault(Token.DTSTART, Token.NULL).date(zoneId),
                eTokens.getOrDefault(Token.DTEND, Token.NULL).date(zoneId),
                eTokens.getOrDefault(Token.SUMMARY, Token.NULL).value,
                eTokens.getOrDefault(Token.DESCRIPTION, Token.NULL).value,
                eTokens.getOrDefault(Token.STATUS, Token.NULL).value,
                eTokens.getOrDefault(Token.TRANSP, Token.NULL).value
            ));
        }
        return new VCalendar(
            calendarTokens.getOrDefault(Token.PRODID, Token.NULL).value,
            calendarTokens.getOrDefault(Token.VERSION, Token.NULL).value,
            calendarTokens.getOrDefault(Token.CALSCALE, Token.NULL).value,
            calendarTokens.getOrDefault(Token.METHOD, Token.NULL).value,
            zoneId,
            events
        );
    }

    public static byte[] serialize(VCalendar calendar) {
        StringJoiner sj = new StringJoiner("\n");
        tokens(calendar).forEach(v -> sj.add(v.serialize()));
        return sj.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] serialize() {
        return serialize(this);
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class Event {

        private final String id;
        private final Date startDate;
        private final Date endDate;
        private final String summary;
        private final String description;
        private final String status;
        private final String transp;

        public Event(String id, Date startDate, Date endDate, String summary) {
            this(id, startDate, endDate, summary, null, null, null);
        }
    }


    private static Collection<Token> tokens(VCalendar calendar) {
        Collection<Token> result = new ArrayList<>();
        result.add(new Token(Token.BEGIN, Token.VCALENDAR));
        if (StringUtils.isNotBlank(calendar.version)) {
            result.add(new Token(Token.VERSION, calendar.version));
        }
        if (StringUtils.isNotBlank(calendar.prod)) {
            result.add(new Token(Token.PRODID, calendar.prod));
        }
        if (StringUtils.isNotBlank(calendar.method)) {
            result.add(new Token(Token.METHOD, calendar.method));
        }
        if (calendar.zoneId != null) {
            result.add(new Token(Token.X_WR_TIMEZONE, calendar.zoneId.getId()));
        }
        for (Event event : calendar.events) {
            result.add(new Token(Token.BEGIN, Token.VEVENT));
            result.add(new Token(Token.UID, event.getId()));
            result.add(new Token(Token.DTSTART, Dates.formatter().format(event.getStartDate(), Token.DATE_PATTERN_SHORT), Token.DATE_SPECS));
            result.add(new Token(Token.DTEND, Dates.formatter().format(event.getEndDate(), Token.DATE_PATTERN_SHORT), Token.DATE_SPECS));
            result.add(new Token(Token.SUMMARY, event.getSummary()));
            result.add(new Token(Token.END, Token.VEVENT));
        }
        result.add(new Token(Token.END, Token.VCALENDAR));
        return result;
    }

    private static Collection<Token> tokens(String source) {
        return Pattern.compile("\\R").splitAsStream(source).map(Token::of).collect(Collectors.toList());
    }

    private enum State {
        CALENDAR,
        EVENT
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    private static class Token {

        public static final String DATE_PATTERN_SHORT = "yyyyMMdd";
        public static final String DATE_PATTERN_FULL = "yyyyMMdd'T'HHmmss";
        public static final Token NULL = new Token(null, null);
        public static final Map<String, String> DATE_SPECS = new HashMap<String, String>() {{
            put(VALUE, DATE);
        }};

        public static final String BEGIN = "BEGIN";
        public static final String END = "END";

        public static final String VCALENDAR = "VCALENDAR";

        public static final String VERSION = "VERSION";
        public static final String METHOD = "METHOD";
        public static final String PRODID = "PRODID";
        public static final String CALSCALE = "CALSCALE";
        public static final String X_WR_TIMEZONE = "X-WR-TIMEZONE";

        public static final String VEVENT = "VEVENT";

        public static final String UID = "UID";
        public static final String SUMMARY = "SUMMARY";
        public static final String DTSTART = "DTSTART";
        public static final String DTEND = "DTEND";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String STATUS = "STATUS";
        public static final String TRANSP = "TRANSP";

        public static final String VALUE = "VALUE";
        public static final String DATE = "DATE";


        private final String name;
        private final String value;
        private final Map<String, String> specs;

        public Token(String name, String value) {
            this(name, value, new HashMap<>());
        }

        public ZoneId zoneId() {
            return StringUtils.isNotBlank(value) ? ZoneId.of(value) : null;
        }

        public String serialize() {
            String specs = "";
            if (Items.isNotEmpty(this.specs)) {
                specs = this.specs.entrySet().stream().map(v -> v.getKey() + "=" + v.getValue()).collect(Collectors.joining(";", ";", ""));
            }
            return name + specs + ":" + value;
        }

        public Date date(ZoneId zoneId) {
            String format = DATE_PATTERN_FULL;
            if (DATE.equals(specs.get(VALUE))) {
                format = DATE_PATTERN_SHORT;
            }
            try {
                return Dates.parser(zoneId).parse(value, format);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public static Token of(String line) {
            return Texts.splitBy(":", 2, true).toObject(line, v -> {
                String key = v[0];
                Map<String, String> specs = new HashMap<>();
                if (key.contains(";")) {
                    List<String> specsPart = Texts.splitBy(";", true).toList(key);
                    for (int i = 0; i < specsPart.size(); ++i) {
                        String part = specsPart.get(i);
                        if (i == 0) {
                            key = part;
                        } else {
                            String[] spec = Texts.splitBy("=", true).toArray(part);
                            specs.put(spec[0], spec[1]);
                        }
                    }
                }
                String value = v[1];
                return new Token(key, value, specs);
            });
        }
    }
}
