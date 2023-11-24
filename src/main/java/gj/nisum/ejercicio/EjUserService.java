package gj.nisum.ejercicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gj.nisum.ejercicio.auth.RegisterRequest;
import gj.nisum.ejercicio.user.Role;
import gj.nisum.ejercicio.user.User;
import gj.nisum.ejercicio.user.UserDTO;
import gj.nisum.ejercicio.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EjUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    

    Logger logger = LoggerFactory.getLogger(EjUserService.class);
    ModelMapper modelMapper = new ModelMapper();


    public UserDTO getUser(String email_param) {
        logger.info(email_param);
        Optional<User> user=userRepository.findByEmail(email_param);
        logger.info(user.toString());
        User user_e=user.get();
        UserDTO dto=modelMapper.map(user_e, UserDTO.class);
        logger.info(dto.toString());
        return dto;        
    }

    public List<UserDTO> getAllUsers() {
        List<User> users=userRepository.findAll();
        return users.stream().map(user->modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    public UserDTO updateUser(String email_param, RegisterRequest request) {
        User user=(userRepository.findByEmail(email_param)).get();

        logger.info(email_param);
        logger.info(request.toString());
        logger.warn(user.toString());

        user.setModified(LocalDateTime.now());


        if(request.getPassword()!=null) user.setPassword(passwordEncoder.encode( request.getPassword()));
        if(request.getName()!=null) user.setName(request.getName());
        if(request.getRole()!=null) user.setRole(request.getRole()=="USER"?Role.USER:Role.ADMIN);
        if(request.getPhones()!=null) user.setPhones(request.getPhones());
        if(request.getIsActive()!=null) user.setActive(request.getIsActive()=="true"?true:false);
            
        userRepository.save(user);


        return getUser(email_param);
    }

    public void delete(String email_param) {
        User user=(userRepository.findByEmail(email_param)).get();
        userRepository.delete(user);
    }
    
}
