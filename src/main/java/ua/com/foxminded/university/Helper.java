package ua.com.foxminded.university;

import org.modelmapper.ModelMapper;

import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.model.UserModel;

public class Helper {

    public static void main(String[] args) {
        UserModel model = new UserModel();
        model.setFirstName("Bill");
        model.setUserAuthority(new UserAuthorityModel());
        model.getUserAuthority().setAuthority(Authority.ADMINISTRATOR);
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.typeMap(UserModel.class, UserEntity.class).addMappings(mapper -> {
            mapper.map(src -> src.getUserAuthority().getAuthority(), 
                       (destination, value) -> destination.getUserAuthority()
                                                          .setAuthority((Authority)value));
        });
        UserEntity entity = modelMapper.map(model, UserEntity.class);
        
        System.out.println(entity.toString());
        
        
    }

}
