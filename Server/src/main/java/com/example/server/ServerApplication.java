package com.example.server;

/**
 * @author saysky
 * @date 2020/2/29 1:09 下午
 */

import com.example.server.filter.SystemFilter;
import com.example.server.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


public class ServerApplication {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new FileUploadServlet()), "/file/upload");
        context.addServlet(new ServletHolder(new FileDownloadServlet()), "/file/download");
        context.addServlet(new ServletHolder(new FileListServlet()), "/file/list");
        context.addServlet(new ServletHolder(new FileDetailServlet()), "/file/detail");

        ServletHandler handler = new ServletHandler();
        FilterHolder fh = handler.addFilterWithMapping(SystemFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(fh, "/*", EnumSet.of(DispatcherType.REQUEST));

        server.start();
        server.join();
    }
}