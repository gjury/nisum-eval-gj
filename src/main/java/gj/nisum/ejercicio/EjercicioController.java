package gj.nisum.ejercicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gj.nisum.ejercicio.auth.AuthResponse;
import gj.nisum.ejercicio.auth.RegisterRequest;
import gj.nisum.ejercicio.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EjercicioController {

    private final EjUserService ejUserService;
    // Logger logger = LoggerFactory.getLogger(EjercicioController.class);

    @GetMapping("demo")
    @Operation(hidden = true)
    public String welcome() {
        return "Bienvenido.";
    }

    @GetMapping(value = "usuario/{email_param}")
    @Operation(summary = "Recupera el Usuario dado su email único")
    public UserDTO buscar_usuario(@PathVariable(value = "email_param") String email_param) {
        UserDTO user_dto = ejUserService.getUser(email_param);
        return user_dto;
    }

    @GetMapping(value = "usuario")
    @Operation(summary = "Recupera todos los usuarios")
    public List<UserDTO> buscar_usuarios() {
        List<UserDTO> users_dto = ejUserService.getAllUsers();
        return users_dto;
    }

    @PutMapping(value = "usuario/{email_param}", consumes = "application/json")
    @Operation(summary = "Modifica el Usuario dado su email único")
    public UserDTO editar_usuario(@PathVariable String email_param, @RequestBody RegisterRequest user_request) {
        UserDTO user_dto = ejUserService.updateUser(email_param, user_request);
        return user_dto;
    }

    @DeleteMapping("usuario/{email_param}")
    @Operation(summary = "Elimina el Usuario dado su email único")
    public ResponseEntity<Map<String,String>> eliminar_usuario( @PathVariable(value="email_param") String email_param)
    {
        ejUserService.delete(email_param);

        return ResponseEntity.ok().body(new HashMap<String,String>(1){
            {
                put("mensaje", "Usuario Eliminado " + email_param);
            }
        });
    }


    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleNoSuchElementFoundException(Exception exception) {

        Map<String, String> map = new HashMap<String, String>(1) {
            {
                put("mensaje", exception.getMessage());
            }
        };

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(map);
    }

}
