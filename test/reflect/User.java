package reflect;

public class User {
    private String test;
    
    private boolean isUser;
    
    public User(String test) {
        super();
        this.test = test;
    }
    
    public User() {
        super();
    }
    
    public String getTest() {
        return test;
    }
    
    public void setTest(String test) {
        this.test = test;
    }
    
    public boolean isUser() {
        return isUser;
    }
    
    public void setUser(boolean isUser) {
        this.isUser = isUser;
    }
    
}
