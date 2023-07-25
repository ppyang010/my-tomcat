package com.code.bio;

import cn.hutool.log.StaticLog;
import com.code.context.Context;
import com.code.context.DefaultServlet;
import com.code.parser.HttpMessageParser;
import com.code.servlet.StandardHttpServletRequest;
import com.code.servlet.StandardHttpServletResponse;
import com.servlet.DemoServlet;

import javax.servlet.Servlet;
import java.io.IOException;
import java.net.Socket;

public class BioSocketProcessor implements Runnable {

    private Socket socket;

    private Context context;

    public BioSocketProcessor(Socket socket, Context context) {
        this.socket = socket;
        this.context = context;
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

            // 在servlet中可以处理业务逻辑,在响应体(暂存)中写入业务数据,写响应头等
            StandardHttpServletRequest standardHttpServletRequest = new StandardHttpServletRequest(request);
            StandardHttpServletResponse standardHttpServletResponse = new StandardHttpServletResponse(response, socket);

            //            DemoServlet servlet = new DemoServlet();

            String requestURI = standardHttpServletRequest.getRequestURI();
            StaticLog.info("[{}] requestURI={}", Thread.currentThread().getName(), requestURI);

            //todo 多context逻辑 contextMap 还没有实现
            // todo 匹配逻辑需要抽象
//            requestURI = requestURI.substring(1);
//            String[] parts = requestURI.split("/");
//            String appName = parts[0];
//            Context context = tomcat.getContextMap().get(appName);
            if (context != null) {
                Servlet servlet = context.getByUrlPattern(requestURI);
                if (servlet != null) {
                    servlet.service(standardHttpServletRequest, standardHttpServletResponse);

                } else {
                    //如果没有找到使用默认的servlet
                    Servlet defaultServlet = new DefaultServlet();
                    defaultServlet.service(standardHttpServletRequest, standardHttpServletResponse);
                }
            }


            //处理逻辑结束后 返回响应消息
            standardHttpServletResponse.complete();

//            // 组装完成的响应消息返回给客户端 包括(响应行 响应头 响应体)
//            OutputStream outputStream = socket.getOutputStream();
//            PrintWriter out = new PrintWriter(outputStream);
//            String responseStr = HttpMessageParser.buildResponse(request, "hello ccy");
//            out.print(responseStr);
//            // 刷新输出流，确保响应消息被发送
//            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭 Socket 连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
