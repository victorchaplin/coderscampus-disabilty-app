package com.app.repositoy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Disability; // commmuncation from model to database mySql , postgresql, oracle

public interface DisabilityRepository extends JpaRepository<Disability, Long> {
	public Optional<Disability> findByDisability(String disability);
}