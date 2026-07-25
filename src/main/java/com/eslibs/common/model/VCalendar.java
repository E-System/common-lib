package com.eslibs.common.model;

import com.eslibs.common.collection.Items;
import com.eslibs.common.text.Texts;
import lombok.Builder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record VCalendar(
    String prod,
    String version,
    String scale,
    String method,
    ZoneId zoneId,
    Collection<Event> events
) {

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
        Collection<Event> events = new ArrayList<>();
        ZoneId zoneId = calendarTokens.getOrDefault(Token.X_WR_TIMEZONE, Token.NULL).zoneId();
        for (Map<String, Token> eTokens : eventsTokens) {
            events.add(
                Event.builder()
                    .id(eTokens.getOrDefault(Token.UID, Token.NULL).value)
                    .startDate(eTokens.getOrDefault(Token.DTSTART, Token.NULL).date(zoneId))
                    .endDate(eTokens.getOrDefault(Token.DTEND, Token.NULL).date(zoneId))
                    .summary(eTokens.getOrDefault(Token.SUMMARY, Token.NULL).value)
                    .description(eTokens.getOrDefault(Token.DESCRIPTION, Token.NULL).value)
                    .status(eTokens.getOrDefault(Token.STATUS, Token.NULL).value)
                    .transp(eTokens.getOrDefault(Token.TRANSP, Token.NULL).value)
                    .build()
            );
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


    @Builder
    public record Event(
        String id,
        EventDate startDate,
        EventDate endDate,
        String summary,
        String description,
        String status,
        String transp) {}

    @Builder
    public record EventDate(LocalDateTime value, boolean full) {

        public EventDate(LocalDateTime value) {
            this(value, false);
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
            result.add(new Token(Token.UID, event.id()));
            result.add(token(Token.DTSTART, event.startDate()));
            result.add(token(Token.DTEND, event.endDate()));
            result.add(new Token(Token.SUMMARY, event.summary()));
            result.add(new Token(Token.DESCRIPTION, event.description()));
            result.add(new Token(Token.END, Token.VEVENT));
        }
        result.add(new Token(Token.END, Token.VCALENDAR));
        return result;
    }

    private static Token token(String name, EventDate date) {
        return new Token(
            name,
            DateTimeFormatter.ofPattern(date.full ? Token.DATE_PATTERN_FULL : Token.DATE_PATTERN_SHORT).format(date.value),
            date.full ? null : Token.DATE_SPECS
        );
    }

    private static Collection<Token> tokens(String source) {
        return Pattern.compile("\\R").splitAsStream(source).map(Token::of).collect(Collectors.toList());
    }

    private enum State {
        CALENDAR,
        EVENT
    }

    private record Token(
        String name,
        String value,
        Map<String, String> specs
    ) {

        public static final String VALUE = "VALUE";
        public static final String DATE = "DATE";

        public static final String DATE_PATTERN_SHORT = "yyyyMMdd";
        public static final String DATE_PATTERN_FULL = "yyyyMMdd'T'HHmmss";
        public static final Token NULL = new Token(null, null);
        public static final Map<String, String> DATE_SPECS = Map.of(VALUE, DATE);

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

        public EventDate date(ZoneId zoneId) {
            boolean full = !DATE.equals(specs.get(VALUE));
            String pattern = DATE_PATTERN_FULL;
            if (!full) {
                pattern = DATE_PATTERN_SHORT;
            }
            try {
                return new EventDate(parse(zoneId, pattern), full);
            } catch (DateTimeParseException e) {
                throw new RuntimeException(e);
            }
        }

        private LocalDateTime parse(ZoneId zoneId, String pattern) {
            return new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter().withZone(zoneId)
                .parse(value, LocalDateTime::from);
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
