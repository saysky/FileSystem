package com.example.server.service;

import com.example.server.dto.FileDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * @author saysky
 * @date 2020/2/29 11:04 下午
 */

public interface FileService {

    /**
     * 获得所有的文件
     * @return
     */
    List<FileDTO> findAll() throws SQLException;

    /**
     * 根据ID获取文件
     * @param id
     * @return
     */
    FileDTO findById(String id) throws SQLException;

    /**
     * 添加
     * @param fileDTO
     */
    void insert(FileDTO fileDTO) throws SQLException;
}
