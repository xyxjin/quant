package com.pureblue.quant.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.pureblue.quant.ConnectionPool.ConnectionManager;


public class DropEmptyTable {

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
           removeworkrun t = new removeworkrun(stock);
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
class workrun implements Runnable{
    String stock;
    public workrun(String stock){
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
        try {
            rs = st.executeQuery(sql);
            if(!rs.next())
            {
                System.out.println("no data = " + stock);
                String dropTb = "DROP TABLE " + stock;
                st.execute(dropTb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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