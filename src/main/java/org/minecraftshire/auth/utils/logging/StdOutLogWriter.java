package org.minecraftshire.auth.utils.logging;


import java.nio.charset.Charset;


public class StdOutLogWriter implements ILogWriter {

    @Override
    public Charset getCharset() {
        return Charset.defaultCharset();
    }

    @Override
    public void write(int b) {
        System.out.write(b);
    }

    @Override
    public void write(String string) {
        System.out.print(string);
    }

    @Override
    public void write(String[] lines) {
        for (String line: lines) {
            this.write(line);
        }
    }

    @Override
    public void write(Iterable<String> lines) {
        for (String line: lines) {
            this.write(line);
        }
    }

    @Override
    public void flush() {
        System.out.flush();
    }

}
