package org.minecraftshire.auth.utils.logging;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class BufferedLogWriter implements ILogWriter {

    private ByteArrayOutputStream bos;


    public BufferedLogWriter() {
        bos = new ByteArrayOutputStream();
    }

    public byte[] getBytes() {
        return bos.toByteArray();
    }


    @Override
    public Charset getCharset() {
        return Charset.defaultCharset();
    }

    @Override
    public void write(int b) {
        bos.write(b);
    }

    @Override
    public void write(String string) {
        try {
            bos.write(string.getBytes(this.getCharset()));
        } catch (IOException e) {
        }
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
        try {
            bos.flush();
        } catch (IOException e) {
        }
    }

}
