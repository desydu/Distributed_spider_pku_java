package cloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MatchRules_File2DB
{

	public static void main(String[] args)
	{

		MatchRules_File2DB mr = new MatchRules_File2DB();
		mr.startPool();
	}

	public void startPool()
	{

		MyRunnable2 tp = new MyRunnable2();
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 6, 1, TimeUnit.DAYS, queue);

		for (int i = 0; i < 5; i++)
		{
			executor.execute(tp);
		}
		executor.shutdown();
	}

}

class MyRunnable2 implements Runnable
{

	//不想写死，可是不写死总出错 %>_<%
	String rulePath = "e:/java/rule.dict";

	String filePath = "e:/java/file";

	String resultTable = "result1";

	String ruleTable = "rule1";

	List<File> filePathsList = new ArrayList<File>();

	int index = 0;

	public MyRunnable2()
	{

		//获得待处理文件
		File f = new File(filePath);
		getFileList(f);
	}

	//获得所有待处理的文件
	private void getFileList(File f)
	{

		File[] filePaths = f.listFiles();
		for (File s : filePaths)
		{
			if (s.isFile())
			{
				filePathsList.add(s);
			}
		}
	}

	//从数据库中获得规则数据
	public static ResultSet getRule(String ruleTable)
	{

		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		String sql = "select * from " + ruleTable;
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			return rs;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	//将比较后的结果（hashmap）写入数据库表中
	public static void add(HashMap<String, Integer> hm, String ruleTable)
	{

		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();

		PreparedStatement pstmt = null;

		Iterator<Entry<String, Integer>> it = hm.entrySet().iterator();
		String str = null;
		int count = 0;

		while (it.hasNext())
		{
			Entry<String, Integer> entry = (Entry<String, Integer>) it.next();
			str = entry.getKey();
			count = entry.getValue();

			String insert = "insert into " + ruleTable + " (str, count) values (?,?)";
			try
			{
				pstmt = conn.prepareStatement(insert);
				pstmt.setString(1, str);
				pstmt.setInt(2, count);
				pstmt.executeUpdate();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run()
	{

		File file = null;
		while (index < filePathsList.size())
		{
			synchronized (this)
			{
				file = filePathsList.get(index);
				if (file == null)
				{
					continue;
				}
				index++;
			}
			try
			{
				Thread.sleep(300);
			}
			catch (InterruptedException e2)
			{
				e2.printStackTrace();
			}

			//方法二、从数据库读取规则数据（这个没做好，还不行）
			BufferedReader br = null;
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			//使用规则比较内容
			try
			{

				//读取文件内容
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath()),
						"UTF-8"));
				System.out.println("当前使用的线程是：" + Thread.currentThread().getName() + ",正在读文件:"
						+ filePathsList.indexOf(file));
				String s = null;

				while ((s = br.readLine()) != null)
				{
					String[] str = s.toString().split("\\s+");
					//从数据库中获取规则数据
					ResultSet rs = getRule("rule1");
					while (rs.next())
					{
						if (str[0].equals(rs.getString(1)))
						{
							hm.put(str[0], Integer.valueOf(str[1]));
						}
					}
				}
				add(hm, resultTable);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}

		}

	}
}
