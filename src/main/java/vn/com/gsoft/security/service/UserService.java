package vn.com.gsoft.security.service;

import vn.com.gsoft.security.entity.NhaThuocs;
import vn.com.gsoft.security.entity.UserProfile;
import vn.com.gsoft.security.model.system.Profile;

import java.util.Optional;

public interface UserService extends BaseService {
    Optional<Profile> findUserByToken(String token, String username);

    Optional<Profile> findUserByUsername(String token);

    Optional<Profile> findByUserNameWhenChoose(String username);

    Optional<Profile> getUserNameWhenChoose(String username);

    Optional<Profile> chooseNhaThuoc(String token, String username);

    UserProfile findByUsername(String username);

    void save(UserProfile username);
    NhaThuocs findByMaNhaThuoc(String maNhaThuoc);
}
