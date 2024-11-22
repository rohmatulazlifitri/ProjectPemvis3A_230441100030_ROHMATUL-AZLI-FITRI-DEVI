package projekakhir;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLDataException;

public class koneksi {

    private static java.sql.Connection koneksi;
    public static java.sql.Connection getkoneksi(){
        Connection conn = null;
        try{
            String url = "jdbc:mysql://localhost:3306/db_projek";
            String user = "root";
            String pass = "";
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            conn =  DriverManager.getConnection(url,user,pass);
            System.out.println("koneksi berhasil!");
        }catch(Exception e){
            System.err.println("koneksi gagal" + e.getMessage());
        }
        return conn;
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        getkoneksi();
    }
    
}
