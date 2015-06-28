package ca.appdirect.appchallenge.model.dao;

import org.springframework.data.repository.CrudRepository;

import ca.appdirect.appchallenge.model.lib.User;

public interface IUserDAO extends CrudRepository<User, Integer> {

	public User findByOpenId(String openId);
}
