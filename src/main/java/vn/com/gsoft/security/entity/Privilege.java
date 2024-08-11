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
@Table(name = "Privilege")
public class Privilege extends BaseEntity {
    @Id
    private Long id;
    @Column(name = "Code")
    private String code;
    @Column(name = "ParentCode")
    private String parentCode;
    @Column(name = "Name")
    private String name;
    @Column(name = "Enable")
    private Boolean enable;
}
