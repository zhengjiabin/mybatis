package mybatis;
import java.io.IOException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;
import bean.User;


public class TestTypeHandler {

	@Test
	public void getUser() throws IOException{
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		String statement = "bean.userMapper.selectUserByCacheOne";
		User user = session.selectOne(statement, 1);
		System.out.println(user.getName());
		user.setName("xxx");

		session.close();
	}
}
