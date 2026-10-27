package com.es.lib.common.validation.xml

import spock.lang.Specification

class XMLValidatorSpec extends Specification {

    static String XSD = '''<?xml version="1.0" encoding="utf-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <xs:element name="note">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="to" type="xs:string"/>
                <xs:element name="from" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
</xs:schema>'''

    def "Validate valid and invalid XML"() {
        given:
        XMLValidator validator = XMLValidator.create(XSD)
        XMLValidator validatorFromBytes = XMLValidator.create(XSD.bytes)
        XMLValidator validatorFromStream = XMLValidator.create(new ByteArrayInputStream(XSD.bytes))

        String validXml = '<note><to>User</to><from>Admin</from></note>'
        String invalidXml = '<note><to>User</to><unknown>Element</unknown></note>'

        expect:
        validator.validate(validXml)
        validator.validate(validXml.bytes)
        validator.validate(new ByteArrayInputStream(validXml.bytes))
        validatorFromBytes.validate(validXml)
        validatorFromStream.validate(validXml)

        !validator.validate(invalidXml)
        !validator.validate("malformed xml")
    }
}
