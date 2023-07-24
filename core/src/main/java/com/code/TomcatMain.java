package com.code;

import com.code.bio.BioServer;
import com.code.context.Context;

public class TomcatMain {
    public static void main(String[] args) {
        Context webDemo = new Context("webDemo", "/", "D:\\work\\code\\gitcode\\my-tomcat\\core\\webapps\\webDemo");
        BioServer bioServer = new BioServer(8000, webDemo);
        bioServer.start();
    }

}