package org.minecraftshire.auth.utils.logging;


import java.io.IOException;
import java.nio.charset.Charset;

public interface ILogWriter {

    Charset getCharset();

    void flush() throws IOException;
    void write(int b) throws IOException;

    void write(String string);
    void write(String[] lines);
    void write(Iterable<String> lines);

}
