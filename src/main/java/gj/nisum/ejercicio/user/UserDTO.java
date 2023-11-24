package gj.nisum.ejercicio.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserDTO {
    UUID id;
    String name;
    String email;
    Role role;
    LocalDateTime created;
    LocalDateTime modified;
    LocalDateTime last_login;
    String token;
    boolean isActive;
    List<Phone> phones;

}
