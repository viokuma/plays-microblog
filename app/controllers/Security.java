package controllers;

import java.util.Date;

import models.User;

public class Security extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
    	User user = User.find("username=?", username).first();
    	return user != null && user.password.equals(password);
    }
    
	static boolean check(String profile) {
		return connected().equals(profile);
	}
	
    static void onAuthenticated() {
    	redirect("/?type=1");
    }
	
    static void onDisconnected() {
    	redirect("/");
    }
	
    
}
