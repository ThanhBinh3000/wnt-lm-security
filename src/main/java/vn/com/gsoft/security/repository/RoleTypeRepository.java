package vn.com.gsoft.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.RoleType;

import java.util.Optional;


@Repository
public interface RoleTypeRepository extends CrudRepository<RoleType, Long> {

}
