/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SourcePackages;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.TableModel;
/**
 *
 * @author Sohail
 */
public class JDBC {
    
    Connection con=null;
    Statement st=null;
    ResultSet rs=null; 


    
    public JDBC()
     {
     try
      {

        con=DriverManager.getConnection("jdbc:postgresql://db.drrlypuppsbjgkwhnjiu.supabase.co:5432/postgres","postgres","Micro2022@teambeta");
        st=con.createStatement();
      }
    catch(Exception ex)
      {
       System.out.println(ex);
    
      }    
     }
 private static class statement {

        public statement() {
        }
    }
    
}
