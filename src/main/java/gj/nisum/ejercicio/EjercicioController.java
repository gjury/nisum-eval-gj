package gj.nisum.ejercicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1")
public class EjercicioController {


    @GetMapping("demo")
    @Operation(summary = "Prueba summary")
    //@SecurityRequirement(name = "Bearer Authentication")
    public String welcome(){
        return "Bienvenido.";
    }
    




}
