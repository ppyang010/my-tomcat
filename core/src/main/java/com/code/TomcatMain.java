package com.code;

import com.code.bio.BioServer;

public class TomcatMain {
    public static void main(String[] args) {
        BioServer bioServer = new BioServer(8000);
        bioServer.start();
    }

}