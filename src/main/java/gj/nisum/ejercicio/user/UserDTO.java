package gj.nisum.ejercicio.user;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
    Integer id;
    String name;
    String email;
    Role role;
    List<Phone> phones;

}
