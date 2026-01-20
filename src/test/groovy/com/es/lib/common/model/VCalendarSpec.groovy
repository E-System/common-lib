package com.es.lib.common.model

import com.es.lib.common.date.Dates
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

class VCalendarSpec extends Specification {

    def "Parse 1"() {
        when:
        def source = VCalendar.getResourceAsStream("/calendars/2766853728.ics")
        def item = VCalendar.deserialize(source)
        println(item)
        then:
        item.prod == 'Avito.ru'
        item.version == '2.0'
        item.scale == null
        item.method == 'PUBLISH'
        item.zoneId == null
        item.events.size() == 6
    }

    def "Parse 2"() {
        when:
        def source = VCalendar.getResourceAsStream("/calendars/3598891270.ics")
        def item = VCalendar.deserialize(source)
        println(item)
        then:
        item.prod == 'Avito.ru'
        item.version == '2.0'
        item.scale == null
        item.method == 'PUBLISH'
        item.zoneId == null
        item.events.size() == 0
    }

    def "Parse 3"() {
        when:
        def source = VCalendar.getResourceAsStream("/calendars/3599223072.ics")
        def item = VCalendar.deserialize(source)
        println(item)
        then:
        item.prod == 'Avito.ru'
        item.version == '2.0'
        item.scale == null
        item.method == 'PUBLISH'
        item.zoneId == null
        item.events.size() == 2
    }

    def "Parse 4"() {
        when:
        def source = VCalendar.getResourceAsStream("/calendars/calendar.ics")
        def item = VCalendar.deserialize(source)
        println(item)
        then:
        item.prod == '//ApiCalendar//Sutochno.ru_1.0'
        item.version == '2.0'
        item.scale == 'GREGORIAN'
        item.method == 'PUBLISH'
        item.zoneId == ZoneId.of("Europe/Moscow")
        item.events.size() == 5
    }

    def "Parse 5"() {
        when:
        def source = VCalendar.getResourceAsStream("/calendars/calendar-2.ics")
        def item = VCalendar.deserialize(source)
        println(item)
        then:
        item.prod == '//ApiCalendar//Sutochno.ru_1.0'
        item.version == '2.0'
        item.scale == 'GREGORIAN'
        item.method == 'PUBLISH'
        item.zoneId == ZoneId.of("Europe/Moscow")
        item.events.size() == 4
    }

    def "Parse 6"() {
        when:
        def source = VCalendar.getResourceAsStream("/calendars/calendar-3.ics")
        def item = VCalendar.deserialize(source)
        println(item)
        then:
        item.prod == '//ApiCalendar//Sutochno.ru_1.0'
        item.version == '2.0'
        item.scale == 'GREGORIAN'
        item.method == 'PUBLISH'
        item.zoneId == ZoneId.of("Europe/Moscow")
        item.events.size() == 2
    }

    def "Serialize"() {
        when:
        def calendar = new VCalendar(
                "GeshGo",
                "2.0",
                null,
                "PUBLISH",
                ZoneId.of("Asia/Barnaul"),
                [
                        new VCalendar.Event(
                                UUID.randomUUID().toString(),
                                Dates.converter().get(LocalDate.of(2024, 7, 11).atStartOfDay()),
                                Dates.converter().get(LocalDate.of(2024, 7, 12).atStartOfDay()),
                                "GeshGo (Booked)"
                        ),
                        new VCalendar.Event(
                                UUID.randomUUID().toString(),
                                Dates.converter().get(LocalDate.of(2024, 8, 11).atStartOfDay()),
                                Dates.converter().get(LocalDate.of(2024, 8, 20).atStartOfDay()),
                                "GeshGo (Booked)"
                        )
                ]
        )
        def item = new String(VCalendar.serialize(calendar))
        println(item)
        then:
        !item.isEmpty()
    }

}
