package vn.com.gsoft.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import vn.com.gsoft.security.constant.CachingConstant;
import vn.com.gsoft.security.entity.*;
import vn.com.gsoft.security.model.dto.ChooseNhaThuoc;
import vn.com.gsoft.security.model.dto.NhaThuocsReq;
import vn.com.gsoft.security.model.dto.NhaThuocsRes;
import vn.com.gsoft.security.model.system.CodeGrantedAuthority;
import vn.com.gsoft.security.model.system.Profile;
import vn.com.gsoft.security.repository.*;
import vn.com.gsoft.security.service.RedisListService;
import vn.com.gsoft.security.service.RoleService;
import vn.com.gsoft.security.service.UserService;
import vn.com.gsoft.security.util.system.DataUtils;
import vn.com.gsoft.security.util.system.JwtTokenUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private NhaThuocsRepository nhaThuocsRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RedisListService redisListService;

    @Override
    @Cacheable(value = CachingConstant.USER_TOKEN, key = "#token+ '-' +#username")
    public Optional<Profile> findUserByToken(String token, String username) {
        log.warn("Cache findUserByToken missing: {}", username);
        redisListService.addValueToListEnd(username, token);
        return findUserByUsername(username);
    }

    @Override
    public Optional<Profile> findUserByUsername(String username) {
        Optional<UserProfile> user = userProfileRepository.findByUserName(username);
        if (!user.isPresent()) {
            throw new BadCredentialsException("Không tìm thấy username!");
        }
        Set<CodeGrantedAuthority> privileges = new HashSet<>();
        //kiểm tra quyền thành viên
        var entityId = nhaThuocsRepository.findByMaNhaThuoc(user.get().getMaNhaThuoc()).getEntityId();
        List<Privilege> privilegeObjs = privilegeRepository.findByRoleIdInAndEntityId(Math.toIntExact(entityId));
        for (Privilege p : privilegeObjs) {
            privileges.add(new CodeGrantedAuthority(p.getCode()));
        }
        List<Role> roles = new ArrayList<>();
        return Optional.of(new Profile(
                user.get().getId(),
                user.get().getTenDayDu(),
                user.get().getUserName(),
                user.get().getPassword(),
                user.get().getHoatDong(),
                true,
                true,
                true,
                privileges,
                user.get().getMaNhaThuoc()
        ));
    }

    @Override
    public Optional<Profile> findByUserNameWhenChoose(String username) {
        Optional<UserProfile> user = userProfileRepository.findByUserName(username);
        if (!user.isPresent()) {
            throw new BadCredentialsException("Không tìm thấy username!");
        }
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ChooseNhaThuoc chooseNhaThuoc = (ChooseNhaThuoc) requestAttributes.getAttribute("chooseNhaThuoc", RequestAttributes.SCOPE_REQUEST);
        Set<CodeGrantedAuthority> privileges = new HashSet<>();
        NhaThuocsReq req = new NhaThuocsReq();
        req.setUserIdQueryData(user.get().getId());

        return Optional.of(new Profile(
                user.get().getId(),
                user.get().getTenDayDu(),
                user.get().getUserName(),
                user.get().getPassword(),
                user.get().getHoatDong(),
                true,
                true,
                true,
                privileges,
                user.get().getMaNhaThuoc()
        ));
    }

    @Override
    public Optional<Profile> getUserNameWhenChoose(String username) {
        return Optional.empty();
    }

    @Override
    @CachePut(value = CachingConstant.USER_TOKEN, key = "#token+ '-' +#username")
    public Optional<Profile> chooseNhaThuoc(String token, String username) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            ChooseNhaThuoc chooseNhaThuoc = (ChooseNhaThuoc) requestAttributes.getAttribute("chooseNhaThuoc", RequestAttributes.SCOPE_REQUEST);
            if (chooseNhaThuoc != null) {
                redisListService.addValueToListEnd(username, token);
                return findByUserNameWhenChoose(username);
            }
        }
        return Optional.ofNullable(null);
    }

    @Override
    public UserProfile findByUsername(String username) {
        return userProfileRepository.findByUserName(username).orElse(null);
    }

    @Override
    public void save(UserProfile username) {
        userProfileRepository.save(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username).get();
    }
    @Override
    public NhaThuocs findByMaNhaThuoc(String maNhaThuoc){
        return nhaThuocsRepository.findByMaNhaThuoc(maNhaThuoc);
    }
}
