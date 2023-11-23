package gj.nisum.ejercicio.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Phone {
    @Id
    @GeneratedValue
    Integer id;
    String number;
    String citycode;
    String countrycode;
}
