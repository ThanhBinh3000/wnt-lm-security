package vn.com.gsoft.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
@OpenAPIDefinition(
        info = @Info(
                title = "WNT-SECURITY",
                description = "api doc",
                termsOfService = "WebNhaThuoc",
                contact = @Contact(
                        name = "Tùng",
                        email = "info@webnhathuoc.com"
                ),
                license = @License(
                        name = "WNT"
                ),
                version = "1.0"
        ),
        security = @SecurityRequirement(
                name = "auth"
        )
)
@SecurityScheme(
        name = "auth",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "security desc"

)
public class OpenApiConfig {
}
