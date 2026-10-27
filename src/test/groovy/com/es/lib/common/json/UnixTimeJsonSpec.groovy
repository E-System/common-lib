package com.es.lib.common.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import spock.lang.Specification

class UnixTimeJsonSpec extends Specification {

    static class TimeDTO {
        @JsonSerialize(using = UnixTimeSerializer)
        @JsonDeserialize(using = UnixTimeDeserializer)
        Date date
    }

    def "Serialize and deserialize Date using Unix time in seconds"() {
        given:
        ObjectMapper mapper = new ObjectMapper()
        Date date = new Date(1586044800000L) // in ms
        TimeDTO dto = new TimeDTO(date: date)

        when:
        String json = mapper.writeValueAsString(dto)

        then:
        json.contains('"1586044800"')

        when:
        TimeDTO deserialized = mapper.readValue('{"date":"1586044800"}', TimeDTO)

        then:
        deserialized.date == date
    }
}
