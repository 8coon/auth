package org.minecraftshire.auth.utils.logging;


import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class FileLogWriter implements ILogWriter {

    private String fileName;
    private FileOutputStream fos;
    private BufferedOutputStream os;
    private Charset charset;


    public FileLogWriter(String fileName, Charset charset) throws FileNotFoundException {
        this.fileName = fileName;
        this.fos = new FileOutputStream(fileName);
        this.os = new BufferedOutputStream(this.fos);
        this.charset = charset;
    }


    public FileLogWriter(String fileName) throws FileNotFoundException {
        this(fileName, Charset.forName("utf-8"));
    }


    public String getFilename() {
        return this.fileName;
    }


    @Override
    public Charset getCharset() {
        return this.charset;
    }


    @Override
    public void write(int b) throws IOException {
        this.os.write(b);
        this.flush();
    }


    @Override
    public void write(String string) {
        if (string == null) {
            return;
        }

        try {
            this.os.write(string.getBytes(this.charset));
            this.flush();
        } catch (IOException e) {
        }
    }

    @Override
    public void write(String[] lines) {
        if (lines == null) {
            return;
        }

        for (String line: lines) {
            this.write(line);
        }
    }

    @Override
    public void write(Iterable<String> lines) {
        if (lines == null) {
            return;
        }

        for (String line: lines) {
            this.write(line);
        }
    }

    @Override
    public void flush() throws IOException {
        this.os.flush();
    }

}
