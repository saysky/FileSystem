package com.example.server.dao;


import com.example.server.dto.FileDTO;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class FileDaoTest {

    FileDao fileDao = new FileDao();

    @Test
    public void createTable() throws SQLException {
        fileDao.createTable();
    }

    @Test
    public void dropTable() throws SQLException {
        fileDao.dropTable();
    }

    @Test
    public void insert() throws SQLException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId("1");
        fileDao.insert(fileDTO);
    }

    @Test
    public void findAll() throws SQLException {
        List<FileDTO> fileDTOList = fileDao.findAll();
        System.out.println(fileDTOList);
    }

    @Test
    public void findById() throws SQLException {
        FileDTO fileDTO = fileDao.findById("1");
        System.out.println(fileDTO);
    }

    @Test
    public void deleteById() throws SQLException {
        fileDao.deleteById("1");
    }
}
