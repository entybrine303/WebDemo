package java6.reponsitory;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java6.domain.Product;

@Repository
public interface ProductReponsitory extends JpaRepository<Product, String> {
	List<Product> findByNameContaining(String name);

	Page<Product> findByNameContaining(String name, Pageable page);
	
    
}
