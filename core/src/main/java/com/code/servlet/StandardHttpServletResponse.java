package com.code.servlet;

import com.code.parser.HttpMessageParser;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

/**
 * 按照servlet规范实现Request和Response
 */
public class StandardHttpServletResponse extends AbstractHttpServletResponse {

    private HttpMessageParser.Response response;
    private Socket socket;
    private StandardResponseOutputStream outputStream;


    public StandardHttpServletResponse(HttpMessageParser.Response response, Socket socket) {
        this.response = response;
        this.outputStream = new StandardResponseOutputStream();
        this.socket = socket;
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        //自定义outPutStream 需要暂存响应体
        return outputStream;
    }

    public HttpMessageParser.Response getResponse() {
        return response;
    }

    public void setResponse(HttpMessageParser.Response response) {
        this.response = response;
    }


    /**
     * service方法执行完后 进行对外输出
     */
    public void complete() {
        byte[] bytes = outputStream.getBytes();
        //取出有数据的部分
        byte[] bodyBytes = Arrays.copyOfRange(bytes, 0, outputStream.getPos());
        // 组装完成的响应消息返回给客户端 包括(响应行 响应头 响应体)
        String responseStr = HttpMessageParser.completeResponseStr(response, bodyBytes);
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();

            PrintWriter out = new PrintWriter(outputStream);
            out.print(responseStr);
            // 刷新输出流，确保响应消息被发送
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
