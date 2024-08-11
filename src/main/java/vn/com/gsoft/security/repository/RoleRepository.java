package vn.com.gsoft.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.Role;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    @Query(value = "SELECT r from Role r " +
            "join UserRole ur on ur.roleId = r.id  " +
            "where ur.userId = ?1 and (r.isDefault = true or r.maNhaThuoc = ?2)")
    List<Role> findByUserIdAndMaNhaThuoc(Long userId, String maNhaThuoc);

    @Query(value = "SELECT r from Role r " +
            "join UserRole ur on ur.roleId = r.id  " +
            "join RoleType rt on rt.id = r.roleTypeId  " +
            "where (rt.roleName = 'SuperUser' OR rt.roleName = 'TechnicalSupport') and ur.userId = ?1")
    List<Role> findByUserId(Long id);
}
