package org.tooldelta.logging;

import lombok.Getter;

import java.io.OutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomOutputStream extends OutputStream {
    @Getter
    private static final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();

    @Override
    public void write(int b) throws IOException {
        String str = String.valueOf((char) b);
        logQueue.add(str);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        String str = new String(b, off, len);
        logQueue.add(str);
    }

}
