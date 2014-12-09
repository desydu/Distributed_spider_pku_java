package com.rp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
//F:\app\lenovo\product\11.2.0\dbhome_1\jdbc\lib
//าชาýฐü
public class ConnOrcl {

	public static void main(String[] args) {
		
		try {
			// 1.load driver
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2.get connection
			Connection ct = DriverManager.getConnection(
					"jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "m1234");
			// from follow is same with sql server
			Statement sm = ct.createStatement();
			ResultSet rs = sm.executeQuery("select * from emp");

			while (rs.next()) {
				System.out.println("name:" + rs.getString(2));
			}
			
			//close the resource
			rs.close();
			sm.close();
			ct.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
