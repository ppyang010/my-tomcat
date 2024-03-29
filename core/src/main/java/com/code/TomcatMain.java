package com.code;

import com.code.bio.BioServer;
import com.code.context.Context;

public class TomcatMain {
    public static void main(String[] args) {
        Context webDemo = new Context("webDemo", "/", "D:\\work\\code\\gitcode\\my-tomcat\\core\\webapps\\webDemo");
//        Context webDemo = new Context("webDemo", "/", "/Users/ccy/CcyProjects/my-tomcat/core/webapps/webDemo");
        BioServer bioServer = new BioServer(8000, webDemo);
        // todo 允许添加多个context 需要一个contextMap
        // todo webapps目录和servlet目录可以独立出来
        // todo servlet单例map
        bioServer.start();
    }

}