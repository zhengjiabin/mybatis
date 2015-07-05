package cache;
import java.io.IOException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;
import bean.User;

/**
 * <pre>
 * 测试一级缓存
 * 1、默认开启
 * 2、当执行了clear，则清空缓存
 * 3、当执行了C/D/U，则清空缓存
 * 4、执行了commit，则清空缓存
 * 5、session被close，则清空缓存
 * </pre>
 * 
 * @author Administrator
 * 
 */
public class TestCacheOne {
	@Test
	public void selectUserByCacheOne() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		String statement = "bean.userMapper.selectUserByCacheOne";

		User user = session.selectOne(statement, 1);
		System.out.println(user.getName());

		// 执行commit，则清空缓存
		// session.commit();

		user = session.selectOne(statement, 1);
		System.out.println(user.getName());

		session.clearCache();
		user = session.selectOne(statement, 1);
		System.out.println(user.getName());

		session.close();
	}

	/**
	 * 执行增/删/改，清空缓存
	 * 
	 * @throws IOException
	 */
	@Test
	public void selectUserByOperator() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		String statement1 = "bean.userMapper.selectUserByCacheOne";

		User user = session.selectOne(statement1, 1);
		System.out.println(user.getName());

		// 更新操作
		String statement2 = "bean.userMapper.updateUser2";
		user.setName("wahaha");
		session.update(statement2, user);

		user = session.selectOne(statement1, 1);
		System.out.println(user.getName());

		session.close();
	}

	/**
	 * 关闭session
	 * 
	 * @throws IOException
	 */
	@Test
	public void selectUserByClose() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		String statement = "bean.userMapper.selectUserByCacheOne";

		User user = session.selectOne(statement, 1);
		System.out.println(user.getName());

		// 关闭session
		session.close();

		session = sessionFactory.openSession();
		user = session.selectOne(statement, 1);
		System.out.println(user.getName());

		session.close();
	}
}
