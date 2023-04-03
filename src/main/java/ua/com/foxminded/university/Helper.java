package ua.com.foxminded.university;

import java.util.Objects;

public class Helper {
    
    private String name;
    int id;
    
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
//        Class<?> clazz = obj.getClass();
//        Helper helper = obj;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Helper other = (Helper) obj;
        return id == other.id && Objects.equals(name, other.name);
    }
    
    public static void main(String[] args) {
        
        Helper helper = new Helper();
        Helper2 helper2 = new Helper2();
        
        
        
        boolean value = helper.equals(helper2);
        
        
        System.out.println(value);
        
        
        
        
    }
    
    
    boolean someMethod() {
        Helper2 helper2 = new Helper2();
        
        return this.getClass() == helper2.getClass();
        
        
        
    }
    
    
    
    

    
}
