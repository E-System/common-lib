package com.es.lib.common.email

import com.es.lib.common.email.config.EmailAuth
import com.es.lib.common.email.config.EmailServer
import com.es.lib.common.email.config.POP3ServerConfiguration
import com.es.lib.common.email.pop.ReceivedMessage
import com.es.lib.common.email.simple.Message
import com.es.lib.common.email.simple.MessageStreamAttachment
import spock.lang.Specification

import javax.mail.Address
import javax.mail.Session
import javax.mail.internet.InternetAddress
import java.nio.charset.StandardCharsets

class EmailModelsSpec extends Specification {

    def "POP3ServerConfiguration presets and constructors"() {
        expect:
        POP3ServerConfiguration.PRESETS.containsKey("gmail")
        POP3ServerConfiguration.PRESETS.containsKey("mailru")
        POP3ServerConfiguration.PRESETS.containsKey("yandex")
        POP3ServerConfiguration.PRESETS.containsKey("e-system")

        when:
        EmailServer server = new EmailServer("pop.example.com", 995, true)
        EmailAuth auth = new EmailAuth("user", "pass")
        POP3ServerConfiguration config1 = new POP3ServerConfiguration(server, auth)
        POP3ServerConfiguration config2 = new POP3ServerConfiguration(server, auth, ["mail.custom.prop": "5000"], true)

        then:
        config1.server == server
        config1.auth == auth
        config2.isDebug()
        config2.getProperty().getProperty("mail.custom.prop") == "5000"
        config2.getAuthenticator() != null
        config2.toString().contains("pop.example.com")
    }

    def "Email attachments and contents"() {
        given:
        byte[] bytes = "Content".getBytes(StandardCharsets.UTF_8)
        EmailByteArrayContent byteContent = new EmailByteArrayContent(bytes, "text/plain")
        EmailByteArrayContent byteContentNamed = new EmailByteArrayContent(bytes, "text/plain", "file.txt")

        when:
        EmailRootAttachment rootAtt = new EmailRootAttachment(byteContent)
        EmailAttachment multiAtt = new EmailAttachment("cid123", byteContentNamed)

        then:
        byteContent.bytes == bytes
        byteContent.type == "text/plain"
        byteContent.name == null
        byteContentNamed.name == "file.txt"
        byteContent.toString().contains("text/plain")

        rootAtt.isSingle()
        !rootAtt.isMulti()
        !multiAtt.isSingle()
        multiAtt.isMulti()
        multiAtt.cid == "cid123"
        rootAtt.toString().contains("content=")
    }

    def "ReceivedMessage getters and toString"() {
        given:
        Address from = new InternetAddress("sender@example.com")
        Date date = new Date()
        Map<String, String> headers = ["X-Custom": "Val"]
        Map<String, File> attachments = ["att.txt": new File("/tmp/att.txt")]

        when:
        ReceivedMessage msg1 = new ReceivedMessage([from], "Subj1", "Body1", date, headers)
        ReceivedMessage msg2 = new ReceivedMessage([from], "Subj2", "Body2", date, headers, attachments)

        then:
        msg1.from == [from]
        msg1.subject == "Subj1"
        msg1.message == "Body1"
        msg1.sentDate == date
        msg1.headers == headers
        msg1.attachments == null

        msg2.attachments == attachments
        msg2.toString().contains("Subj2")
    }

    def "Simple Message and MessageStreamAttachment"() {
        given:
        MessageStreamAttachment att = new MessageStreamAttachment("doc.pdf", new ByteArrayInputStream("data".bytes), "application/pdf")

        when:
        Message msgWithoutAtt = new Message("Subj", "Text")
        Message msgWithAtt = new Message("Subj", "Text", [att])

        then:
        !msgWithoutAtt.isAttachmentAvailable()
        msgWithAtt.isAttachmentAvailable()
        msgWithAtt.attachments == [att]
        att.name == "doc.pdf"
        att.contentType == "application/pdf"
        att.inputStream != null
    }

    def "SMTPMessageImpl updateMessageID and writeTo"() {
        given:
        Session session = Session.getInstance(new Properties())
        SMTPMessageImpl msg = new SMTPMessageImpl(session)

        when:
        msg.setMessageId("custom-msg-id-123")
        msg.setText("Hello SMTP")
        msg.saveChanges()

        then:
        msg.getHeader("Message-ID")[0] == "<custom-msg-id-123>"

        when:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        msg.writeTo(baos, ["ignored"] as String[])

        then:
        baos.size() > 0
    }
}
