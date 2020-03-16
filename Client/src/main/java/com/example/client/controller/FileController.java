package com.example.client.controller;

import com.alibaba.fastjson.JSON;
import com.example.client.constant.SystemConst;
import com.example.client.dto.FileDTO;
import com.example.client.util.AESUtil;
import com.example.client.util.FileUtil;
import com.example.client.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author saysky
 * @date 2020/3/1 1:12 下午
 */

@Controller
public class FileController {

    /**
     * 声明 restTemplate
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * 注入 restTemplate
     */
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 服务器主机
     */
    public static String SERVER_DOMAIN = "http://localhost:8080";

    /**
     * 构建 header，因为这段代码下面几个方法都需要调用，所以单独提取出来
     *
     * @return
     * @throws Exception
     */
    private HttpHeaders builderHeader() throws Exception {
        // 请求头设置属性
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));
        // X-SID :随机数
        String sid = String.valueOf(new Random().nextInt(10));
        // X-Signature :RSA算法 使用私钥对sid加密
        String signature = RSAUtil.encryptByPriKey(sid, SystemConst.PRIVATE_KEY);
        headers.add("X-SID", sid);
        headers.add("X-Signature", signature);
        return headers;
    }

    /**
     * 文件列表
     *
     * @param model
     * @return
     */
    @GetMapping
    public String index(Model model) throws Exception {

        // 设置header
        HttpHeaders headers = builderHeader();
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(form, headers);
        List<FileDTO> fileDTOList = new ArrayList<>();
        try {
            // 向Server端请求获得列表
            ResponseEntity<String> result = restTemplate.exchange(SERVER_DOMAIN + "/file/list", HttpMethod.GET, httpEntity, String.class);
            if (result.getStatusCode() == HttpStatus.OK) {
                fileDTOList = JSON.parseArray(result.getBody(), FileDTO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用Server接口失败");
        }
        model.addAttribute("fileDTOList", fileDTOList);
        return "index";
    }


    /**
     * 文件下载
     *
     * @param id
     * @param response
     * @throws Exception
     */
    @GetMapping("/file/download")
    public void index(@RequestParam("id") String id, HttpServletResponse response) throws Exception {

        // 设置header
        HttpHeaders headers = builderHeader();
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(form, headers);

        //1. 向Server端请求：根据ID获取文件信息
        FileDTO fileDTO = null;
        try {
            ResponseEntity<String> result = restTemplate.exchange(SERVER_DOMAIN + "/file/detail?id=" + id, HttpMethod.GET, httpEntity, String.class);
            if (result.getStatusCode() == HttpStatus.OK) {
                fileDTO = JSON.parseObject(result.getBody(), FileDTO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用Server接口失败");
        }

        // 2.向Server端请求下载
        if (fileDTO != null) {
            ResponseEntity<Resource> result = restTemplate.exchange(SERVER_DOMAIN + "/file/download?id=" + id, HttpMethod.GET, httpEntity, Resource.class);
            if (result.getStatusCode() == HttpStatus.OK) {
                InputStream in = result.getBody().getInputStream();
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileDTO.getName(), "UTF-8"));
                OutputStream out = response.getOutputStream();

                // 3.解密，输出到浏览器
                String key = RSAUtil.decryptByPriKey(fileDTO.getEnvelope(), SystemConst.PRIVATE_KEY);
                AESUtil.decryptFileAndOutput(key, in, out);
            }
        }
    }

    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping("/file/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {

        // 设置header
        HttpHeaders headers = builderHeader();
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(form, headers);


        // 将 MultipartFile 转成 File
        InputStream ins = multipartFile.getInputStream();
        File file = new File(multipartFile.getOriginalFilename());
        FileUtil.inputStreamToFile(ins, file);
        // 将 file 作为参数装到 form 中
        FileSystemResource resource = new FileSystemResource(file);
        form.add("file", resource);

        try {
            // 向Server端请求上传
            ResponseEntity<String> result = restTemplate.exchange(SERVER_DOMAIN + "/file/upload", HttpMethod.POST, httpEntity, String.class);
            if (result.getStatusCode() == HttpStatus.OK) {
                System.out.println("上传成功，文件ID：" + result.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        } finally {
            // 清除临时文件
            file.delete();
        }
        return "redirect:/";
    }


}
