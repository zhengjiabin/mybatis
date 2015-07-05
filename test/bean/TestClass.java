package bean;
import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;

public class TestClass {
	@Test
	public void selectClass() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		// 默认不自动提交
		SqlSession session = sessionFactory.openSession();

		String statement = "bean.classMapper.getClass";
		bean.Class c = session.selectOne(statement, 1);
		System.out.println(c.toString());

		session.close();
	}
	
	@Test
	public void getAllClass() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();
		
		String statement = "bean.classMapper.getAllClass";
		List<bean.Class> c = session.selectList(statement);
		System.out.println(c.size());
		
		session.close();
	}
	
	@Test
	public void getClassInfo() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();
		
		String statement = "bean.classMapper.getClassInfo";
		bean.Class c = session.selectOne(statement,1);
		System.out.println(c.toString());
		
		session.close();
	}
	
	@Test
	public void getAllClassInfo() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();
		
		String statement = "bean.classMapper.getAllClassInfo";
		List<bean.Class> classes = session.selectList(statement);
		System.out.println(classes.size());
		
		session.close();
	}
}
