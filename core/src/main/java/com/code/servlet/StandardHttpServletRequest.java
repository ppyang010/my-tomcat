package com.code.servlet;

import com.code.parser.HttpMessageParser;

public class StandardHttpServletRequest extends AbstractHttpServletRequest{

    private HttpMessageParser.Request request;

    public StandardHttpServletRequest(HttpMessageParser.Request request){
        super();
        this.request = request;
    }


    @Override
    public String getRequestURI() {
        return request.getUri();
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getProtocol() {
        return request.getVersion();
    }
}
