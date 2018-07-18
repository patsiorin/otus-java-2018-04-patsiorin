package ru.patsiorin.otus.servlet;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.patsiorin.otus.orm.cache.CacheEngineSoftReference;
import ru.patsiorin.otus.orm.model.UserDataSet;
import ru.patsiorin.otus.orm.service.DBServiceCachedImpl;
import ru.patsiorin.otus.orm.service.DBServiceHibernateImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class Main {
    private static final int PORT = 8080;
    private static final String TEMPLATES = "tml";
    private static final String ADMIN_PAGE = "/admin";
    private static final String LOGIN_PAGE = "/login";
    private static final String ERROR_PAGE = "/error";
    public static final String USERS_CONFIG = "users.properties";

    static DBServiceCachedImpl<UserDataSet> cachedService =
            new DBServiceCachedImpl<>(new DBServiceHibernateImpl(), new CacheEngineSoftReference<>(10, 20 ,30, false));

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);

        context.setContextPath("/");
        context.setResourceBase(TEMPLATES);

        server.setHandler(context);

        context.addServlet(new ServletHolder(new DefaultServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
                response.sendRedirect(ADMIN_PAGE);
            }
        }), "/*");

        context.addServlet(AdminPage.class, ADMIN_PAGE);

        context.addServlet(LoginServlet.class, LOGIN_PAGE);
        context.addServlet(new ServletHolder(new DefaultServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
                response.getWriter().write(new TemplateProcessor().getPage("error.html", null));
            }
        }), ERROR_PAGE);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__FORM_AUTH);
        constraint.setRoles(new String[] {"admin"});
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.addConstraintMapping(constraintMapping);

        HashLoginService loginService = new HashLoginService();
        ClassLoader classLoader = Main.class.getClassLoader();
        loginService.setConfig(Objects.requireNonNull(classLoader.getResource(USERS_CONFIG)).getPath());
        securityHandler.setLoginService(loginService);

        FormAuthenticator authenticator = new FormAuthenticator(LOGIN_PAGE, ERROR_PAGE, false);
        securityHandler.setAuthenticator(authenticator);

        context.setSecurityHandler(securityHandler);

        server.start();
        server.join();

        cachedService.shutdown();
    }

    static Map<String, Object> getCacheParamsAndState() {
        return cachedService.getCacheParamsAndState();
    }
}
