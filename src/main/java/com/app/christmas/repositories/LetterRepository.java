package com.app.christmas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.christmas.models.Letter;

public interface LetterRepository extends JpaRepository<Letter, Integer>{
	
	public Letter findByEmail(String email);

}
