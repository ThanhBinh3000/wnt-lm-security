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
import vn.com.gsoft.security.constant.RoleConstant;
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
    private EntityRepository entityRepository;
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
        var entity = entityRepository.findById(user.get().getEntityId());
        var isAdmin = false;
        if (entity.isPresent()) {
            isAdmin = entity.get().getCode().equals(RoleConstant.ROLE_ADMIN);
        }
        Set<CodeGrantedAuthority> privileges = new HashSet<>();
        //kiểm tra quyền thành viên
        var nhaThuoc= new NhaThuocs();
        if(!isAdmin){
            nhaThuoc = nhaThuocsRepository.findByMaNhaThuoc(user.get().getMaNhaThuoc());
        }
        var entityId = nhaThuoc.getMaNhaThuoc() == null ? entity.get().getId() : nhaThuoc.getEntityId();
        List<Privilege> privilegeObjs = privilegeRepository.findByRoleIdInAndEntityId(Math.toIntExact(entityId));
        for (Privilege p : privilegeObjs) {
            privileges.add(new CodeGrantedAuthority(p.getCode()));
        }
        //lấy quyền supper;

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
                user.get().getMaNhaThuoc(),
                nhaThuoc.getCityId() != null ? nhaThuoc.getCityId() : 0L,
                nhaThuoc.getRegionId() != null ? nhaThuoc.getRegionId() : 0L,
                nhaThuoc.getWardId() != null ? nhaThuoc.getWardId() :0L,
                nhaThuoc.getTenNhaThuoc(),
                nhaThuoc.getDienThoai(),
                nhaThuoc.getDiaChi(),
                isAdmin
        ));
    }

    @Override
    public UserProfile findByUsername(String username) {
        var userProfile = userProfileRepository.findByUserName(username);
        if (userProfile.isPresent()) {
            var entity = entityRepository.findById(userProfile.get().getEntityId());
            userProfile.get().setEntityCode(entity.get().getCode());
        }
        return userProfile.orElse(null);
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
