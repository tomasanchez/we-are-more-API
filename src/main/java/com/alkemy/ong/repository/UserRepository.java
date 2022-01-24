package com.alkemy.ong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alkemy.ong.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}
