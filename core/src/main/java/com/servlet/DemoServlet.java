package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/demo", "/demo/index"})
public class DemoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getMethod());
        // doing
        //写入getOutputStream暂存响应体
        //为什么需要暂存响应体  因为整个业务逻辑过程中会有多次写入操作 需要等doGet/doPost执行完后 再返回数据
        resp.getOutputStream().write("你好呀".getBytes("utf-8"));
        resp.getOutputStream().write("我是ccy\n".getBytes("utf-8"));
    }
}
