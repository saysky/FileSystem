package com.example.server.servlet;

import com.alibaba.fastjson.JSON;
import com.example.server.dto.FileDTO;
import com.example.server.service.FileService;
import com.example.server.service.impl.FileServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * 文件列表
 * @author saysky
 * @date 2020/3/1 1:29 下午
 */
@WebServlet(name = "FileDetailServlet", urlPatterns = "/file/detail")
public class FileDetailServlet extends HttpServlet {


    /**
     * 注入FileService
     */
    FileService fileService = new FileServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if(id == null) {
            return;
        }
        response.setContentType("text/html; charset=UTF-8");
        OutputStream out = response.getOutputStream();
        try {
            FileDTO fileDTO = fileService.findById(id);
            out.write(JSON.toJSONString(fileDTO).getBytes());
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
