package com.example.server.dao;

import com.example.server.dto.FileDTO;
import com.example.server.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author saysky
 * @date 2020/2/29 10:45 下午
 */

public class FileDao {

    /**
     * 创建表
     *
     * @throws SQLException
     */
    public void createTable() throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "CREATE TABLE file ( " +
                "id VARCHAR(100), " +
                "name VARCHAR(100), " +
                "type VARCHAR(100), " +
                "size VARCHAR(100), " +
                "location VARCHAR(200), " +
                "envelope VARCHAR(200), " +
                "time TIMESTAMP )";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.executeUpdate();
    }

    /**
     * 删除表
     */
    public void dropTable() throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "drop table file";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.executeUpdate();
    }

    /**
     * 添加
     *
     * @param fileDTO
     * @throws SQLException
     * @throws ParseException
     */
    public void insert(FileDTO fileDTO) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "insert into file(id, name, type, location, size, envelope, time) values (?,?,?,?,?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, fileDTO.getId());
        ptmt.setString(2, fileDTO.getName());
        ptmt.setString(3, fileDTO.getType());
        ptmt.setString(4, fileDTO.getLocation());
        ptmt.setString(5, fileDTO.getSize());
        ptmt.setString(6, fileDTO.getEnvelope());
        ptmt.setTimestamp(7, fileDTO.getTime());
        ptmt.executeUpdate();
    }


    /**
     * 查询所有
     *
     * @return
     * @throws SQLException
     */
    public List<FileDTO> findAll() throws SQLException {
        List<FileDTO> list = new ArrayList<FileDTO>();
        Connection conn = DBUtil.getConnection();
        String sql = "select id, name, type, location,size,envelope,time from file";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        while (rs.next()) {
            FileDTO fileDTO = new FileDTO();
            fileDTO.setId(rs.getString("id"));
            fileDTO.setName(rs.getString("name"));
            fileDTO.setType(rs.getString("type"));
            fileDTO.setLocation(rs.getString("location"));
            fileDTO.setSize(rs.getString("size"));
            fileDTO.setEnvelope(rs.getString("envelope"));
            fileDTO.setTime(rs.getTimestamp("time"));
            list.add(fileDTO);
        }
        return list;
    }

    /**
     * 根据ID查询
     *
     * @return
     * @throws SQLException
     */
    public FileDTO findById(String id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "select id,name, type, location,size,envelope,time from file where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, id);
        ResultSet rs = ptmt.executeQuery();
        FileDTO fileDTO = null;
        while (rs.next()) {
            fileDTO = new FileDTO();
            fileDTO.setId(rs.getString("id"));
            fileDTO.setName(rs.getString("name"));
            fileDTO.setType(rs.getString("type"));
            fileDTO.setLocation(rs.getString("location"));
            fileDTO.setSize(rs.getString("size"));
            fileDTO.setEnvelope(rs.getString("envelope"));
            fileDTO.setTime(rs.getTimestamp("time"));
        }
        return fileDTO;
    }

    /**
     * 根据ID查询
     *
     * @return
     * @throws SQLException
     */
    public void deleteById(String id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "delete from file where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, id);
        ptmt.executeUpdate();
    }

}