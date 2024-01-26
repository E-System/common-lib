package com.eslibs.common


import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

import java.util.function.BiConsumer
import java.util.function.Consumer

class PatcherSpec extends Specification {

    @Shared
    def mapper = new ObjectMapper()

    def "Rules all"() {
        when:
        DTO dto = new DTO("NAME", "NAME2", new DTOInternal(1), EntityCode.CODE.name(), true)
        Entity entity = new Entity()
        Patcher.create(dto, entity, Arrays.asList("name", "name2", "role", "code", "valid"))
                .rule("name", DTO::getName, Entity::setName)
                .rule("name2", new BiConsumer<DTO, Entity>() {

                    @Override
                    void accept(DTO from, Entity to) {
                        to.setName2(from.getName2())
                    }
                })
                .rule("role", DTO::getRole, Entity::setRole, v -> new EntityInternal(v.getId()))
                .rule("code", DTO::getCode, Entity::setCode, EntityCode::valueOf)
                .apply()
        then:
        entity.name == dto.name
        entity.name2 == dto.name2
        entity.role.id == dto.role.id
        entity.code.name() == dto.code
    }

    def "Rules all with null fields"() {
        when:
        DTO dto = new DTO("NAME", "NAME2", new DTOInternal(1), EntityCode.CODE.name(), true)
        Entity entity = new Entity()
        Patcher.create(dto, entity)
                .rule("name", DTO::getName, Entity::setName)
                .rule("name2", new BiConsumer<DTO, Entity>() {

                    @Override
                    void accept(DTO from, Entity to) {
                        to.setName2(from.getName2())
                    }
                })
                .rule("role", DTO::getRole, Entity::setRole, v -> new EntityInternal(v.getId()))
                .rule("code", DTO::getCode, Entity::setCode, EntityCode::valueOf)
                .rule("valid", DTO::isValid, Entity::setValid)
                .apply()
        then:
        entity.name == dto.name
        entity.name2 == dto.name2
        entity.role.id == dto.role.id
        entity.code.name() == dto.code
    }

    def "Rules partial"() {
        when:
        DTO dto = new DTO("NAME", "NAME2", new DTOInternal(1), EntityCode.CODE.name(), true)
        Entity entity = new Entity()
        Patcher.create(dto, entity, Arrays.asList("name", "code"))
                .rule("name", DTO::getName, Entity::setName)
                .rule("name2", new BiConsumer<DTO, Entity>() {

                    @Override
                    void accept(DTO from, Entity to) {
                        to.setName2(from.getName2())
                    }
                })
                .rule("role", DTO::getRole, Entity::setRole, v -> new EntityInternal(v.getId()))
                .rule("code", DTO::getCode, Entity::setCode, EntityCode::valueOf)
                .apply()
        then:
        entity.name == dto.name
        entity.name2 == null
        entity.role == null
        entity.code.name() == dto.code
    }

    def "Rules with reflective"() {
        when:
        DTO dto = new DTO("NAME", "NAME2", new DTOInternal(1), EntityCode.CODE.name(), true)
        Entity entity = new Entity()
        Patcher.create(dto, entity, Arrays.asList("name", "name2", "role", "code", "valid"))
                .rule("name")
                .rule("name2")
                .rule("valid")
                .rule("role", DTO::getRole, Entity::setRole, v -> new EntityInternal(v.getId()))
                .rule("code", DTO::getCode, Entity::setCode, EntityCode::valueOf)
                .apply()
        then:
        entity.name == dto.name
        entity.name2 == dto.name2
        entity.role.id == dto.role.id
        entity.code.name() == dto.code
        entity.valid == dto.valid
    }

    def "On change fields"() {
        when:
        DTO dto = new DTO("NAME", "NAME2", new DTOInternal(1), EntityCode.CODE.name(), true)
        Entity entity = new Entity()
        def items = Patcher.create(dto, entity, Arrays.asList("name", "name2", "role", "code", "valid"))
                .rule("name", true)
                .rule("name2", true)
                .rule("valid", true)
                .rule("role", DTO::getRole, Entity::setRole, v -> new EntityInternal(v.getId()))
                .rule("code", DTO::getCode, Entity::setCode, EntityCode::valueOf, Entity::getCode, v -> v.toString())
                .apply().items()
        then:
        !items.empty
        items.size() == 4
        items[0].field == 'name'
        (items[0] as Patcher.UpdatedField).was == null
        (items[0] as Patcher.UpdatedField).became == 'NAME'
        items[1].field == 'name2'
        (items[1] as Patcher.UpdatedField).was == null
        (items[1] as Patcher.UpdatedField).became == 'NAME2'
        items[2].field == 'valid'
        (items[2] as Patcher.UpdatedField).was == 'false'
        (items[2] as Patcher.UpdatedField).became == 'true'
        items[3].field == 'code'
        (items[3] as Patcher.UpdatedField).was == null
        (items[3] as Patcher.UpdatedField).became == 'CODE'
    }

    def "On change fields with callback"() {
        when:
        List<Patcher.Updated> callbackItems = []
        def consumer = new Consumer<Patcher.Updated>() {

            @Override
            void accept(Patcher.Updated updatedField) {
                callbackItems.add(updatedField)
            }
        }
        DTO dto = new DTO("NAME", "NAME2", new DTOInternal(1), EntityCode.CODE.name(), true)
        Entity entity = new Entity()
        def items = Patcher.create(dto, entity, Arrays.asList("name", "name2", "role", "code", "valid"))
                .rule("name", true, consumer)
                .rule("name2", true, consumer)
                .rule("valid", true, consumer)
                .rule("role", DTO::getRole, Entity::setRole, v -> new EntityInternal(v.getId()))
                .rule("code", DTO::getCode, Entity::setCode, EntityCode::valueOf, Entity::getCode, v -> v.toString(), consumer)
                .apply().items()
        then:
        !items.empty
        items.size() == 4
        items[0].field == 'name'
        (items[0] as Patcher.UpdatedField).was == null
        (items[0] as Patcher.UpdatedField).became == 'NAME'
        items[1].field == 'name2'
        (items[1] as Patcher.UpdatedField).was == null
        (items[1] as Patcher.UpdatedField).became == 'NAME2'
        items[2].field == 'valid'
        (items[2] as Patcher.UpdatedField).was == 'false'
        (items[2] as Patcher.UpdatedField).became == 'true'
        items[3].field == 'code'
        (items[3] as Patcher.UpdatedField).was == null
        (items[3] as Patcher.UpdatedField).became == 'CODE'

        !callbackItems.empty
        callbackItems.size() == 4
        callbackItems[0].field == 'name'
        (callbackItems[0] as Patcher.UpdatedField).was == null
        (callbackItems[0] as Patcher.UpdatedField).became == 'NAME'
        callbackItems[1].field == 'name2'
        (callbackItems[1] as Patcher.UpdatedField).was == null
        (callbackItems[1] as Patcher.UpdatedField).became == 'NAME2'
        callbackItems[2].field == 'valid'
        (callbackItems[2] as Patcher.UpdatedField).was == 'false'
        (callbackItems[2] as Patcher.UpdatedField).became == 'true'
        callbackItems[3].field == 'code'
        (callbackItems[3] as Patcher.UpdatedField).was == null
        (callbackItems[3] as Patcher.UpdatedField).became == 'CODE'
    }

    def "On change fields with callback and labels"() {
        when:
        List<Patcher.Updated> callbackItems = []
        def consumer = new Consumer<Patcher.Updated>() {

            @Override
            void accept(Patcher.Updated updatedField) {
                callbackItems.add(updatedField)
            }
        }
        DTO dto = new DTO("NAME", "NAME2", new DTOInternal(1), EntityCode.CODE.name(), true)
        Entity entity = new Entity()
        def items = Patcher.create(dto, entity, Arrays.asList("name", "name2", "role", "code", "valid"))
                .rule("name", true, consumer)
                .rule("name2", true, consumer)
                .rule("valid", true, consumer)
                .rule("role", DTO::getRole, Entity::setRole, v -> new EntityInternal(v.getId()))
                .rule("code", DTO::getCode, Entity::setCode, EntityCode::valueOf, Entity::getCode, v -> v.toString(), consumer)
                .apply([name: "Name", code: "Code"]).items()
        then:
        !items.empty
        items.size() == 4
        items[0].field == 'Name'
        (items[0] as Patcher.UpdatedField).was == null
        (items[0] as Patcher.UpdatedField).became == 'NAME'
        items[1].field == 'name2'
        (items[1] as Patcher.UpdatedField).was == null
        (items[1] as Patcher.UpdatedField).became == 'NAME2'
        items[2].field == 'valid'
        (items[2] as Patcher.UpdatedField).was == 'false'
        (items[2] as Patcher.UpdatedField).became == 'true'
        items[3].field == 'Code'
        (items[3] as Patcher.UpdatedField).was == null
        (items[3] as Patcher.UpdatedField).became == 'CODE'

        !callbackItems.empty
        callbackItems.size() == 4
        callbackItems[0].field == 'Name'
        (callbackItems[0] as Patcher.UpdatedField).was == null
        (callbackItems[0] as Patcher.UpdatedField).became == 'NAME'
        callbackItems[1].field == 'name2'
        (callbackItems[1] as Patcher.UpdatedField).was == null
        (callbackItems[1] as Patcher.UpdatedField).became == 'NAME2'
        callbackItems[2].field == 'valid'
        (callbackItems[2] as Patcher.UpdatedField).was == 'false'
        (callbackItems[2] as Patcher.UpdatedField).became == 'true'
        callbackItems[3].field == 'Code'
        (callbackItems[3] as Patcher.UpdatedField).was == null
        (callbackItems[3] as Patcher.UpdatedField).became == 'CODE'
    }

    def "Serialize and deserialize UpdatedField"() {
        when:
        def item = new Patcher.UpdatedField('code', 'was', 'became')
        def json = mapper.writeValueAsString(item)
        println(json)
        def result = mapper.readValue(json, Patcher.UpdatedField.class)
        def result2 = mapper.readValue('{"field":"code","was":"was","became":"became"}', Patcher.UpdatedField.class)
        then:
        item.field == result.field
        item.was == result.was
        item.became == result.became
        item.type == Patcher.Updated.Type.FIELD
        item.field == result2.field
        item.was == (result2 as Patcher.UpdatedField).was
        item.became == (result2 as Patcher.UpdatedField).became
        item.type == Patcher.Updated.Type.FIELD
    }

    def "Serialize and deserialize Updated"() {
        when:
        def items = [
                new Patcher.UpdatedField('field1', 'was', 'became'),
                new Patcher.UpdatedGroup('group1', [
                        new Patcher.UpdatedRow("row1", Patcher.UpdatedRow.Event.INSERT, [
                                new Patcher.UpdatedField('field2', 'was', 'became'),
                                new Patcher.UpdatedField('field3', 'was', 'became'),
                                new Patcher.UpdatedGroup('group2', [
                                        new Patcher.UpdatedRow('row2', Patcher.UpdatedRow.Event.UPDATE, [
                                                new Patcher.UpdatedField('field4', 'was', 'became'),
                                                new Patcher.UpdatedField('field5', 'was', 'became'),
                                        ])
                                ]),
                        ])
                ]),
        ]
        def json = mapper.writeValueAsString(items)
        println(json)
        def result = mapper.readValue(json, new TypeReference<Collection<Patcher.Updated>>() {})
        then:
        result[0].type == Patcher.Updated.Type.FIELD
        result[1].type == Patcher.Updated.Type.GROUP
        ((result[1] as Patcher.UpdatedGroup).items[0] as Patcher.UpdatedRow).event == Patcher.UpdatedRow.Event.INSERT
        ((result[1] as Patcher.UpdatedGroup).items[0] as Patcher.UpdatedRow).items[0].field == 'field2'
        ((result[1] as Patcher.UpdatedGroup).items[0] as Patcher.UpdatedRow).items[1].field == 'field3'
        ((result[1] as Patcher.UpdatedGroup).items[0] as Patcher.UpdatedRow).items[2].field == 'group2'
    }

    def "Equals for UpdatedField"() {
        expect:
        new Patcher.UpdatedField('field', null, null) == new Patcher.UpdatedField('field', null, null)
        new Patcher.UpdatedField('field', 'was', null) == new Patcher.UpdatedField('field', 'was', null)
        new Patcher.UpdatedField('field', 'was', 'became') == new Patcher.UpdatedField('field', 'was', 'became')
        new Patcher.UpdatedField('field', 'was', 'became') != new Patcher.UpdatedField('field', 'was', 'became1')
    }

    static class DTOInternal {

        Integer id

        DTOInternal(Integer id) {
            this.id = id
        }
    }

    static class DTO {

        String name
        String name2
        DTOInternal role
        String code
        boolean valid

        DTO(String name, String name2, DTOInternal role, String code, boolean valid) {
            this.name = name
            this.name2 = name2
            this.role = role
            this.code = code
            this.valid = valid
        }
    }

    static class EntityInternal {

        Integer id

        EntityInternal(Integer id) {
            this.id = id
        }
    }

    enum EntityCode {

        CODE
    }

    static class Entity {

        String name
        String name2
        EntityInternal role
        EntityCode code
        boolean valid
    }
}