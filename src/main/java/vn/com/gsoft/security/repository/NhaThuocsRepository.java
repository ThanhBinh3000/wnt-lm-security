package vn.com.gsoft.security.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.constant.RecordStatusContains;
import vn.com.gsoft.security.entity.NhaThuocs;
import vn.com.gsoft.security.model.dto.NhaThuocsReq;

import java.util.List;

@Repository
public interface NhaThuocsRepository extends CrudRepository<NhaThuocs, Long> {
    @Query(value = "SELECT c.id, c.maNhaThuoc, c.tenNhaThuoc, nv.role, " +
            " (SELECT ua.tenDayDu From UserProfile ua JOIN  NhanVienNhaThuocs nva on nva.User_UserId = ua.id where nva.role = 'Admin' and nva.NhaThuoc_MaNhaThuoc = c.maNhaThuoc  ) as  nguoiPhuTrach " +
            " FROM NhaThuocs c " +
            " join NhanVienNhaThuocs nv  on c.maNhaThuoc = nv.NhaThuoc_MaNhaThuoc " +
            " join UserProfile u on nv.User_UserId = u.id " +
            " WHERE 1=1 AND u.id = :#{#param.userIdQueryData} " +
            " AND lower(c.maNhaThuoc) LIKE lower(:#{#param.maNhaThuoc})" +
            " ORDER BY c.id desc", nativeQuery = true
    )
    Tuple getUserRoleNhaThuoc(@Param("param") NhaThuocsReq req);
    NhaThuocs findByMaNhaThuoc(String maNhaThuoc);
    @Query(value = "SELECT COUNT(*) FROM NhaThuocs c " +
            "WHERE 1=1 AND c.maNhaCha = ?1 AND c.recordStatusId = 0")
    Integer countByMaNhaThuocCha(String maNhaThuoc);
}
