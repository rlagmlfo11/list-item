package com.sample.project.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sample.project.entity.QudtlsMaster;

@Repository
public interface QudtlsMasterRepository extends JpaRepository<QudtlsMaster, Long> {

	@Query("SELECT q FROM QudtlsMaster q WHERE " + "LOWER(q.name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "
			+ "LOWER(q.job) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "
			+ "LOWER(q.reason) LIKE LOWER(CONCAT('%',:keyword,'%'))")
	Page<QudtlsMaster> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

	List<QudtlsMaster> findByName(String name);

}
