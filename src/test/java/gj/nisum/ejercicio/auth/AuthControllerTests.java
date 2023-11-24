package gj.nisum.ejercicio.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import gj.nisum.ejercicio.auth.jwt.JwtService;
import gj.nisum.ejercicio.user.Role;

import gj.nisum.ejercicio.user.UserDTO;


@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registra_usr_error() throws Exception {
        BDDMockito.given(authService.register(ArgumentMatchers.any()))
                .willAnswer((invocation -> invocation.getArgument(0)));

        UserDTO user = new UserDTO();
        user.setRole(Role.USER);
        user.setEmail("");
        user.setName("Pruebas");

        ResultActions response = mockMvc.perform(
                post("/auth/register")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("formato")));
    }

    @Test
    public void registra_usr_ok() throws Exception {

        RegisterRequest user = new RegisterRequest();
        user.setPassword("12312312");
        user.setRole("USER");
        user.setEmail("prueba@gmail.com");
        user.setName("Pruebas");

        Mockito.when(authService.register(user)).thenReturn(AuthResponse.builder()
                .token("tokenASDFASDFADFADF").build());

        ResultActions response = mockMvc.perform(
                post("/auth/register")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk());
    }

    @Test
    public void testLogin_usr_ok() throws Exception {

        LoginRequest user = new LoginRequest();
        user.setPassword("12312312");
        user.setEmail("prueba@gmail.com");

        Mockito.when(authService.login(user)).thenReturn(AuthResponse.builder()
                .token("token-DE-PRUEBA-ABCDEGHIJKLMNOPQ").build());

        ResultActions response = mockMvc.perform(
                post("/auth/login")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(org.hamcrest.Matchers.containsString("token-DE-PRUEBA-ABCDEGHIJKLMNOPQ")));
        ;
    }

    @Test
    public void testLogin_usr_error() throws Exception {

        LoginRequest user = new LoginRequest();
        user.setPassword("PASSWORD MAL");
        user.setEmail("prueba@gmail.com");

        Mockito.when(authService.login(user)).thenThrow();

        ResultActions response = mockMvc.perform(
                post("/auth/login")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isBadRequest());
    }

}
