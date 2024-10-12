package bhushan.userservice.services;

import bhushan.userservice.dtos.UserDto;
import bhushan.userservice.exception.PasswordDoesNotMatchException;
import bhushan.userservice.exception.UserAlreadyExistException;
import bhushan.userservice.exception.UserDoesNotExistException;
import bhushan.userservice.models.Session;
import bhushan.userservice.models.SessionStatus;
import bhushan.userservice.models.User;
import bhushan.userservice.repositorys.SessionRepository;
import bhushan.userservice.repositorys.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository,SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.sessionRepository = sessionRepository;
    }

    public ResponseEntity<UserDto> signUp(String email, String password) throws UserAlreadyExistException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isEmpty()) {
            throw  new UserAlreadyExistException("User with email " + email + " already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        User savedUser = userRepository.save(user);
            UserDto userDto = UserDto.fromUser(savedUser);
        ResponseEntity<UserDto> response = ResponseEntity.ok(userDto);
        return  response;
    }

    public ResponseEntity<UserDto> login(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with email " + email + " does not exist");
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new PasswordDoesNotMatchException("Password doesn't match");
        }
        String token = RandomStringUtils.randomAscii(20);
        UserDto userDto = UserDto.fromUser(user);
        MultiValueMapAdapter<String,String> header= new MultiValueMapAdapter<>( new HashMap<>());
        header.add("AUTH Token", token);
        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setSessionStatus(SessionStatus.ACTIVE);
        sessionRepository.save(session);


        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto,header, HttpStatus.OK);
        return   response;
    }
    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUserId(token,userId);
        if (sessionOptional.isEmpty()){
            return  SessionStatus.EXPIRED;
        }
        Session session = sessionOptional.get();
        if(!session.getSessionStatus().equals(SessionStatus.ACTIVE)){
            return  SessionStatus.EXPIRED;
        }
        return  SessionStatus.ACTIVE;
    }
    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUserId(token,userId);
        if(sessionOptional.isEmpty()){
            return  ResponseEntity.noContent().build();
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.LOGGED_OUT);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }
}
