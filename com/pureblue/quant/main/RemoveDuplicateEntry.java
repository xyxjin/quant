package com.pureblue.quant.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.pureblue.quant.ConnectionPool.ConnectionManager;


public class RemoveDuplicateEntry {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        ConnectionManager cm = ConnectionManager.getInstance();

        //1.从数据池中获取数据库连接
        Connection conn = cm.getConnection("default");

        //2.获取用于向数据库发送sql语句的statement
       Statement st = null;
       try {
           st = conn.createStatement();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       Set<String> stocks = new HashSet<String>();
       ResultSet rs = null;
       try {
           rs = st.executeQuery("SHOW TABLES");
           while (rs.next()) {
               System.out.println("table name=" + rs.getString(1));
               stocks.add(rs.getString(1));
           }
       } catch (SQLException e1) {
           e1.printStackTrace();
       }
//       stocks.add("sz300175_capital");

       //5.关闭链接，释放资源
       try {
           rs.close();
           st.close();
           cm.closeConnection("default",conn);
       } catch (SQLException e) {
           e.printStackTrace();
       }

       ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
       for (String stock : stocks) {
           workrun t = new workrun(stock);
           pool.execute(t);
       }
       pool.shutdown();
       while(!pool.isTerminated()){
           try {
               Thread.sleep(5000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           System.out.println("complete task count: "+pool.getCompletedTaskCount());
       }
       ConnectionManager.getInstance().destroy();
       System.out.println("done!!!!!!!!!!!!!!!!!!!");
        ConnectionManager.getInstance().destroy();
    }

}
class removeworkrun implements Runnable{
    String stock;
    public removeworkrun(String stock){
        this.stock=stock;
    }
    @Override
    public void run() {
        ConnectionManager cm = ConnectionManager.getInstance();

        //1.从数据池中获取数据库连接
       Connection conn = cm.getConnection("default");

         //2.获取用于向数据库发送sql语句的statement
        Statement st = null;
        try {
            st = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //3.向数据库发sql,并获取代表结果集的resultset
        //4.取出结果集的数据
        ResultSet rs = null;
        String sql = "SELECT * FROM " + stock;
        Map<String, String> dates = new HashMap<String, String>();
        try {
            rs = st.executeQuery(sql);
            while(rs.next()){
                dates.put(rs.getObject("ID").toString(), rs.getObject("DATE_TIME").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<String, List<String>> values = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        Iterator<String> iterator = dates.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = dates.get(key);
            if (dates.containsValue(value)) {
                if (values.containsKey(value)) {
                    list = values.get(value);
                }
                else {
                    list = new ArrayList<String>();
                }
                list.add(key);
                values.put(value, list);
            }
        }

        System.out.println("result-----------------------");
        iterator = values.keySet().iterator();
        while (iterator.hasNext()) {
            String value = iterator.next();
            List<String> result = values.get(value);
            for(int i=1; i<result.size(); i++)
            {
                String delSql = "DELETE FROM " + stock +" WHERE ID=" + result.get(i);
                System.out.println(delSql);
                try {
                    st.execute(delSql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //5.关闭链接，释放资源
        try {
            rs.close();
            st.close();
            cm.closeConnection("default",conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}