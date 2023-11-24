package gj.nisum.ejercicio.auth;

import java.util.List;

import gj.nisum.ejercicio.user.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String name;
    String email;
    String password;
    String role;
    String isActive;
    List<Phone> phones;

}
