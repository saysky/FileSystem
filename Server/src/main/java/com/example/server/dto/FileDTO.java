package com.example.server.dto;

import java.sql.Timestamp;

/**
 * 文件对象，用于存储数据库
 * @author saysky
 * @date 2020/2/29 8:06 下午
 */
public class FileDTO {

    /**
     * 主键：UUID
     */
    private String id;

    /**
     * 原始文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 存储位置
     */
    private String location;

    /**
     * 文件大小：单位字节
     */
    private String size;

    /**
     * 数字信封: 即AES的随机密钥，然后进行RSA加密后的结果
     */
    private String envelope;

    /**
     * 创建时间
     */
    private Timestamp time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getEnvelope() {
        return envelope;
    }

    public void setEnvelope(String envelope) {
        this.envelope = envelope;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", size=" + size +
                ", envelope='" + envelope + '\'' +
                ", time=" + time +
                '}';
    }
}
