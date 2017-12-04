package com.pureblue.quant.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
    /**
     * 获取一个数据库连接，如果等待超过超时时间，将返回null
     * @return 数据库连接对象
     */
    public Connection getConnection();

    /**
     * 获得当前线程的连接库连接
     * @return 数据库连接对象
     */
    public Connection getCurrentConnecton();

    /**
     * 释放当前线程数据库连接
     * @param conn 数据库连接对象
     * @throws SQLException
     */
    public void releaseConn(Connection conn) throws SQLException;

    /**
     * 销毁清空当前连接池
     */
    public void destroy();

    /**
     * 连接池可用状态
     * @return 连接池是否可用
     */
    public boolean isActive();

    /**
     * 定时器，检查连接池
     */
    public void checkPool();

    /**
     * 获取线程池活动连接数
     * @return 线程池活动连接数
     */
    public int getActiveNum();

    /**
     * 获取线程池空闲连接数
     * @return 线程池空闲连接数
     */
    public int getFreeNum();
}