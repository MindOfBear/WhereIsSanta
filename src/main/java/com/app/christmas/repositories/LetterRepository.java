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
	
	@Query("SELECT l FROM Letter l WHERE YEAR(l.createdAt) = :year")
	List<Letter> findAllByYear(@Param("year") int year);
	
	@Query("SELECT DISTINCT YEAR(l.createdAt) FROM Letter l ORDER BY YEAR(l.createdAt) DESC")
	List<Integer> findDistinctYears();
	
	@Query("""
		    SELECT l 
		    FROM Letter l 
		    JOIN l.user u 
		    WHERE (:email IS NULL OR u.email LIKE %:email%) 
		      AND (:year IS NULL OR YEAR(l.createdAt) = :year)
		""")
		List<Letter> findAllByEmailAndYear(@Param("email") String email, @Param("year") Integer year, Sort sort);
}
