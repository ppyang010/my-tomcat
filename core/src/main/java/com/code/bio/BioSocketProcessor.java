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
            // 在servlet中处理逻辑并写入响应体
            servlet.service(new StandardHttpServletRequest(request),new StandardHttpServletResponse(response));

//            // 组装完成的响应消息返回给客户端 包括(响应行 响应头 响应体)
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
