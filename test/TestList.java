import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import bean.User;

public class TestList {
    
    @Test
    public void testList() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();
        User user5 = new User();
        
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        
        User user = null;
        for (int i = 0; i < users.size(); i++) {
            user = users.get(i);
            System.out.println(user.toString());
            
            users.set(i, user);
            
            user = users.get(i);
            System.out.println(user.toString());
        }
    }
}
