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
@Table(name = "UserRole")
public class UserRole extends BaseEntity{
    @Id
    @Column(name = "Id")
    private Long id;
    @Column(name = "UserId")
    private Long userId;
    @Column(name = "RoleId")
    private Long roleId;
}

