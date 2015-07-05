package bean;
import java.io.IOException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;
import bean.Teacher;

public class TestTeacher {
	@Test
	public void getTeacher() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		String statement = "bean.teacherMapper.getTeacher";
		Teacher teacher = session.selectOne(statement,1);
		System.out.println(teacher.toString());

		session.close();
	}
}
