package vn.com.gsoft.security.service;

import vn.com.gsoft.security.entity.NhaThuocs;
import vn.com.gsoft.security.entity.UserProfile;
import vn.com.gsoft.security.model.system.Profile;

import java.util.Optional;

public interface UserService extends BaseService {
    Optional<Profile> findUserByToken(String token, String username);

    Optional<Profile> findUserByUsername(String token);

    UserProfile findByUsername(String username);

    void save(UserProfile username);
    NhaThuocs findByMaNhaThuoc(String maNhaThuoc);
    Optional<Profile> chooseNhaThuoc(String token, String username);
}
