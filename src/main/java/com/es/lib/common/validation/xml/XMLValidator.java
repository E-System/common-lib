package com.es.lib.common.validation.xml;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class XMLValidator {

    private final Validator validator;

    public XMLValidator(InputStream xsd) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(xsd);
        Schema schema = factory.newSchema(schemaFile);
        validator = schema.newValidator();
    }

    public boolean validate(String xml) {
        return validate(xml.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validate(byte[] xml) {
        try (InputStream is = new ByteArrayInputStream(xml)) {
            return validate(is);
        } catch (IOException e) {
            return false;
        }
    }

    public boolean validate(InputStream xml) {
        try {
            validator.validate(new StreamSource(xml));
            return true;
        } catch (SAXException | IOException e) {
            return false;
        }
    }

    public static XMLValidator create(InputStream xsd) throws SAXException {
        return new XMLValidator(xsd);
    }

    public static XMLValidator create(byte[] xsd) throws SAXException, IOException {
        try (InputStream is = new ByteArrayInputStream(xsd)) {
            return create(is);
        }
    }

    public static XMLValidator create(String xsd) throws SAXException, IOException {
        return create(xsd.getBytes(StandardCharsets.UTF_8));
    }
}
