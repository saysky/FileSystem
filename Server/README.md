## 注意

#### 1. 启动 derby
```
java -jar derbyrun.jar  server start
```

其中 derbyrun.jar 在 jdk 安装目录下的 db/lib 目录


#### 2. 创建 file 表
该表在启动启动时会创建，在 SystemFilter 的 init 方法中


#### 3. 启动该项目
运行 ServerApplication，该项目采用 jetty 内置服务器，启动端口在8080


#### 4.RSA算法公钥
在 SystemConst.PUBLIC_KEY