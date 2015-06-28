package ca.appdirect.appchallenge.model.dao;

import org.springframework.data.repository.CrudRepository;

import ca.appdirect.appchallenge.model.lib.database.OrderingCompany;

public interface IOrderingCompanyDAO extends CrudRepository<OrderingCompany, Integer> {

}