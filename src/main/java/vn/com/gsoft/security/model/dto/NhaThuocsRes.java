package vn.com.gsoft.security.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NhaThuocsRes {
    private Long id;
    private String maNhaThuoc;
    private String tenNhaThuoc;
    private String nguoiPhuTrach;
    private String role;

    public NhaThuocsRes(Long id, String maNhaThuoc, String tenNhaThuoc, String nguoiPhuTrach, String role) {
        this.id = id;
        this.maNhaThuoc = maNhaThuoc;
        this.tenNhaThuoc = tenNhaThuoc;
        this.nguoiPhuTrach = nguoiPhuTrach;
        this.role = role;
    }
}
