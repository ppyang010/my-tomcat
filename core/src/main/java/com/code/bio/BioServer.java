package com.code.bio;

import cn.hutool.log.StaticLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BioServer {
    private int port;
    private Executor executor;

    public BioServer(int port) {
        this.port = port;
        this.executor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    AtomicInteger atomic = new AtomicInteger();

                    public Thread newThread(Runnable r) {
                        return new Thread(r, "BioServer-pool-" + this.atomic.getAndIncrement());
                    }
                });
    }

    public void start() {
        StaticLog.info("[{}] BioServer Start !!!", Thread.currentThread().getName());
        try {
            //启动服务 监听端口
            ServerSocket serverSocket = new ServerSocket(port);
            StaticLog.info("[{}] Listening port={}", Thread.currentThread().getName(), port);
            while (true) {
                //阻塞等待连接
                Socket accept = serverSocket.accept();
                executor.execute(new BioSocketProcessor(accept));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
