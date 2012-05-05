package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Content;
import models.User;

import org.apache.commons.lang.math.NumberUtils;

import play.cache.Cache;
import play.data.validation.Required;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(value={Secure.class, PagerController.class})
public class App extends Controller {
	
	@Before(priority=1)
	static void before() {
		if (Security.isConnected()) {
			renderArgs.put("username", Security.connected());
		}
		renderArgs.put("followingsCount", User.countFollowings(Security.connected()));
		renderArgs.put("followersCount", User.countFollowers(Security.connected()));
	}
	
    public static void index(int type) {
    	if (!Security.isConnected()) {
    		type = 0;
    	}
    	putType(type);
    	Pager pager = PagerController.getPager();
    	if (type == 0) {
    		Content.findAlls(pager);
    	} else if (type == 1) {
    		Content.findAllByUser(Security.connected(), pager);
    	} else if (type == 2) {
    		Content.findMineByUser(Security.connected(), pager);
    	}
    	pager.completes();
    	
        render(type);
    }
    
    /**
     * 发布微博
     */
    public static void publish(@Required(message="微博不能为空") String message) {
    	if (validation.hasErrors()) {
            validation.keep();
    		index(getType());
    	}
		Content content = new Content(message, User.findByUsername(Security.connected()));
		content.save();
		index(getType());
	}
    
    /**
     * 删除微博
     */
    public static void delete(Long id) {
    	Content content = Content.findById(id);
    	if (content != null) {
    		content.delete();
    	}
    	index(getType());
	}
    
    private static void putType(int type) {
    	session.put("type", type);
    }
    
    private static int getType() {
    	String type = session.get("type");
    	return NumberUtils.toInt(type);
    }
    
    /**
     * 增加关注
     */
    public static void plus(String username) {
		User be = User.findByUsername(username);
		if (be != null) {
			User user = User.findByUsername(Security.connected());
			if (!user.followings.contains(be)) {
				user.followings.add(be);
				user.save();
				renderText("添加成功");
			} else {
				renderText("已经添加");
			}
		} else {
			renderText("没有此用户");
		}
	}
    
    /**
     * 删除关注
     */
    public static void minus(String username) {
		User be = User.findByUsername(username);
		if (be != null) {
			User user = User.findByUsername(Security.connected());
			if (user.followings.contains(be)) {
				user.followings.remove(be);
				user.save();
				renderText("删除成功");
			} else {
				renderText("已经删除");
			}
		} else {
			renderText("没有此用户");
		}
	}
    
    /**
     * 搜索朋友
     */
    public static void search(String key) {
    	Pager pager = PagerController.getPager();
		User.search(key, Security.connected(), pager);
		pager.completes();
		render();
	}
    
    /**
     * 显示关注或跟随的列表
     * @param inverse 是否为跟随
     */
    public static void listFollows(boolean inverse) {
    	Pager pager = PagerController.getPager();
    	if (inverse) {
    		User.findFollowers(Security.connected(), pager);
    	} else {
    		User.findFollowings(Security.connected(), pager);
    	}
    	pager.completes();
		render(inverse);
	}

}

