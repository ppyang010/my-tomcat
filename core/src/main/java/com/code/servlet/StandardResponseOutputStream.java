package com.code.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class StandardResponseOutputStream extends ServletOutputStream {

    /**
     * 暂存8kb
     * 用于暂存doService业务逻辑产生的响应体数据
     * 为什么需要暂存响应体  因为整个业务逻辑过程中会有多次写入操作 需要等doGet/doPost执行完后 再返回数据
     */
    private byte[] bytes = new byte[1024 * 8];
    private int pos = 0;

    public byte[] getBytes() {
        return bytes;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public void write(int b) throws IOException {
        bytes[pos] = (byte) b;
        pos++;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}