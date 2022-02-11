package com.alkemy.ong.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.entity.Category;
import com.alkemy.ong.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  CategoryService categoryService;

  @PostMapping
  public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO category) throws Exception {

    try {
      CategoryDTO categorySaved = categoryService.create(category);
      return ResponseEntity.status(HttpStatus.CREATED).body(categorySaved);
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
    }

  }

  @PreAuthorize("ROL_ADMIN")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    if (categoryService.findById(id) == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    categoryService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> getCategoryDetails(@PathVariable String id) {
    CategoryDTO categoryDetails = categoryService.findCategoryById(id);
    if (categoryDetails == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(categoryDetails);
  }


  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ROL_ADMIN')")
  public ResponseEntity<?> updateCategory(@Validated @RequestBody CategoryDTO categoryDTO,
      @PathVariable UUID id) {

    Map<String, Object> response = new HashMap<>();

    Optional<CategoryDTO> optCategoryDTO = this.categoryService.updateCategory(categoryDTO, id);

    if (!optCategoryDTO.isPresent()) {
      response.put("Error", String.format("Category with ID %s not found.", id));
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    } else {
      response.put("ok", optCategoryDTO);
      return ResponseEntity.ok(response);
    }
  }
  
  /***
   * Se crea una lista y se le pasa la lista del service con los datos del pageable que se recibe, 
   * el size se le pasa 10 ya que es un requerimiento.
   * Luego por medio del PageImpl pasa la lista a Page.
   * Nos aseguramos que no cargue paginas con sin contenido, si esta vacía devuelve la url de la pagina anterior. 
   * @author Mauro
   */
  @GetMapping
  @PreAuthorize("hasAuthority('ROL_USER')")
  public ResponseEntity<?> findAllPage(Pageable pageable){
  	
  	int page = pageable.getPageNumber();
  	final int size = 10;
  	
  	List<CategoryDTO> lista = categoryService.findAllPage(PageRequest.of(page, size));
  	
  	Page<CategoryDTO> pages = new PageImpl<CategoryDTO>(lista, pageable, lista.size());
  	
  	/***
  	 * Valores positivos
  	 */
  	if(page < 0) {
  		return ResponseEntity.status(HttpStatus.NOT_FOUND)
  	  			.body(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString()
  	  					+"/categories/?page=0");
  	}

  	if(page >= 0 && (pages.getNumberOfElements() != 0)) {
  		return ResponseEntity.ok().body(pages);
  	}
  	
  	
  	return ResponseEntity.status(HttpStatus.NOT_FOUND)
  			.body(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString()
  					+"/categories/?page="+pages.previousPageable().getPageNumber());
  }


}
