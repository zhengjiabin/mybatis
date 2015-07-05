package cache;
import java.io.IOException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;
import bean.User;

/**
 * <pre>
 * ��������
 * 1��Ĭ�ϲ�����
 * 2����������ʵ������ʵ�����л�
 * </pre>
 * 
 * @author Administrator
 * 
 */
public class TestCacheTwo {

	@Test
	public void selectUserByCacheTwo() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session1 = sessionFactory.openSession();
		SqlSession session2 = sessionFactory.openSession();

		String statement = "bean.userMapper.selectUserByCacheTwo";

		User user = session1.selectOne(statement, 1);
		System.out.println(user);
		// ���ύ���߲��رգ��򲻻���
		// session1.commit();
		session1.close();

		user = session2.selectOne(statement, 1);
		System.out.println(user);

		session2.close();
	}
}
