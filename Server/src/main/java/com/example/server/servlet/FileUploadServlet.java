package com.example.server.servlet;

import com.example.server.constant.SystemConst;
import com.example.server.dto.FileDTO;
import com.example.server.service.FileService;
import com.example.server.service.impl.FileServiceImpl;
import com.example.server.util.AESUtil;
import com.example.server.util.FileUtil;
import com.example.server.util.RSAUtil;
import org.eclipse.jetty.server.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.UUID;

/**
 * 文件上传
 *
 * @author saysky
 * @date 2020/2/29 5:57 下午
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = "/file/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("upload");

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * 注入FileService
     */
    FileService fileService = new FileServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String contentType = request.getContentType();

        // 新建年月日文件夹
        Date now = new Date(System.currentTimeMillis());
        String dirPath = MULTI_PART_CONFIG.getLocation() + File.separator + sdf.format(now);
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        // 获得随机AES密钥
        String AES_KEY = "";
        try {
            AES_KEY = AESUtil.getSecretKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //存储路径
        if (contentType != null && contentType.startsWith("multipart/")) {
            request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
            //获取上传的文件集合
            Collection<Part> parts = request.getParts();
            //上传单个文件
            if (parts.size() == 1) {
                Part part = request.getPart("file");
                // 原文件名
                String originName = getFileName(part);
                // 文件后缀
                String type = originName.substring(originName.lastIndexOf("."));
                // 新的文件名：UUID
                String uuid = UUID.randomUUID().toString();
                // 存储位置
                String position = dirPath + File.separator + uuid;
                // 将文件存储到本地
                InputStream is = part.getInputStream();
                FileOutputStream fos = new FileOutputStream(position);
                InputStream aesIs = null;
                try {
                    aesIs = AESUtil.encryptFile(AES_KEY, is);
                    byte[] bytes = new byte[1024];
                    int length = 0;
                    while ((length = aesIs.read(bytes)) != -1) {
                        fos.write(bytes, 0, length);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    fos.close();
                    aesIs.close();
                    is.close();
                }

                // 存储信息到数据库
                FileDTO fileDTO = new FileDTO();
                fileDTO.setId(uuid);
                fileDTO.setName(originName);
                fileDTO.setType(type);
                fileDTO.setSize(FileUtil.parseSize(part.getSize()));
                fileDTO.setTime(new Timestamp(System.currentTimeMillis()));
                fileDTO.setLocation(position);
                OutputStream out = response.getOutputStream();
                try {
                    // 对随机AES密钥进行RSA加密
                    fileDTO.setEnvelope(RSAUtil.encryptByPubKey(AES_KEY, SystemConst.PUBLIC_KEY));
                    // 将文件信息写入数据库
                    fileService.insert(fileDTO);
                    out.write(uuid.getBytes());
                    System.out.println("上传成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    out.write("上传失败".getBytes());
                } finally {
                    out.flush();
                    out.close();
                }
            }

        }
    }

    private String getFileName(Part part) {
        String head = part.getHeader("Content-Disposition");
        String fileName = head.substring(head.indexOf("filename=\"") + 10, head.lastIndexOf("\""));
        return fileName;
    }
}
