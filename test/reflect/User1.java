package reflect;

public class User1 extends User{
    private String test0;
    
    private String test1;
    
    private String test2;
    
    private String test3;
    
    private String test4;
    
    public User1(String test0, String test1, String test2, String test3, String test4) {
        super();
        this.test0 = test0;
        this.test1 = test1;
        this.test2 = test2;
        this.test3 = test3;
        this.test4 = test4;
    }
    
    public User1() {
        super();
    }
    
    private String getTest0() {
        return test0;
    }
    
    private void setTest0(String test0) {
        this.test0 = test0;
    }
    
    public String getTest1() {
        return test1;
    }
    
    public void setTest1(String test1) {
        this.test1 = test1;
    }
    
    public String getTest2() {
        return test2;
    }
    
    public void setTest2(String test2) {
        this.test2 = test2;
    }
    
    public String getTest3() {
        return test3;
    }
    
    public void setTest3(String test3) {
        this.test3 = test3;
    }
    
    public String getTest4() {
        return test4;
    }
    
    public void setTest4(String test4) {
        this.test4 = test4;
    }
    
}
