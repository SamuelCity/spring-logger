package com.gocity.logger.test;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class MemoryLogger extends ListAppender<ILoggingEvent> {

    public static MemoryLogger create(String name) {
        final var logger = LoggerFactory.getLogger(name);
        final var appender = new MemoryLogger();
        final var context = LoggerFactory.getILoggerFactory();

        if (logger instanceof Logger l) {
            l.addAppender(appender);
        }

        if (context instanceof LoggerContext c) {
            appender.setContext(c);
        }

        appender.start();

        return appender;
    }

    public List<ILoggingEvent> getEvents() {
        return Collections.unmodifiableList(this.list);
    }

    public List<String> getFormattedMessages() {
        return this.getEvents()
            .stream()
            .map(ILoggingEvent::getFormattedMessage)
            .toList();
    }
}
