package com.example.clabuyakchai.brushtrainingsimulator.validator;

import com.example.clabuyakchai.brushtrainingsimulator.model.UserRegistration;

/**
 * Created by Clabuyakchai on 05.05.2018.
 */

public class RegistrationValidator {

    public static String userValidation(UserRegistration user){

        if(user == null){
            return "Fields must not be empty!";
        }

        if(user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()){
            return "Fields must not be empty!";
        }

        if(user.getUsername().length() < 6 || user.getUsername().length() > 32){
            return "Please use between 6 and 32 characters.";
        }

        if(user.getEmail().length() < 5 || user.getEmail().length() > 50){
            return "Check email.";
        }

        if(user.getPassword().length() < 8 || user.getPassword().length() > 32){
            return "The password must be between 8 and 32 characters";
        }

        return null;
    }
}
