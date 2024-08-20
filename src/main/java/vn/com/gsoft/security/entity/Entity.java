package vn.com.gsoft.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Data
@jakarta.persistence.Entity
@Table(name = "Entity")
@EntityListeners(AuditingEntityListener.class)
public class Entity extends BaseEntity {
    @Id
    private Long id;

    @Column(name = "Code")
    private String code;

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private Integer type;

    @Column(name = "IsDefault")
    private Boolean isDefault;
}
