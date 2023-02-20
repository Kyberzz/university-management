package ua.com.foxminded.university;

public class Helper {

    public static void main(String[] args) {
        String prefix = "ROLE_";
        String role = "ROLE_ADMIN";
        int index = prefix.length();
        String receivedRole = role.substring(index);
        System.out.println(receivedRole);
    }

}
