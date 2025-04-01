package com.billing_system.auth.Controllers;

import com.billing_system.auth.DTO.Request.*;
import com.billing_system.auth.DTO.Response.AuthResponseDTO;
import com.billing_system.auth.DTO.Response.ResponseDTO;
import com.billing_system.auth.Services.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO, HttpServletRequest request) throws JsonProcessingException {
        return new ResponseEntity<>(authService.Register(registerRequestDTO, request), HttpStatus.CREATED);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<AuthResponseDTO> verify_code(@Valid @RequestBody VerificationCodeDTO verificationCodeDTO, HttpServletRequest request){
        return new ResponseEntity<>(authService.VerifyCode(verificationCodeDTO,request), HttpStatus.OK);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO> reset_password(@Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO, HttpServletRequest request) throws JsonProcessingException {
        return new ResponseEntity<>(authService.ResetPassword(resetPasswordRequestDTO,request), HttpStatus.OK);
    }
    @PostMapping("/verify-reset-code")
    public ResponseEntity<ResponseDTO> verify_reset_code(@Valid @RequestBody ResetPasswordVerifyCodeDTO resetPasswordVerifyCodeDTO, HttpServletRequest request){
        return new ResponseEntity<>(authService.VerifyResetPasswordCode(resetPasswordVerifyCodeDTO, request), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseDTO> change_password(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO, HttpServletRequest request){
        return new ResponseEntity<>(authService.ChangePassword(changePasswordRequestDTO, request), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody  LoginRequestDTO loginRequestDTO, HttpServletRequest request){
        return new ResponseEntity<>(authService.Login(loginRequestDTO, request), HttpStatus.OK);
    }
}
