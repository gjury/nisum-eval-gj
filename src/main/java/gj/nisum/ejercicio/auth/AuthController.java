package gj.nisum.ejercicio.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @Value("${gj-nisum.email.formato}")
    private String emailFormato;
    @Value("${gj-nisum.password.formato}")
    private String passwordFormato;

    @PostMapping("login")
    @Operation(summary = "Permite la Autenticacion con email y password para acceder al sistema", description = "No requiere seguridad")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));

    }

    @PostMapping("register")
    @Operation(summary = "Permite dar de alta un nuevo usuario", description = "No requiere seguridad")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws Exception {

        Pattern patternEmail = Pattern.compile(emailFormato);
        Matcher matcherEmail = patternEmail.matcher(request.getEmail());
        if (!matcherEmail.matches()) {
            throw new Exception("Error en formato de email");
        }
        Pattern patternPwd = Pattern.compile(passwordFormato);
        Matcher matcherPwd = patternPwd.matcher(request.getPassword());
        if (!matcherPwd.matches()) {
            throw new Exception("Error en formato de password");
        }

        return ResponseEntity.ok(authService.register(request));
    }

    @ExceptionHandler()
    public ResponseEntity<Map<String, String>> handleNoSuchElementFoundException(Exception exception) {
        //System.err.println(exception.toString());

        Map<String, String> map = new HashMap<String, String>(1) {
            {
                put("mensaje", "Ya existe un usuario con este email");
            }
        };
        if (exception.getMessage().contains("not-null"))
            map.replace("mensaje", "Falta el mail, dato obligatorio para la creacion del usuario.");
        if (exception.getMessage().contains("formato"))
            map.replace("mensaje", exception.getMessage());
        if (exception.getMessage().contains("JSON"))
            map.replace("mensaje", exception.getMessage());
        if (exception.getMessage().contains("JWT"))
            map.replace("mensaje", exception.getMessage());
        if (exception.getMessage().contains("credentials"))
            map.replace("mensaje", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(map);
    }

}
