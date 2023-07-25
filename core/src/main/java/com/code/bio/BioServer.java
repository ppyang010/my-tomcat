package com.code.bio;

import cn.hutool.log.StaticLog;
import com.code.classloader.WebAppClassLoader;
import com.code.context.Context;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BioServer {
    private final int port;
    private final Executor executor;

    private final Context context;

    public BioServer(int port, Context context) {
        this.port = port;
        this.executor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    final AtomicInteger atomic = new AtomicInteger();

                    public Thread newThread(Runnable r) {
                        return new Thread(r, "BioServer-pool-" + this.atomic.getAndIncrement());
                    }
                });
        this.context = context;
    }

    public void start() {
        //step1
        deployApps(context);

        //step2
        StaticLog.info("[{}] BioServer Start !!!", Thread.currentThread().getName());
        try {
            //启动服务 监听端口
            ServerSocket serverSocket = new ServerSocket(port);
            StaticLog.info("[{}] Listening port={}", Thread.currentThread().getName(), port);
            while (true) {
                //阻塞等待连接
                Socket accept = serverSocket.accept();
                executor.execute(new BioSocketProcessor(accept, context));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * todo 可以考虑解耦
     *
     * @param context
     */
    private void deployApps(Context context) {
        StaticLog.info("[{}] deployApps start !!!", Thread.currentThread().getName());
        StaticLog.info("[{}] user.dir={}", Thread.currentThread().getName(), System.getProperty("user.dir"));
        File docBase = new File(context.getDocBase());
        //classes目录
        File classesDirectory = new File(docBase, "/WEB-INF/classes");
        // 拿到该目录下的所有class文件
        List<File> allClassFileList = getAllFilePath(classesDirectory);

        List<String> clazzNameList = new ArrayList<>();
        for (File clazz : allClassFileList) {
            String clazzName = clazz.getPath();
            //过滤非class文件
            if (!clazzName.contains(".class")) {
                continue;
            }
            clazzName = clazzName.replace(classesDirectory.getPath() + "\\", "");
            clazzName = clazzName.replace(".class", "");
            clazzName = clazzName.replace("\\", ".");
//            System.out.println(clazzName);

            clazzNameList.add(clazzName);
        }

        try {
            WebAppClassLoader webAppClassLoader = new WebAppClassLoader(new URL[]{classesDirectory.toURL()});
            for (String clazzName : clazzNameList) {
                Class<?> servletClass = webAppClassLoader.loadClass(clazzName);
                System.out.println(servletClass);
                //判断这个类是不是一个servlet了，如果是就解析WebServlet注解得到对应的urlPatterns
                if (HttpServlet.class.isAssignableFrom(servletClass)) {
                    if (servletClass.isAnnotationPresent(WebServlet.class)) {
                        WebServlet webServlet = servletClass.getAnnotation(WebServlet.class);
                        //优先取urlPatterns的值 如果为空再取value的值
                        String[] urlPatterns = webServlet.urlPatterns();
                        if (null == urlPatterns || urlPatterns.length == 0) {
                            urlPatterns = webServlet.value();
                        }

                        if (null != urlPatterns && urlPatterns.length > 0) {
                            List<String> urlPatternList = Arrays.asList(urlPatterns);
                            for (String urlPattern : urlPatternList) {
                                StaticLog.info("[{}] addUriPatternServletMap urlPattern={} class={}", Thread.currentThread().getName(), urlPattern, servletClass);
                                context.addUriPatternServletMap(urlPattern, (Servlet) servletClass.newInstance());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public List<File> getAllFilePath(File srcFile) {
        List<File> result = new ArrayList<File>();
        File[] files = srcFile.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    result.addAll(getAllFilePath(file));
                } else {
                    result.add(file);
                }
            }
        }

        return result;

    }
}
