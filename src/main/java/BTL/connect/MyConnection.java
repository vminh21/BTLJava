/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.connect;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author vanminh
 */
public class MyConnection {
    private static MyConnection instance = new MyConnection();
    private  MyConnection(){
    }
    String hostName = "localhost";
    String dbName = "cuongthuanstore";
    String userName = "root";
    String password= "";
    String url = "jdbc:mysql://" + hostName + ":3306/" + dbName + "?useUnicode=true&characterEncoding=utf8";
    Connection conn = null;
    public static MyConnection getInstance(){
        return instance;
    }
    public Connection getConnection(){
        if (conn == null) {
            return open();
        }
        return conn;
    }
    private Connection open(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    private void close(){
        try {
            if(conn != null){
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        MyConnection myConnection= MyConnection.getInstance();
        Connection conn = myConnection.getConnection();
        if (conn != null) {
            System.out.println("Kết nối thành công");
        } else {
            System.out.println("Kết nối không thành công");
        }
    }
}
