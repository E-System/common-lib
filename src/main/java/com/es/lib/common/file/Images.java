/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common.file;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Slf4j
public final class Images {

    private Images() { }

    private static RenderedImage resize(Image originalImage, int scaledWidth, int scaledHeight) {
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaledBI.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setComposite(AlphaComposite.Src);
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public static byte[] resize(InputStream inputStream, final String contentType, final int maxWidth) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            resize(inputStream, contentType, maxWidth, output);
            byte[] bytes = output.toByteArray();
            log.trace("Result file size after resize: {}", bytes.length);
            return bytes;
        }
    }


    public static void resize(InputStream inputStream, final String contentType, final int maxWidth, OutputStream outputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        log.debug("Input image size: {} - {}", width, height);
        boolean success;
        if (width > maxWidth) {
            int saveHeight = maxWidth * Math.max(width, height) / Math.min(width, height);
            log.debug("Output image size: {} - {}", maxWidth, saveHeight);
            success = ImageIO.write(
                resize(bufferedImage, maxWidth, saveHeight),
                getExtension(contentType),
                outputStream
            );
        } else {
            success = ImageIO.write(
                bufferedImage,
                getExtension(contentType),
                outputStream
            );
        }
        if (!success) {
            throw new IOException("File loading error");
        }
        outputStream.flush();
    }

    private static String getExtension(final String contentType) throws IOException {
        if (contentType.contains("jpg") || contentType.contains("jpeg")) {
            return "jpg";
        } else if (contentType.contains("png")) {
            return "png";
        } else if (contentType.contains("gif")) {
            return "gif";
        }
        throw new IOException("Accepted file format is gif, png, jpg");
    }

    public static Dimension measureString(Graphics graphics, String text, int padding) {
        FontMetrics metrics = graphics.getFontMetrics();
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(text);
        return new Dimension(adv + padding, hgt + padding);
    }

    public static void write(int width, int height, OutputStream outputStream, Consumer<Graphics> drawer) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        drawer.accept(graphics);
        ImageIO.write(bufferedImage, "png", outputStream);
    }

    public static void write(int width, int height, OutputStream outputStream, String message) throws IOException {
        write(width, height, outputStream, graphics -> {
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.black);
            Dimension dimension = measureString(graphics, message, 0);
            graphics.drawString(message, Math.round((width - dimension.width) * 0.5f), Math.round((height + dimension.height) * 0.5f));
            graphics.dispose();
        });
    }
}
