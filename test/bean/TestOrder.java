package bean;
import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;
import bean.Order;

public class TestOrder {
	@Test
	public void getOrder() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		// 默认不自动提交
		SqlSession session = sessionFactory.openSession();

		String statement = "bean.orderMapper.getOrder";
		Order order = session.selectOne(statement, 1);
		System.out.println(order.toString());

		session.close();
	}

	@Test
	public void getAllOrder() throws IOException {
		SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
		SqlSession session = sessionFactory.openSession();
		
		String statement = "bean.orderMapper.getAllOrder";
		List<Order> orders = session.selectList(statement);
		System.out.println(orders.size());

		session.close();
	}
}
