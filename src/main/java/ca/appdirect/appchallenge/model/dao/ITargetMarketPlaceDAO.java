package ca.appdirect.appchallenge.model.dao;

import org.springframework.data.repository.CrudRepository;

import ca.appdirect.appchallenge.model.lib.database.TargetMarketPlace;

public interface ITargetMarketPlaceDAO extends CrudRepository<TargetMarketPlace, Integer> {

}