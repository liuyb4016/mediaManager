package com.eshore.yxt.media.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface BaseEntityManager<T,PK extends Serializable> extends CrudRepository<T,PK>,JpaSpecificationExecutor<T> {

}
