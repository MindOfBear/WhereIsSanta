package com.app.christmas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.christmas.models.Letter;

public interface LetterRepository extends JpaRepository<Letter, Integer>{
	
	long countByUserId(int userId);
	
	@Query("SELECT COUNT(l) FROM Letter l WHERE l.user.id = :userId AND YEAR(l.createdAt) = :year")
	long countByUserIdAndYear(@Param("userId") int userId, @Param("year") int year);
}
