package controllers;

import play.mvc.With;

@With(Secure.class)
@Check("admin")
public class Users extends CRUD {

	
}
