package vn.com.gsoft.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@jakarta.persistence.Entity
@Table(name = "Entity")
@EntityListeners(AuditingEntityListener.class)
public class Entity extends BaseEntity {
    @Id
    private Long id;

    @Column(name = "Code")
    private Long code;

    @Column(name = "Name")
    private Long name;

    @Column(name = "Type")
    private Integer type;
}
