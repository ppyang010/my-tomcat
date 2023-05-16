package com.code.servlet;

import com.code.parser.HttpMessageParser;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * 按照servlet规范实现Request和Response
 */
public class StandardHttpServletResponse extends AbstractHttpServletResponse{

    private HttpMessageParser.Response response;

    public StandardHttpServletResponse(HttpMessageParser.Response response) {
        this.response = response;
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        //自定义outPutStream 需要暂存响应体
        return super.getOutputStream();
    }

    public HttpMessageParser.Response getResponse() {
        return response;
    }

    public void setResponse(HttpMessageParser.Response response) {
        this.response = response;
    }




}
