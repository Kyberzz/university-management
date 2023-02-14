package ua.com.foxminded.university;

public class Helper {

    public static void main(String[] args) {
        String line = "1234 > 678";
        String[] roles = line.trim().split("\\s+>\\s+");
        int firstWord = roles[0].length();
        System.out.println(firstWord);
        System.out.println("---" + roles[0] + "----");

    }

}
