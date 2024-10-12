package bhushan.userservice.controllers;

import bhushan.userservice.dtos.LoginRequestDto;
import bhushan.userservice.dtos.SignUpRequestDto;
import bhushan.userservice.dtos.UserDto;
import bhushan.userservice.dtos.ValidateTokenRequestDto;
import bhushan.userservice.exception.UserAlreadyExistException;
import bhushan.userservice.exception.UserDoesNotExistException;
import bhushan.userservice.models.SessionStatus;
import bhushan.userservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) throws UserDoesNotExistException {
        return authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());


    }
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpRequestDto signUpRequestDto) throws UserAlreadyExistException {
        ResponseEntity<UserDto>userDto = authService.signUp(signUpRequestDto.getEmail(),signUpRequestDto.getPassword());
        return userDto;
    }
    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequestDto requestDto){
        SessionStatus sessionStatus = authService.validate(requestDto.getToken(), requestDto.getUserId());
        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<SessionStatus> logout(@RequestBody SessionStatus sessionStatus){
        return new
    }



}
