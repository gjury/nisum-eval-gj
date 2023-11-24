package gj.nisum.ejercicio.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import gj.nisum.ejercicio.auth.jwt.JwtService;
import gj.nisum.ejercicio.user.Role;
import gj.nisum.ejercicio.user.User;
import gj.nisum.ejercicio.user.UserDTO;

//@SpringBootTest
@WebMvcTest(controllers=AuthController.class)
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
    public void registra_usr_error() throws Exception{
        BDDMockito.given(authService.register(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));
            
        UserDTO user= new UserDTO();
        user.setRole(Role.USER);
        user.setEmail("");
        user.setName("Pruebas");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/register")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON) );
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());


    }
	
}
