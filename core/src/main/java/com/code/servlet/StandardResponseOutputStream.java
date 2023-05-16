package com.code.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class StandardResponseOutputStream  extends ServletOutputStream {

    /**
     * 暂存8kb
     */
    byte[] bytes = new byte[1024*8];

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}