package com.example.server.filter;

import com.example.server.constant.SystemConst;
import com.example.server.dao.FileDao;
import com.example.server.util.RSAUtil;
import org.eclipse.jetty.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 过滤器
 *
 * @author saysky
 * @date 2020/2/29 2:12 下午
 */
public class SystemFilter implements Filter {

    /**
     * header 中的X-SID属性
     */
    private static final String SID = "X-SID";

    /**
     * header 中的X-Signature属性
     */
    private static final String SIGNATURE = "X-Signature";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 项目启动时检查 file 表是否创建
        try {
            new FileDao().createTable();
        } catch (SQLException e) {
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        servletResponse.setContentType("text/html; charset=UTF-8");
        OutputStream out = response.getOutputStream();

        // 获取 header 中的SID：客户端生成的随机字符串
        String sid = request.getHeader(SID);
        // 获取 header 中的签名：客户端使用私钥对SID加密后的字符串
        String signature = request.getHeader(SIGNATURE);

        // 如果SID或者签名为空，拦截
        if (StringUtil.isBlank(sid) || StringUtil.isBlank(signature)) {
            out.write("SID或签名为空".getBytes());
            response.setStatus(403);
            out.close();
            return;
        }

        // 验证SID和签名是否匹配
        try {
            String decrypt = RSAUtil.decryptByPubKey(signature, SystemConst.PUBLIC_KEY);
            // 如果解密后的值和原值(SID) 不相等，则拦截
            if (!Objects.equals(decrypt, sid)) {
                out.write("SID和签名不匹配".getBytes());
                response.setStatus(403);
                out.close();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("服务器内部错误".getBytes());
            response.setStatus(403);
            out.close();
            return;
        }

        // 通过, 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
