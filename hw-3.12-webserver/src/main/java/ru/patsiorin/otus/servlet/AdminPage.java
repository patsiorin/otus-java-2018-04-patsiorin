package ru.patsiorin.otus.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminPage extends HttpServlet {

    public static final String ADMIN_HTML = "/admin.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(new TemplateProcessor().getPage(ADMIN_HTML, Main.getCacheParamsAndState()));
    }
}
