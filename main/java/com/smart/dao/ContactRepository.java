package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	//pagination method....
	@Query("from Contact as c where c.user.id =:userid ")
	//currentPage-page
	//contact per page - 5
	public Page<Contact> findcontactsByUser(@Param("userid") int userid, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("delete from Contact as c where c.cId=:cid")
	public void deleteContactById(@Param("cid") Integer cid);

	//search
	public List<Contact> findByNameContainingAndUser(String name, User user);
	
	
} 
