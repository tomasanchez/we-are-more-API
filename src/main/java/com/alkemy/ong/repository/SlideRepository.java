package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Organization;
import com.alkemy.ong.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, String> {
  List<Slide> findAllByOrganization(Organization id);
  
  @Query("SELECT c.imageUrl, c.orderNumber FROM Slide c")
  public Iterable<Slide> findAllDefined(); 
}