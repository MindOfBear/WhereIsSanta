package com.app.christmas.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.christmas.models.Letter;

public interface LetterRepository extends JpaRepository<Letter, Integer>{
	
	long countByUserId(int userId);
	List<Letter> findByApproved(int approved, Sort sort);
	
	@Query("SELECT COUNT(l) FROM Letter l WHERE l.user.id = :userId AND YEAR(l.createdAt) = :year")
	long countByUserIdAndYear(@Param("userId") int userId, @Param("year") int year);
}
