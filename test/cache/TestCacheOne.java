package cache;
import java.io.IOException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;
import bean.User;

/**
 * <pre>
 * ����һ������
 * 1��Ĭ�Ͽ���
 * 2����ִ����clear������ջ���
 * 3����ִ����C/D/U������ջ���
 * 4��ִ����commit������ջ���
 * 5��session��close������ջ���
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

		// ִ��commit������ջ���
		// session.commit();

		user = session.selectOne(statement, 1);
		System.out.println(user.getName());

		session.clearCache();
		user = session.selectOne(statement, 1);
		System.out.println(user.getName());

		session.close();
	}

	/**
	 * ִ����/ɾ/�ģ���ջ���
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

		// ���²���
		String statement2 = "bean.userMapper.updateUser2";
		user.setName("wahaha");
		session.update(statement2, user);

		user = session.selectOne(statement1, 1);
		System.out.println(user.getName());

		session.close();
	}

	/**
	 * �ر�session
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

		// �ر�session
		session.close();

		session = sessionFactory.openSession();
		user = session.selectOne(statement, 1);
		System.out.println(user.getName());

		session.close();
	}
}
