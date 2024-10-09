package vn.com.gsoft.security.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserProfile")
public class UserProfile implements Serializable {
    @Id
    @Column(name = "Id")
    private Long id;
    @Column(name = "UserId")
    private Long userId;
    @Column(name = "UserName")
    private String userName;
    @Column(name = "Password")
    private String password;
    @Column(name = "TenDayDu")
    private String tenDayDu;
    @Column(name = "Email")
    private String email;
    @Column(name = "SoDienThoai")
    private String soDienThoai;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "HoatDong")
    private Boolean hoatDong;
    @Column(name = "SoCMT")
    private String soCMT;
    @Column(name = "RegionId")
    private Long regionId;
    @Column(name = "CityId")
    private Long cityId;
    @Column(name = "WardId")
    private Long wardId;
    @Column(name = "Addresses")
    private String addresses;
    @Column(name = "TokenDevice")
    private String tokenDevice;
    @Column(name = "TokenBrowser")
    private String tokenBrowser;
    @Column(name = "IsVerificationAccount")
    private Boolean isVerificationAccount;
    @Column(name = "EntityId")
    private Long entityId;
    @Column(name = "IsAdminCS")
    private Boolean isAdminCS;
    @Transient
    private String entityCode;
}

