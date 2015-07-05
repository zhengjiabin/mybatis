package cache;
import java.io.IOException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;
import bean.User;

/**
 * <pre>
 * 二级缓存
 * 1、默认不开启
 * 2、二级缓存实体类需实现序列化
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
		// 不提交或者不关闭，则不缓存
		// session1.commit();
		session1.close();

		user = session2.selectOne(statement, 1);
		System.out.println(user);

		session2.close();
	}
}
