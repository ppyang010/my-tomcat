package com.code.bio;

import cn.hutool.log.StaticLog;

import java.net.Socket;

public class BioSocketProcessor implements Runnable {

    private Socket socket;

    public BioSocketProcessor(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        processorSocket(socket);
    }

    private void processorSocket(Socket socket) {
        StaticLog.info("[{}] processorSocket", Thread.currentThread().getName());
    }
}