package org.minecraftshire.auth.utils.logging;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


public class SystemRedirectStream extends OutputStream {

    private PrintStream combined;
    private PrintStream out;
    private PrintStream err;
    private Logger log;
    private boolean verbose = true;


    public SystemRedirectStream(Logger log, PrintStream out, PrintStream err) {
        this.log = log;
        this.out = out;
        this.err = err;

        combined = new PrintStream(this);

        System.setOut(combined);
        System.setErr(combined);
    }


    public PrintStream getCombined() {
        return this.combined;
    }

    public PrintStream getOut() {
        return this.out;
    }

    public PrintStream getErr() {
        return this.err;
    }

    public Logger getLogger() {
        return this.log;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(boolean value) {
        this.verbose = value;
    }


    @Override
    public void write(int b) throws IOException {
        log.writeByte(b);

        if (this.isVerbose()) {
            err.write(b);
        }
    }

    @Override
    public void flush() throws IOException {
        log.flush();

        if (this.isVerbose()) {
            err.flush();
        }
    }

}
