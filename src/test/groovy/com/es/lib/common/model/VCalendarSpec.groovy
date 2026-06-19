package com.es.lib.common.model

import com.es.lib.common.DateUtil
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
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
        with(item.events){
            it[0].startDate != null
            !it[0].startDate.full
        }
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
        with(item.events){
            it[0].startDate != null
            it[0].startDate.full
        }
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
                        VCalendar.Event.builder()
                                .id(UUID.randomUUID().toString())
                                .startDate(new VCalendar.EventDate(DateUtil.convert(LocalDate.of(2024, 7, 11).atStartOfDay())))
                                .endDate(new VCalendar.EventDate(DateUtil.convert(LocalDate.of(2024, 7, 12).atStartOfDay())))
                                .summary("GeshGo (Booked)").build(),
                        VCalendar.Event.builder()
                                .id(UUID.randomUUID().toString())
                                .startDate(new VCalendar.EventDate(DateUtil.convert(LocalDate.of(2024, 8, 11).atStartOfDay())))
                                .endDate(new VCalendar.EventDate(DateUtil.convert(LocalDate.of(2024, 8, 20).atStartOfDay())))
                                .summary("GeshGo (Booked)").build()
                ]
        )
        def item = new String(calendar.serialize())
        def item2 = new String(VCalendar.serialize(calendar))
        println(item)
        item == item2
        then:
        !item.isEmpty()
        !item2.isEmpty()
    }

    def "Calendar for ticket"() {
        when:
        def calendar = new VCalendar(
                "Biletik Online",
                "2.0",
                null,
                "PUBLISH",
                ZoneId.of("Europe/Moscow"),
                [
                        VCalendar.Event.builder()
                                .id("order-1@biletik.online")
                                .startDate(new VCalendar.EventDate(DateUtil.convert(LocalDateTime.now()), true))
                                .endDate(new VCalendar.EventDate(DateUtil.convert(LocalDateTime.now().plusHours(5)), true))
                                .summary("Поездка: Сочи - Анапа 12.02.2026 14:00")
                                .description("Отправление: Сочи,ул.Тестовая").build()
                ]
        )
        def item = new String(calendar.serialize())
        def item2 = new String(VCalendar.serialize(calendar))
        def item3 = VCalendar.deserialize(item)
        println(item)
        item == item2
        then:
        !item.isEmpty()
        !item2.isEmpty()
        println(item3)
        with(item3.events){
            it[0].startDate != null
            it[0].startDate.full
        }
        /*  sb.append("UID:order-").append(order.getId()).append("@biletik.online\r\n");

          if (!dtStart.isEmpty()) {
              sb.append("DTSTART:").append(dtStart).append("\r\n");
              sb.append("DTEND:").append(dtEnd).append("\r\n");
          }

          sb.append("SUMMARY:Поездка: ").append(order.getDepartureName())
                  .append(" - ").append(order.getDestinationName()).append("\r\n");

          if (order.getDepartureAddress() != null) {
              sb.append("LOCATION:").append(order.getDepartureName())
                      .append(", ").append(order.getDepartureAddress()).append("\r\n");
          }

          sb.append("DESCRIPTION:Ваш билет на Biletik.online. Стоимость: ")
                  .append(order.getTotal()).append(" руб.\r\n");

          sb.append("END:VEVENT\r\n");
          sb.append("END:VCALENDAR\r\n");

          return sb.toString().getBytes(StandardCharsets.UTF_8);*/
    }

}
