package vn.com.gsoft.security.model.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.security.core.userdetails.UserDetails;
import vn.com.gsoft.security.entity.NhaThuocs;
import vn.com.gsoft.security.entity.Role;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Profile implements UserDetails, Serializable {
    private static final long serialVersionUID = 620L;
    private static final Log logger = LogFactory.getLog(Profile.class);
    @JsonIgnore
    private String password;
    private String username;
    private Set<CodeGrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Long id;

    private String fullName;
    private List<Role> roles;
    private String maCoSo;
    private Long citiId;
    private Long regionId;
    private Long wardId;
    private String tenNhaThuoc;
    private String soDienThoai;
    private String diaChi;
    private Boolean isAdmin;
    private Integer soCoSo;
    private String maNhaCha;


    public Profile(Long id, String fullName,
                   String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                   boolean accountNonLocked, Set<CodeGrantedAuthority> authorities,
                   String maCoSo, long citiId, long regionId, long wardId,
                   String tenNhaThuoc, String soDienThoai, String diaChi,
                   Boolean isAdmin, Integer soCoSo, String maNhaCha

    ) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
        this.maCoSo = maCoSo;
        this.citiId = citiId;
        this.regionId = regionId;
        this.wardId = wardId;
        this.tenNhaThuoc = tenNhaThuoc;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.isAdmin = isAdmin;
        this.soCoSo = soCoSo;
        this.maNhaCha = maNhaCha;
    }
}
