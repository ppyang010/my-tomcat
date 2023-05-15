package com.code.bio;

import cn.hutool.log.StaticLog;
import com.code.parser.HttpMessageParser;
import com.code.servlet.StandardHttpServletRequest;
import com.code.servlet.StandardHttpServletResponse;
import com.servlet.DemoServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
        try {
            // 接收请求数据
            HttpMessageParser.Request request = HttpMessageParser.parse2request(socket.getInputStream());
            HttpMessageParser.Response response = HttpMessageParser.buildSuccessResponse(request);
            //todo 匹配servlet doService
            // 模拟将请求数据分发给servlet
            DemoServlet servlet = new DemoServlet();
            // 返回数据方式2:返回数据由servlet返回
            servlet.service(new StandardHttpServletRequest(request),new StandardHttpServletResponse(response));

//            // 返回数据方式1:直接返回数据给请求方
//            OutputStream outputStream = socket.getOutputStream();
//            PrintWriter out = new PrintWriter(outputStream);
//            String responseStr = HttpMessageParser.buildResponse(request, "hello ccy");
//            out.print(responseStr);
//            // 刷新输出流，确保响应消息被发送
//            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }  finally {
            // 关闭 Socket 连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
