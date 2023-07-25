package com.code.context;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * context表示一个web应用
 */
public class Context {


    private String name;

    /**
     * 请求默认前缀
     */
    private String path;

    /**
     * 资源路径
     */
    private String docBase;
    /**
     * KEY = URI
     * VAL = Servlet
     */
    private Map<String, Servlet> urlPatternMap = new ConcurrentHashMap<>();

    public Context(String name, String path, String docBase) {
        this.name = name;
        this.path = path;
        this.docBase = docBase;
    }

    public void addUriPatternServletMap(String urlPattern, Servlet servlet){
        urlPatternMap. put( urlPattern, servlet);
    }


    public String getName() {
        return name;
    }

    public Context setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Context setPath(String path) {
        this.path = path;
        return this;
    }

    public String getDocBase() {
        return docBase;
    }

    public Context setDocBase(String docBase) {
        this.docBase = docBase;
        return this;
    }
}
