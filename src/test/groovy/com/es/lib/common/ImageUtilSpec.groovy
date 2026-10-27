package com.es.lib.common

import spock.lang.Specification

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class ImageUtilSpec extends Specification {

    private byte[] createTestImage(int width, int height, String format = "png") {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        Graphics2D g = image.createGraphics()
        g.setColor(Color.RED)
        g.fillRect(0, 0, width, height)
        g.dispose()

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ImageIO.write(image, format, baos)
        return baos.toByteArray()
    }

    def "Resize image when width > maxWidth"() {
        given:
        byte[] originalBytes = createTestImage(200, 100, format)

        when:
        byte[] resizedBytes = ImageUtil.resize(new ByteArrayInputStream(originalBytes), contentType, 100)

        then:
        resizedBytes != null
        resizedBytes.length > 0
        BufferedImage resized = ImageIO.read(new ByteArrayInputStream(resizedBytes))
        resized.width == 100

        where:
        contentType  | format
        "image/png"  | "png"
        "image/jpeg" | "jpg"
        "image/gif"  | "gif"
    }

    def "Resize image when width <= maxWidth"() {
        given:
        byte[] originalBytes = createTestImage(50, 50, "png")

        when:
        byte[] resizedBytes = ImageUtil.resize(new ByteArrayInputStream(originalBytes), "image/png", 100)

        then:
        resizedBytes != null
        BufferedImage resized = ImageIO.read(new ByteArrayInputStream(resizedBytes))
        resized.width == 50
        resized.height == 50
    }

    def "Resize throws IOException for unsupported format"() {
        given:
        byte[] originalBytes = createTestImage(50, 50, "png")

        when:
        ImageUtil.resize(new ByteArrayInputStream(originalBytes), "image/bmp", 100)

        then:
        thrown(IOException)
    }

    def "Measure string and write image"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()

        when:
        ImageUtil.writeImage(100, 100, baos) { graphics ->
            graphics.setColor(Color.BLUE)
            graphics.fillRect(0, 0, 100, 100)
            def dim = ImageUtil.measureString(graphics, "Test", 5)
            assert dim.width > 5
            assert dim.height > 5
        }

        then:
        baos.size() > 0
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()))
        image.width == 100
        image.height == 100
    }

    def "Write default empty image"() {
        given:
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream()
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream()

        when:
        ImageUtil.writeDefaultEmptyImage(150, 80, baos1)
        ImageUtil.writeDefaultEmptyImage(200, 100, "Custom Empty", baos2)

        then:
        baos1.size() > 0
        baos2.size() > 0
        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(baos1.toByteArray()))
        img1.width == 150
        img1.height == 80
        BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(baos2.toByteArray()))
        img2.width == 200
        img2.height == 100
    }
}
