package com.pureblue.quant.ConnectionPool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;


public class ConnectionManager {
    private final Logger log = Logger.getLogger(ConnectionPool.class);

    private static ConnectionManager dbm = null;

    /** 
     * 加载的驱动器名称集合 
     */  
    private Set<String> drivers = new HashSet<String>(); 

    /**
     * 数据库连接池字典
     * 为每个节点创建一个连接池（可配置多个节点）
     */
    private ConcurrentHashMap<String, IConnectionPool> pools = new ConcurrentHashMap<String, IConnectionPool>();



    private ConnectionManager() {
        createPools();
    }


    /**
     * 装载JDBC驱动程序，并创建连接池
     */
    private void createPools() {
        String str_nodenames = PropertiesManager.getInstance().getProperty("nodename");
        //基本点1、可配置并管理多个连接节点的连接池
        for (String str_nodename : str_nodenames.split(",")) {
            DBPropertyBean dbProperty = new DBPropertyBean();
            dbProperty.setNodeName(str_nodename);

            //验证url配置正确性
            String url = PropertiesManager.getInstance().getProperty(str_nodename + ".url");
            if (url == null) {
                log.error(str_nodename+"节点的连接字符串为空，请检查配置文件");
                continue;
            }
            dbProperty.setUrl(url);

            //验证driver配置正确性
            String driver = PropertiesManager.getInstance().getProperty(str_nodename + ".driver");
            if (driver == null) {
                log.error(str_nodename+"节点的driver驱动为空，请检查配置文件");
                continue;
            }
            dbProperty.setDriverName(driver);


            //验证user配置正确性
            String user = PropertiesManager.getInstance().getProperty(str_nodename + ".user");
            if (user == null) {
                log.error(str_nodename+"节点的用户名设置为空，请检查配置文件");
                continue;
            }
            dbProperty.setUsername(user);


            //验证password配置正确性
            String password = PropertiesManager.getInstance().getProperty(str_nodename + ".password");
            if (password == null) {
                log.error(str_nodename+"节点的密码设置为空，请检查配置文件");
                continue;
            }
            dbProperty.setPassword(password);

            //验证最小连接数配置正确性
            String str_minconnections=PropertiesManager.getInstance().getProperty(str_nodename + ".minconnections");
            int minConn;
            try {
                minConn = Integer.parseInt(str_minconnections);
            } catch (NumberFormatException e) {
                log.error(str_nodename + "节点最小连接数设置错误，默认设为5");
                minConn=5;
            }
            dbProperty.setMinConnections(minConn);

            //验证初始连接数配置正确性
            String str_initconnections=PropertiesManager.getInstance().getProperty(str_nodename + ".initconnections");
            int initConn;
            try {
                initConn = Integer.parseInt(str_initconnections);
            } catch (NumberFormatException e) {
                log.error(str_nodename + "节点初始连接数设置错误，默认设为5");
                initConn=5;
            }
            dbProperty.setInitConnections(initConn);

            //验证最大连接数配置正确性
            String str_maxconnections=PropertiesManager.getInstance().getProperty(str_nodename + ".maxconnections");
            int maxConn;
            try {
                maxConn = Integer.parseInt(str_maxconnections);
            } catch (NumberFormatException e) {
                log.error(str_nodename + "节点最大连接数设置错误，默认设为20");
                maxConn=20;
            }
            dbProperty.setMaxConnections(maxConn);

            //验证conninterval配置正确性
            String str_conninterval=PropertiesManager.getInstance().getProperty(str_nodename + ".conninterval");
            int conninterval;
            try {
                conninterval = Integer.parseInt(str_conninterval);
            } catch (NumberFormatException e) {
                log.error(str_nodename + "节点重新连接间隔时间设置错误，默认设为500ms");
                conninterval = 500;
            }
            dbProperty.setConninterval(conninterval);

            //验证timeout配置正确性
            String str_timeout=PropertiesManager.getInstance().getProperty(str_nodename + ".timeout");
            int timeout;
            try {
                timeout = Integer.parseInt(str_timeout);
            } catch (NumberFormatException e) {
                log.error(str_nodename + "节点连接超时时间设置错误，默认设为2000ms");
                timeout = 2000;
            }
            dbProperty.setTimeout(timeout);

            //创建驱动
            if(!drivers.contains(dbProperty.getDriverName())){
                try {
                    Class.forName(dbProperty.getDriverName());
                    log.info("加载JDBC驱动"+dbProperty.getDriverName()+"成功");
                    drivers.add(dbProperty.getDriverName());
                } catch (ClassNotFoundException e) {
                    log.error("未找到JDBC驱动" + dbProperty.getDriverName() + "，请引入相关包");
                    e.printStackTrace();
                }
            }

            //创建连接池。这里采用同步方法实现的连接池类ConnectionPool。
            //(如果后面我们还有别的实现方式，只需要更改这里就行了。)
            IConnectionPool cp = ConnectionPool.CreateConnectionPool(dbProperty);
            if (cp != null) {
                pools.put(str_nodename, cp);
//                cp.checkPool();
                log.info("创建" + str_nodename + "数据库连接池成功");
            } else {
                log.info("创建" + str_nodename + "数据库连接池失败");
            }
        }

    }

    /**
     * 获得单例
     * 
     * @return DBConnectionManager单例
     */
    public synchronized static ConnectionManager getInstance() {
        if (dbm == null) {
            dbm = new ConnectionManager();
        }
        return dbm;
    }

    /**
     * 从指定连接池中获取可用连接
     * 
     * @param poolName要获取连接的连接池名称
     * @return连接池中的一个可用连接或null
     */
    public Connection getConnection(String poolName) {
        IConnectionPool pool =  pools.get(poolName);
        return pool.getConnection();
    }


    /**
     * 回收指定连接池的连接
     * 
     * @param poolName连接池名称
     * @param conn要回收的连接
     */
    public void closeConnection(String poolName, Connection conn) throws SQLException {
        IConnectionPool pool = pools.get(poolName);
        if (pool != null) {
            try {
                pool.releaseConn(conn);
            } catch (SQLException e) {
                log.error("回收"+poolName+"池中的连接失败。");
                throw new SQLException(e);
            }
        }else{
            log.error("找不到"+poolName+"连接池，无法回收");
        }
    }

    /**
     * 关闭所有连接，撤销驱动器的注册
     */
    public void destroy() {
        for (Map.Entry<String, IConnectionPool> poolEntry : pools.entrySet()) {
            IConnectionPool pool = poolEntry.getValue();
            pool.destroy();
        }
        log.info("已经关闭所有连接");
    }
}