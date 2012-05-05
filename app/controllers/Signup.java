package controllers;

import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;

public class Signup extends Controller {

	public static void reg() {
		render();
	}
	
	public static void createUser(@Valid User user) throws Throwable {
		if (validation.hasErrors()) {
			params.flash();
			flash.error("请正确填写资料");
			reg();
		}
		user.save();
		flash.success("用户[%s]创建成功", user.username);
		Secure.login();
	}
	
}
