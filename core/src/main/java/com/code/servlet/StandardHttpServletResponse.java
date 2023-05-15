package com.code.servlet;

import com.code.parser.HttpMessageParser;

public class StandardHttpServletResponse extends AbstractHttpServletResponse{

    private HttpMessageParser.Response response;

    public StandardHttpServletResponse(HttpMessageParser.Response response) {
        this.response = response;
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }

    public HttpMessageParser.Response getResponse() {
        return response;
    }

    public void setResponse(HttpMessageParser.Response response) {
        this.response = response;
    }
}
