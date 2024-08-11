package vn.com.gsoft.security.service;

import vn.com.gsoft.security.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> findByUserIdAndMaNhaThuoc(Long id, String maNhaThuoc);

    List<Role> findByUserId(Long id);
}
