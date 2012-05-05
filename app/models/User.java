package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.lang.StringUtils;

import controllers.Pager;

import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class User extends Model {

	@Required
	@Column(nullable=false, unique=true)
	public String username;
	
	@Required
	@Column(nullable=false)
	public String password;
	
	@Required
	@Email
	public String email;
	
	@Required
	public Date createTime = new Date();
	
	@ManyToMany
	@JoinTable(name = "follows", joinColumns = @JoinColumn(name = "master_id"), inverseJoinColumns = @JoinColumn(name = "slave_id"))
	public List<User> followings;
	
	@ManyToMany
	@JoinTable(name = "follows", joinColumns = @JoinColumn(name = "slave_id"), inverseJoinColumns = @JoinColumn(name = "master_id"))
	public List<User> followers;

	public static User findByUsername(String username) {
		return find("username=?", username).first();
	}
	
	@Override
	public String toString() {
		return username;
	}

	public static Pager search(String key, String username, Pager pager) {
		String hql = "username like ? and username<>? and id not in (select f.id from User u join u.followings f where u.username=?)";
		pager.models = find(hql, "%" + key + "%", username, username).fetch();
		pager.records = count(hql, "%" + key + "%", username, username);
		return pager;
	}

	public static long countFollowings(String username) {
		return find("select count(f) from User u join u.followings f where u.username=?", username).first();
	}

	public static long countFollowers(String username) {
		return find("select count(f) from User u join u.followers f where u.username=?", username).first();
	}

	public static Pager findFollowings(String username, Pager pager) {
		pager.models = find("select f from User u join u.followings f where u.username=?", username).fetch(pager.page, pager.rows);
		pager.records = countFollowings(username);
		return pager;
	}

	public static Pager findFollowers(String username, Pager pager) {
		pager.models = find("select f from User u join u.followers f where u.username=?", username).fetch(pager.page, pager.rows);
		pager.records = countFollowers(username);
		return pager;
	}

}
