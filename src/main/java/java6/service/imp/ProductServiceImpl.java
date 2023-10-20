package java6.service.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java6.domain.Account;
import java6.domain.Product;
import java6.reponsitory.ProductReponsitory;
import java6.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductReponsitory productReponsitory;

	@Override
	public <S extends Product> S save(S entity) {
		Optional<Product> optExist = findById(entity.getProductId());

		if (optExist.isPresent()) {
			entity.setName(entity.getName());
			entity.setQuantity(entity.getQuantity());
			entity.setPrice(entity.getPrice());
			entity.setImg(entity.getImg());
			entity.setDescription(entity.getDescription());
			System.out.println("Cập nhật đầu");
		}
		
		return productReponsitory.save(entity);
	}

	@Override
	public List<Product> findByNameContaining(String name) {
		return productReponsitory.findByNameContaining(name);
	}

	public Page<Product> findByNameContaining(String name, Pageable page) {
		return productReponsitory.findByNameContaining(name, page);
	}

	@Override
	public <S extends Product> Optional<S> findOne(Example<S> example) {
		return productReponsitory.findOne(example);
	}

	@Override
	public List<Product> findAll() {
		return productReponsitory.findAll();
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productReponsitory.findAll(pageable);
	}

	@Override
	public List<Product> findAll(Sort sort) {
		return productReponsitory.findAll(sort);
	}

	@Override
	public List<Product> findAllById(Iterable<String> ids) {
		return productReponsitory.findAllById(ids);
	}

	@Override
	public <S extends Product> List<S> saveAll(Iterable<S> entities) {
		return productReponsitory.saveAll(entities);
	}

	@Override
	public void flush() {
		productReponsitory.flush();
	}

	@Override
	public <S extends Product> S saveAndFlush(S entity) {
		return productReponsitory.saveAndFlush(entity);
	}

	@Override
	public <S extends Product> List<S> saveAllAndFlush(Iterable<S> entities) {
		return productReponsitory.saveAllAndFlush(entities);
	}

	@Override
	public <S extends Product> Page<S> findAll(Example<S> example, Pageable pageable) {
		return productReponsitory.findAll(example, pageable);
	}

	@Override
	public Optional<Product> findById(String id) {
		return productReponsitory.findById(id);
	}

	@Override
	public void deleteInBatch(Iterable<Product> entities) {
		productReponsitory.deleteInBatch(entities);
	}

	@Override
	public boolean existsById(String id) {
		return productReponsitory.existsById(id);
	}

	@Override
	public <S extends Product> long count(Example<S> example) {
		return productReponsitory.count(example);
	}

	@Override
	public void deleteAllInBatch(Iterable<Product> entities) {
		productReponsitory.deleteAllInBatch(entities);
	}

	@Override
	public <S extends Product> boolean exists(Example<S> example) {
		return productReponsitory.exists(example);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<String> ids) {
		productReponsitory.deleteAllByIdInBatch(ids);
	}

	@Override
	public <S extends Product, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return productReponsitory.findBy(example, queryFunction);
	}

	@Override
	public long count() {
		return productReponsitory.count();
	}

	@Override
	public void deleteAllInBatch() {
		productReponsitory.deleteAllInBatch();
	}

	@Override
	public void deleteById(String id) {
		productReponsitory.deleteById(id);
	}

	@Override
	public Product getOne(String id) {
		return productReponsitory.getOne(id);
	}

	@Override
	public void delete(Product entity) {
		productReponsitory.delete(entity);
	}

	@Override
	public Product getById(String id) {
		return productReponsitory.getById(id);
	}

	@Override
	public void deleteAllById(Iterable<? extends String> ids) {
		productReponsitory.deleteAllById(ids);
	}

	@Override
	public void deleteAll(Iterable<? extends Product> entities) {
		productReponsitory.deleteAll(entities);
	}

	@Override
	public Product getReferenceById(String id) {
		return productReponsitory.getReferenceById(id);
	}

	@Override
	public void deleteAll() {
		productReponsitory.deleteAll();
	}

	@Override
	public <S extends Product> List<S> findAll(Example<S> example) {
		return productReponsitory.findAll(example);
	}

	@Override
	public <S extends Product> List<S> findAll(Example<S> example, Sort sort) {
		return productReponsitory.findAll(example, sort);
	}

}
