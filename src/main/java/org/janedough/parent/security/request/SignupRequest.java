package org.janedough.parent.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "Username can only contain letters, numbers, dots, underscores, and dashes"
    )
    private String username;

    @NotBlank
    @Size(min = 1, max = 50)
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10–11 digits")
    private String phoneNumber;

    private Set<String> role;

    @NotBlank
    @Size(min = 2, max = 120)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
    )
    private String password;

    private String socialMediaHandle;
}
