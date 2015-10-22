package Tencent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class HushenMarket {
    public static final String tableName = "marketStockCode";
    Connection connection;
    private Logger logger;

    public HushenMarket(Connection connection) {
        this.connection = connection;
        this.logger = Logger.getLogger(HushenMarket.class);
    }

    public void initDb() throws SQLException {
        logger.info("HushenMarket::initDb: init hushen all market stock id db table entry.");
        PreparedStatement stmt;
        int numberOfTables = 0;
        stmt = connection.prepareStatement("SHOW TABLES");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            if (tableName.equalsIgnoreCase(rs.getString(1))) {
                numberOfTables++;
                logger.info("HushenMarket::initDb: init hushen all market stock id db table already exist.");
            }
        }
        if (numberOfTables == 0) {
            stmt = connection.prepareStatement("CREATE TABLE " + tableName + " (SDB_ID VARCHAR(200))");
            stmt.execute();
            flush();
            logger.info("HushenMarket::initDb: init hushen all market stock id db table create successfully.");
        } else
            deleteAll();
        rs.close();
        logger.info("HushenMarket::initDb: init hushen all market stock id db exit.");
    }

    public void save(Set<String> quotes) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + tableName + " (SDB_ID) VALUES (?)");
        Iterator<String> itr = quotes.iterator();
        while (itr.hasNext()) {
            stmt.setString(1, itr.next());
            stmt.execute();
        }
        flush();
    }

    public void update(Set<String> quotes) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("REPLACE INTO " + tableName + " (SDB_ID) VALUES (?)");
        Iterator<String> itr = quotes.iterator();
        while (itr.hasNext()) {
            String code = itr.next();
            stmt.setString(1, code);
            stmt.execute();
        }
        flush();
    }

    public Set<String> findAll() {
        logger.info("HushenMarket::findAll: search hushen stock id from db entry.");
        int stockCount = 0;
        Set<String> quotes = new HashSet<String>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String stockId = rs.getString(1);
                quotes.add(stockId);
                stockCount++;
            }
            rs.close();
        } catch (SQLException e) {
            logger.warn("HushenMarket::findAll: SQL opereation failure with " + e.toString());
        }

        logger.info("HushenMarket::findAll: total stock id count: " + stockCount);
        logger.info("HushenMarket::findAll: search hushen stock id from db exit!");
        return quotes;
    }

    @SuppressWarnings("resource")
    public Set<String> processOneSheet(String filename){
        logger.info("HushenMarket::processOneSheet: parse the xls sheet entry.");
        Set<String> quotes = new HashSet<String>();
        HSSFWorkbook workbook;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filename));
            workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = worksheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                quotes.add(row.getCell(0).toString().substring(1, 7));
            }
        } catch (FileNotFoundException e) {
            logger.warn("HushenMarket::processOneSheet: parse the xls sheet failure with " + e.toString());
        } catch (IOException e) {
            logger.warn("HushenMarket::processOneSheet: parse the xls sheet failure with " + e.toString());
        } finally {
        }
        return quotes;
    }

    public void deleteAll() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tableName);
        stmt.execute();
        flush();
    }

    public void flush() throws SQLException {
        connection.commit();
    }

    public void importMarket() {
        logger.info("HushenMarket::importMarket: import hushen market stock id to db entry.");
        Set<String> quotes;
        try {
            initDb();
            quotes = processOneSheet("D:\\Personal\\B.xls");
            save(quotes);
        } catch (Exception e) {
            logger.warn("HushenMarket::importMarket: import hushen stock from xls failure with " + e.toString());
        }
        logger.info("HushenMarket::importMarket: import hushen market stock id to db exit.");
    }
}
