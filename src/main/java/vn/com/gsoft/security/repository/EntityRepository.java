package vn.com.gsoft.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.com.gsoft.security.entity.Entity;

import java.util.List;

public interface EntityRepository extends CrudRepository<Entity, Long> {

}
