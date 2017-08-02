package org.minecraftshire.auth.utils.logging;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    private ILogWriter writer;
    private DateTimeFormatter formatter;

    private static boolean loggerChanged = false;
    protected static final String DELIMITER = " ";

    private static Logger root = new Logger(new BufferedLogWriter());


    public static Logger getLogger() {
        return Logger.root;
    }

    public static void setLogger(Logger logger) {
        if (!Logger.loggerChanged) {
            byte[] bytes = ((BufferedLogWriter) Logger.root.getWriter()).getBytes();

            try {
                for (byte b: bytes) {
                    logger.getWriter().write(b);
                }

                logger.getWriter().flush();
            } catch (IOException e) {
            }

            Logger.loggerChanged = true;
        }

        Logger.root = logger;
    }


    protected ILogWriter getWriter() {
        return this.writer;
    }

    protected DateTimeFormatter getFormatter() {
        return this.formatter;
    }


    protected String formatBlock(String msg) {
        StringBuilder sb = new StringBuilder();

        sb.append('[');
        sb.append(msg);
        sb.append(']');

        return sb.toString();
    }


    protected String formatDate(LocalDateTime dateTime) {
        return this.formatBlock(dateTime.format(this.getFormatter()));
    }

    protected String formatDate() {
        return this.formatDate(LocalDateTime.now());
    }


    protected String formatThrowable(Throwable throwable) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);

        throwable.printStackTrace(ps);
        return new String(os.toByteArray(), this.getWriter().getCharset());
    }


    public Logger(ILogWriter writer) {
        this(writer, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
    }

    public Logger(ILogWriter writer, DateTimeFormatter formatter) {
        this.writer = writer;
        this.formatter = formatter;
    }


    public void write(String... line) {
        StringBuilder sb = new StringBuilder();

        for (String part: line) {
            sb.append(part);
        }

        this.getWriter().write(sb.toString());
    }


    public void writeln(String... line) {
        List<String> parts = new ArrayList<>(line.length);

        for (String part: line) {
            parts.add(part);
        }

        parts.add(System.lineSeparator());
        this.write(parts.toArray(new String[] {}));
    }


    public void log(String level, Object... msg) {
        StringBuilder sb = new StringBuilder();

        for (Object part: msg) {
            sb.append(part.toString());
        }

        this.writeln(this.formatDate(), Logger.DELIMITER, this.formatBlock(level), Logger.DELIMITER, sb.toString());
    }

    public void log(String level, Throwable throwable) {
        this.log(level, this.formatThrowable(throwable));
    }


    public void info(String msg) {
        this.log(LogLevel.INFO, msg);
    }

    public void info(Object... line) {
        this.log(LogLevel.INFO, line);
    }

    public void info(Throwable throwable) {
        this.log(LogLevel.INFO, throwable);
    }


    public void warning(String msg) {
        this.log(LogLevel.WARNING, msg);
    }

    public void warning(Object... line) {
        this.log(LogLevel.WARNING, line);
    }

    public void warning(Throwable throwable) {
        this.log(LogLevel.WARNING, throwable);
    }


    public void error(String msg) {
        this.log(LogLevel.ERROR, msg);
    }

    public void error(Object... line) {
        this.log(LogLevel.ERROR, line);
    }

    public void error(Throwable throwable) {
        this.log(LogLevel.ERROR, throwable);
    }


    public void severe(String msg) {
        this.log(LogLevel.SEVERE, msg);
    }

    public void severe(Object... line) {
        this.log(LogLevel.SEVERE, line);
    }

    public void severe(Throwable throwable) {
        this.log(LogLevel.SEVERE, throwable);
    }


    public void debug(String msg) {
        this.log(LogLevel.DEBUG, msg);
    }

    public void debug(Object... line) {
        this.log(LogLevel.DEBUG, line);
    }

    public void debug(Throwable throwable) {
        this.log(LogLevel.DEBUG, throwable);
    }

}
