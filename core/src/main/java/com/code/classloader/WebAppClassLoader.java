package com.code.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 用AppClassLoader去加载这个类是加载不到的，因为webapps目录并不在classpath中，因此在Tomcat中是通过自定义类加载器来进行加载的。
 */
public class WebAppClassLoader extends URLClassLoader {

    public WebAppClassLoader(URL[] urls) {
        super(urls);
    }
}