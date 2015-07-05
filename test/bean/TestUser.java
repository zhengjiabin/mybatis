package bean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import po.UserCondition;
import po.UserMapper;
import util.MybatisUtil;
import bean.User;

public class TestUser {

	@Test
	public void addUser() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		// 是否自动提交
		SqlSession session = sessionFactory.openSession(true);

		String statement = "bean.userMapper.addUser";
		User user = new User(-1, "test", 12);
		session.insert(statement, user);

		session.close();
	}

	@Test
	public void deleteUser() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession(true);

		String statement = "bean.userMapper.deleteUser";
		session.delete(statement, 4);

		session.close();
	}

	@Test
	public void updateUser() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		// 默认不自动提交
		SqlSession session = sessionFactory.openSession();

		String statement = "bean.userMapper.updateUser";
		User user = new User(4, "wangwu", 40);
		session.update(statement, user);

		// 手动提交
		session.commit();

		session.close();
	}

	@Test
	public void getAllUsers() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		// 默认不自动提交
		SqlSession session = sessionFactory.openSession();

		// 通过注解映射
		UserMapper mapper = session.getMapper(UserMapper.class);
		List<User> users = mapper.getAll();
		System.out.println(users.size());

		session.close();
	}

	@Test
	public void getUsers() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		String statement = "bean.userMapper.getUsers";
		UserCondition condition = new UserCondition("%zhang%", -1, 35);
		List<User> users = session.selectList(statement, condition);
		System.out.println(users.size());

		session.close();
	}

	@Test
	public void updateUser2() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession(true);

		String statement = "bean.userMapper.updateUser2";
		User user = new User();
		user.setId(6);
		user.setName("ceshi");
		session.update(statement, user);

		session.close();
	}

	@Test
	public void selectUsers() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		List<Integer> tests = new ArrayList<Integer>();
		tests.add(1);
		tests.add(2);
		tests.add(5);
		tests.add(6);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", tests);
		params.put("name", null);
		params.put("age", -1);

		String statement = "bean.userMapper.selectUsers";
		List<User> users = session.selectList(statement, params);
		System.out.println(users.size());

		session.close();
	}

	/**
	 * 集合测试
	 * 
	 * @throws IOException
	 */
	@Test
	public void selectUsers2() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		List<User> tests = new ArrayList<User>();
		tests.add(new User(1, null, -1));
		tests.add(new User(2, null, -1));
		tests.add(new User(5, null, -1));
		tests.add(new User(6, null, -1));

		String statement = "bean.userMapper.selectUsers2";
		List<User> users = session.selectList(statement, tests);
		System.out.println(users.size());

		session.close();
	}

	/**
	 * 数组测试
	 * 
	 * @throws IOException
	 */
	@Test
	public void selectUsers3() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		int[] tests = { 1, 2, 5, 6 };

		String statement = "bean.userMapper.selectUsers3";
		List<User> users = session.selectList(statement, tests);
		System.out.println(users.size());

		session.close();
	}

	/**
	 * map测试
	 * 
	 * @throws IOException
	 */
	@Test
	public void selectUsers4() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		List<Integer> tests = new ArrayList<Integer>();
		tests.add(1);
		tests.add(2);
		tests.add(5);
		tests.add(6);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", tests);

		String statement = "bean.userMapper.selectUsers4";
		List<User> users = session.selectList(statement, params);
		System.out.println(users.size());

		session.close();
	}

	/**
	 * or测试
	 * 
	 * @throws IOException
	 */
	@Test
	public void selectUsers5() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		List<String> tests = new ArrayList<String>();
		tests.add("%zhang%");
		tests.add("%li%");
		tests.add("%wang%");

		String statement = "bean.userMapper.selectUsers5";
		List<User> users = session.selectList(statement, tests);
		System.out.println(users.size());

		session.close();
	}

	@Test
	public void addUsers() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession(true);

		List<User> users = new ArrayList<User>();
		users.add(new User(-1, "ceshi1", 10));
		users.add(new User(-1, "ceshi2", 11));

		String statement = "bean.userMapper.addUsers";
		session.insert(statement, users);

		session.close();
	}
	
	/**
	 * 
	 * 
	 * @throws IOException
	 */
	@Test
	public void selectUsers6() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", "zhang");
		params.put("age", 25);
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("map", params);

		String statement = "bean.userMapper.selectUsers6";
		List<User> users = session.selectList(statement, parameter);
		System.out.println(users.size());

		session.close();
	}
	
	/**
	 * 存储过程查询测试
	 * @throws IOException 
	 */
	@Test
	public void selectCountByProcedure() throws IOException{
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sex", 1);
		params.put("count", -1);

		String statement = "bean.userMapper.selectCountByProcedure";
		session.selectOne(statement, params);
		
		int count = (int) params.get("count");
		System.out.println(count);

		session.close();
	}
}
