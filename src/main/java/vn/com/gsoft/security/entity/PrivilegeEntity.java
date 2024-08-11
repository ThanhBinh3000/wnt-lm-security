package vn.com.gsoft.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PrivilegeEntity")
public class PrivilegeEntity extends BaseEntity {
    @Id
    private Long id;
    @Column(name = "PrivilegeId")
    private Long privilegeId;
    @Column(name = "EntityId")
    private Long entityId;
}
