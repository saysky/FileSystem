package com.example.server.service.impl;

import com.example.server.dao.FileDao;
import com.example.server.dto.FileDTO;
import com.example.server.service.FileService;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author saysky
 * @date 2020/2/29 11:05 下午
 */
public class FileServiceImpl implements FileService {

    /**
     * 注入dao层
     */
    private static FileDao fileDao = new FileDao();

    @Override
    public List<FileDTO> findAll() throws SQLException {
        try {
            return fileDao.findAll();
        } catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
            System.out.println("请确保创建了 file 表，可通过 new FileDao().createTable() 创建");
        }
        return Collections.emptyList();
    }

    @Override
    public FileDTO findById(String id) throws SQLException {
        return fileDao.findById(id);
    }

    @Override
    public void insert(FileDTO fileDTO) throws SQLException {
        fileDao.insert(fileDTO);
    }
}
