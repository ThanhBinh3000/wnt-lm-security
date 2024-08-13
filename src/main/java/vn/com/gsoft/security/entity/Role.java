package vn.com.gsoft.security.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Role")
public class Role extends BaseEntity{
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "RoleName")
    private String roleName;
    @Column(name = "IsDeleted")
    private Boolean isDeleted;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "Description")
    private String description;
    @Column(name = "RoleTypeId")
    private Long roleTypeId;
    @Column(name = "IsDefault")
    private Boolean isDefault;  // true là mặc định
    @Transient
    private String roleType;
}

