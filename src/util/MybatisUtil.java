package util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {

	public static SqlSessionFactory getSessionFatory() throws IOException {
		String resource = "mybatis.xml";
		
//		Reader reader = Resources.getResourceAsReader(resource);
		InputStream reader = MybatisUtil.class.getClassLoader().getResourceAsStream(resource);

		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		return sessionFactory;
	}
}
