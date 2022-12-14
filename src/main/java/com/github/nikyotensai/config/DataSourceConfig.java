package com.github.nikyotensai.config;

import com.baomidou.mybatisplus.annotation.DbType;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库配置
 */
public class DataSourceConfig {

    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 驱动连接的URL
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;

    /**
     * 判断数据库类型
     *
     * @return 类型枚举值
     */
    public DbType getDbType() {
        return DbType.MYSQL;
    }

    /**
     * 创建数据库连接对象
     *
     * @return Connection
     */
    public Connection getConn() throws Exception {
        Class.forName(driverName);
        return DriverManager.getConnection(url, username, password);
    }

}
