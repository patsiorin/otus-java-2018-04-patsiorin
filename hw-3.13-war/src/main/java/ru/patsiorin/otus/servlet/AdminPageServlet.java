package ru.patsiorin.otus.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.patsiorin.otus.orm.model.UserDataSet;
import ru.patsiorin.otus.orm.service.CacheInfo;
import ru.patsiorin.otus.orm.service.DBService;
import ru.patsiorin.otus.orm.service.DBServiceCachedImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminPageServlet extends HttpServlet {
    private static final String ADMIN_HTML = "/admin.html";
    private CacheInfo service;

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");
        service = context.getBean("cachedService", DBServiceCachedImpl.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(new TemplateProcessor().getPage(ADMIN_HTML, service.getCacheParamsAndState()));
    }
}
