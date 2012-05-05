package controllers;

import org.apache.commons.lang.math.NumberUtils;

import play.cache.Cache;
import play.mvc.Controller;

public class PagerController extends Controller {

	@play.mvc.Before
	static void before() {
		Pager pager = new Pager();
		pager.page = NumberUtils.toInt(params.get("page"), 1);
		pager.rows = NumberUtils.toInt(params.get("rows"), 5);
		renderArgs.put("pager", pager);
	}
	
    public static Pager getPager() {
    	return renderArgs.get("pager", Pager.class);
//    	return (Pager) request.args.get("pager");
    }
	
}
