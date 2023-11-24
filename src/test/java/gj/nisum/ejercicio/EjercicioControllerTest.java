package gj.nisum.ejercicio;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import gj.nisum.ejercicio.auth.jwt.JwtService;
import gj.nisum.ejercicio.user.UserDTO;

@WebMvcTest(controllers = EjercicioController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class EjercicioControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EjUserService ejUserService;
    @MockBean
    private JwtService jwtService;

    @Test
    public void busca_usr_ok() throws Exception {

        String user = "guille@prueba.com";
        UserDTO udto = new UserDTO();
        udto.setEmail(user);

        Mockito.when(ejUserService.getUser(user)).thenReturn(udto);

        ResultActions response = mockMvc.perform(
                get("/api/v1/usuario/guille@prueba.com")
                        .contentType(MediaType.APPLICATION_JSON));
                //.andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk());
    }

    @Test
    public void busca_usr_error() throws Exception {

        String user = "inexistente@prueba.com";
        UserDTO udto = new UserDTO();
        udto.setEmail(user);

        Mockito.when(ejUserService.getUser(user)).thenThrow();

        ResultActions response = mockMvc.perform(
                get("/api/v1/usuario/"+user)
                        .contentType(MediaType.APPLICATION_JSON));
                //.andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isNotFound());
    }

}
