package org.minecraftshire.auth.utils.logging;


import java.io.IOException;
import java.nio.charset.Charset;

public class NullWriter implements ILogWriter {

    @Override
    public Charset getCharset() {
        return Charset.defaultCharset();
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void write(int b) throws IOException {
    }

    @Override
    public void write(String string) {
    }

    @Override
    public void write(String[] lines) {

    }

    @Override
    public void write(Iterable<String> lines) {
    }

}
