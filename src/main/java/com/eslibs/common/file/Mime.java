package com.eslibs.common.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.2018
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Mime {

    private static final String DEFAULT_TYPE = "application/octet-stream";

    public static String of(String value) {
        return TYPES.getOrDefault(IO.extension(value), DEFAULT_TYPE);
    }

    public static String of(Path value) {
        return of(value.toString());
    }

    private static final Map<String, String> TYPES = parse();

    private static Map<String, String> parse() {
        try (InputStream inputStream = Mime.class.getResourceAsStream("/com/eslibs/common/mime.xml")) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            ParseHandler handler = new ParseHandler();
            saxParser.parse(inputStream, handler);
            return handler.getResult();
        } catch (org.xml.sax.SAXException | javax.xml.parsers.ParserConfigurationException | IOException sxe) {
            throw new UnsupportedOperationException(sxe);
        }
    }

    private static class ParseHandler extends DefaultHandler {

        private boolean inMapping = false;
        private boolean inExtension = false;
        private boolean inMimeType = false;
        private String extension = null;
        private String mime = null;
        @Getter
        private Map<String, String> result = new HashMap<>();

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
            if (inMapping && qName.equals("mime-mapping")) {
                if (extension != null && mime != null) {
                    result.put(extension, mime);
                }
                extension = null;
                mime = null;
                inMapping = false;
            }
            if (inExtension && qName.equals("extension")) {
                inExtension = false;
            }
            if (inMimeType && qName.equals("mime-type")) {
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
    }
}
