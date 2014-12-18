package cloud;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class File2DB
{

	//将文件内容插入到数据库
	public void convert(String filePath, String tableName)
	{

		BufferedReader br = null;
		String text = null;
		//清空表
		delete(tableName);
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
			while ((text = br.readLine()) != null)
			{
				System.out.println(text);
				insert(text, tableName);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//写数据库
	static void insert(String data, String tableName)
	{

		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		String insert = "INSERT INTO " + tableName + " (date) VALUES(?)";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, data);
			pstmt.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//更新数据库
	static void update(String data, String tableName)
	{

		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		String insert = "update " + tableName + "where data='" + data "'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, data);
			pstmt.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//删除表内容
	static void delete(String tableName)
	{

		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		String delete = "DELETE FROM " + tableName;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(delete);
			pstmt.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
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
	
	//更新数据库
	static void update(String originData, String changeData String tableName)
	{

		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		String insert = "update " + tableName + "SET data='" + changeData + "' where data='" + originData + "'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, data);
			pstmt.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//	public static void main(String[] args)
	//	{
	//
	//		String filePath = "E:/java/rule.dict";
	//		File2DB fdb = new File2DB();
	//		fdb.convert(filePath, "rule1");
	//
	//	}

}
