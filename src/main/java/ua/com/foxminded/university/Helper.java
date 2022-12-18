package ua.com.foxminded.university;

public class Helper {

    public static void main(String[] args) {
        String one = "some world";
        doChanges(one);
        System.out.println(one);
        

    }
    
    static protected void doChanges(String one) {
        one = "additional";
    }

}
