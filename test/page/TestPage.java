package page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Page;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import util.MybatisUtil;
import bean.User;

public class TestPage {
    
    @Test
    public void page() throws IOException {
        Page<User> page = new Page<User>(); 
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("age", 10);
        page.setParams(params);
        
        SqlSessionFactory sessionFactory = MybatisUtil.getSessionFatory();
        SqlSession session = sessionFactory.openSession();

        String statement = "bean.userMapper.selectUserByPage";

        List<User> users= session.selectList(statement, page);
        System.out.println(users.size());
        
        session.close();
    }
}
