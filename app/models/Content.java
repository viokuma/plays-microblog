package models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import controllers.Contents;
import controllers.Pager;
import controllers.Security;

import play.data.validation.Max;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Content extends Model {

	@Required
	@Max(140)
	public String message;

	@Required
	public Date createTime;
	
	@Required
	@ManyToOne
	public User user;

	public Content(String message, User user) {
		this.message = message;
		this.user = user;
		this.createTime = new Date();
	}

	public static List<Content> findAlls(Pager pager) {
		pager.models =  find("order by createTime desc").fetch(pager.page, pager.rows);
		pager.records = count();
		return pager.models;
	}
	
	public static List<Content> findAllByUser(String username, Pager pager) {
		String hql = "user.username=? or user.id in (select a.id from User u join u.followings a where u.username=?)";
		pager.models = find(hql + " order by createTime desc", username, username).fetch(pager.page, pager.rows);
		pager.records = count(hql, username, username);
		return pager.models;
	}

	public static List<Content> findMineByUser(String username, Pager pager) {
		String hql = "user.username=?";
		pager.models = find(hql + " order by createTime desc", username).fetch(pager.page, pager.rows);
		pager.records = count(hql, username);
		return pager.models;
	}

}
