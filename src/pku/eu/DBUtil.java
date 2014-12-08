/**
 * FileName:   DBUtil.java
 * @author:    lyp
 * @version    V1.0 
 * Createdate: 2014年11月10日 下午3:38:38
 * All rights Reserved, Designed By liuyanping
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2014年11月10日       lyp          1.0             1.0
 * Why & What is modified: <修改原因描述>
 */
package cloud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author lyp
 *
 */
public class DBUtil
{

	public Connection getConnection()
	{

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e1)
		{
			e1.printStackTrace();
		}

		String url = "jdbc:mysql://localhost:3306/hadoop";
		String user = "root";
		String password = "2630367";
		Connection conn = null;
		try
		{
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/hadoop?user=root&password=2630367&useUnicode=true&characterEncoding=utf-8");
			conn = DriverManager.getConnection(url, user, password);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return conn;

	}

	public void closeConnection(Connection conn)
	{

		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

}
