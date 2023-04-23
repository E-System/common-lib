package com.eslibs.common.file;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.2018
 */
final class Mime {

    private static final String DEFAULT_TYPE = "application/octet-stream";
    private static final Map<String, String> TYPES = new HashMap<>();

    static {
        try (InputStream inputStream = Mime.class.getResourceAsStream("/com/eslibs/common/mime.xml")) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputStream, new DefaultHandler() {
                boolean inMapping = false;
                boolean inExtension = false;
                boolean inMimeType = false;
                private String extension = null;
                private String mime = null;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if (qName.equals("mime-mapping")) {
                        inMapping = true;
                    }
                    if (qName.equals("extension")) {
                        inExtension = true;
                    }
                    if (qName.equals("mime-type")) {
                        inMimeType = true;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    if (qName.equals("mime-mapping")) {
                        if (extension != null && mime != null) {
                            TYPES.put(extension, mime);
                        }
                        extension = null;
                        mime = null;
                        inMapping = false;
                    }
                    if (qName.equals("extension")) {
                        inExtension = false;
                    }
                    if (qName.equals("mime-type")) {
                        inMimeType = false;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    if (inExtension) {
                        extension = new String(ch, start, length);
                    }
                    if (inMimeType) {
                        mime = new String(ch, start, length);
                    }
                }
            });

        } catch (org.xml.sax.SAXException | javax.xml.parsers.ParserConfigurationException | IOException sxe) {
            throw new UnsupportedOperationException(sxe);
        }
    }

    Mime() {}

    public static String get(String fileName) {
        return TYPES.getOrDefault(IO.extension(fileName), DEFAULT_TYPE);
    }
}
