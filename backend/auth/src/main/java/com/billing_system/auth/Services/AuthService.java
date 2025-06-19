package com.billing_system.auth.Services;

import com.billing_system.auth.DTO.Request.*;
import com.billing_system.auth.DTO.Response.AuthResponseDTO;
import com.billing_system.auth.DTO.Response.ResponseDTO;
import com.billing_system.auth.Entities.Users;
import com.billing_system.auth.Exceptions.*;
import com.billing_system.auth.RabbitMQ.Producer;
import com.billing_system.auth.Repositories.UserRepository;
import com.billing_system.auth.Utils.CodeGenerator;
import com.billing_system.auth.Utils.DateFormat;
import com.billing_system.auth.Utils.JwtToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final DateFormat dateFormat;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtToken jwtToken;
    private final Producer producer;
    private final CodeGenerator codeGenerator;
    private final ValueOperations<String, String> valueOperations;

    @Autowired
    public AuthService(DateFormat dateFormat,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtToken jwtToken,
                       Producer producer,
                       CodeGenerator codeGenerator,
                       ValueOperations<String, String> valueOperations){
        this.dateFormat = dateFormat;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtToken = jwtToken;
        this.producer = producer;
        this.codeGenerator = codeGenerator;
        this.valueOperations = valueOperations;
    }

    public ResponseDTO Register(RegisterRequestDTO registerRequestDTO, HttpServletRequest request) throws JsonProcessingException {
        //verify if user exist in db
        Optional<Users> userEmailOptional = userRepository.findUsersByEmail(registerRequestDTO.getEmail());
        if(userEmailOptional.isPresent()){
         throw new UserAlreadyRegisterException();
        }
        //verify if phone_number exist in db
        Optional<Users> userPhoneOptional = userRepository.findUsersByPhoneNumber(registerRequestDTO.getPhone_number());
        if(userPhoneOptional.isPresent()){
            throw  new PhoneNumberAlreadyExistException();
        }
        //hash password
        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        //create Users Entity
        Users user = new Users();
        user.setName(registerRequestDTO.getName());
        user.setLast_name(registerRequestDTO.getLast_name());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setAddress(registerRequestDTO.getAddress());
        user.setPhoneNumber(registerRequestDTO.getPhone_number());
        user.setAvatar("https://i.pinimg.com/736x/d6/76/21/d676216ecdec8474c8f611fb5394575d.jpg");
        //save User Entity
        Users userSaved = userRepository.save(user);
        //generate verification code
        String code = String.valueOf(codeGenerator.generateUniqueCode());
        //save code in cache
        valueOperations.set(userSaved.getPhoneNumber(), code, 5, TimeUnit.MINUTES);
        //send message to queue of rabbit
        producer.sendVerificationCode(userSaved.getPhoneNumber(), code);
        return getResponseDTO("P-201", "Registration successful! A verification code has been sent to your registered WhatsApp number. Please enter the code to complete the process.", userSaved.getPhoneNumber(), request);
    }

    public AuthResponseDTO Login(LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        Optional<Users> userOptional = userRepository.findUsersByEmail(loginRequestDTO.getEmail());
        if(userOptional.isEmpty()){
            throw new UserNotExistException();
        }
        Users user = userOptional.get();
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            throw new PasswordDontMatchException();
        }
        String token = jwtToken.generateToken(user.getId_user(), user.getEmail());
        return getAuthResponseDTO("P-200", "Login successful! Welcome back.", token, request);
    }

    public AuthResponseDTO VerifyCode(VerificationCodeDTO verificationCodeDTO, HttpServletRequest request) {
        String code = valueOperations.get(verificationCodeDTO.getPhoneNumber());
        Optional<Users> usersOptional = userRepository.findUsersByPhoneNumber(verificationCodeDTO.getPhoneNumber());
        if(usersOptional.isEmpty()){
            throw new UserNotExistException();
        }
        Users user = usersOptional.get();
        if (code == null || code.isEmpty()){
            throw new UnauthorizedException();
        }
        if(!Objects.equals(verificationCodeDTO.getCode(), code)) {
            throw new BadVerifyCodeException();
        }
        String token = jwtToken.generateToken(user.getId_user(),user.getEmail());
        valueOperations.getOperations().delete(verificationCodeDTO.getPhoneNumber());
        return getAuthResponseDTO("P-200", "The code has been successfully verified.", token, request);
    }

    public ResponseDTO ResetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO, HttpServletRequest request) throws JsonProcessingException {
        Optional<Users> userOptional = userRepository.findUsersByEmail(resetPasswordRequestDTO.getEmail());
        if(userOptional.isEmpty()){
            throw new UserNotExistException();
        }
        Users user = userOptional.get();
        String code = String.valueOf(codeGenerator.generateUniqueCode());
        valueOperations.set(user.getEmail(), code,5, TimeUnit.MINUTES);
        producer.sendResetPasswordCode(user.getEmail(), code);
        return getResponseDTO("P-200", "A password reset code has been sent to your email. Please check your inbox and follow the instructions to securely reset your password.", user.getEmail(), request);
    }

    public ResponseDTO VerifyResetPasswordCode(ResetPasswordVerifyCodeDTO resetPasswordVerifyCodeDTO, HttpServletRequest request){
        Optional<Users> usersOptional = userRepository.findUsersByEmail(resetPasswordVerifyCodeDTO.getEmail());
        if(usersOptional.isEmpty()){
            throw new UserNotExistException();
        }
        Users user = usersOptional.get();
        String code = valueOperations.get(user.getEmail());
        if (code == null || code.isEmpty()){
            throw new UnauthorizedException();
        }
        if (!code.equals(resetPasswordVerifyCodeDTO.getCode())){
         throw new CodeInvalidException();
        }
        valueOperations.getOperations().delete(user.getEmail());
        valueOperations.set(user.getEmail(), "valid", 5, TimeUnit.MINUTES);
        return getResponseDTO("P-200", "The reset password code is valid. You can now proceed to reset your password.", String.valueOf(user.getId_user()), request);
    }

    public ResponseDTO ChangePassword(ChangePasswordRequestDTO changePasswordRequestDTO, HttpServletRequest request){
        Optional<Users> usersOptional = userRepository.findUsersByEmail(changePasswordRequestDTO.getEmail());
        if (usersOptional.isEmpty()){
            throw new UserNotExistException();
        }
        if(!Objects.equals(changePasswordRequestDTO.getPassword(), changePasswordRequestDTO.getConfirm_password())){
            throw new PasswordDontMatchException();
        }
        Users user = usersOptional.get();
        if(passwordEncoder.matches(changePasswordRequestDTO.getPassword(), user.getPassword())){
           throw new PasswordSameAsPreviousException();
        }
        String IsAvaibleForUpdate = valueOperations.get(user.getEmail());
        if(IsAvaibleForUpdate == null || IsAvaibleForUpdate.isEmpty()){
            throw new UnauthorizedException();
        }
        if(IsAvaibleForUpdate.equals("valid")){
          String encodedPassword = passwordEncoder.encode(changePasswordRequestDTO.getPassword());
          user.setPassword(encodedPassword);
          userRepository.save(user);
        }
        return getResponseDTO("P-200", "Your password has been successfully updated. You can now log in with your new password.", String.valueOf(user.getId_user()), request);
    }
    private ResponseDTO getResponseDTO(String code, String message, String identifier, HttpServletRequest request){
        ResponseDTO response = new ResponseDTO();
        response.setCode(code);
        response.setMessage(message);
        response.setIdentifier(identifier);
        response.setUri(request.getRequestURI());
        response.setTimestamp(dateFormat.getDate());
        return response;
    }
    private AuthResponseDTO getAuthResponseDTO(String code, String message, String token, HttpServletRequest request){
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setCode(code);
        authResponseDTO.setUri(request.getRequestURI());
        authResponseDTO.setTimestamp(dateFormat.getDate());
        authResponseDTO.setToken(token);
        authResponseDTO.setMessage(message);
        return authResponseDTO;
    }
}
