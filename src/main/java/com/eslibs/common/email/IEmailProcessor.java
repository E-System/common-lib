package com.eslibs.common.email;

import jakarta.mail.MessagingException;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface IEmailProcessor {

    default void send(Message message) throws IOException, MessagingException {
        send(message, null);
    }

    void send(Message message, Logger log) throws IOException, MessagingException;

    default Collection<Message> fetch(Path pathToSave) throws IOException, MessagingException {
        return fetch(pathToSave, false);
    }

    default Collection<Message> fetch(Path pathToSave, Logger log) throws IOException, MessagingException {
        return fetch(pathToSave, false, log);
    }

    default Collection<Message> fetch(Path pathToSave, boolean delete) throws IOException, MessagingException {
        return fetch(pathToSave, delete, null);
    }

    Collection<Message> fetch(Path pathToSave, boolean delete, Logger log) throws IOException, MessagingException;
}
