package com.billing_system.users.Services;

import com.billing_system.users.DTO.Request.ChangePasswordRequestDTO;
import com.billing_system.users.DTO.Request.UserRequestDTO;
import com.billing_system.users.DTO.Response.ResponseDTO;
import com.billing_system.users.Entities.Users;
import com.billing_system.users.Repositories.UserRepository;
import com.billing_system.users.utils.DateFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    private final UserRepository userRepository;
    private final DateFormat dateFormat;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UserRepository userRepository,
                        DateFormat dateFormat,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dateFormat = dateFormat;
        this.passwordEncoder = passwordEncoder;
    }

    public Users getUserById(Long id_user){
        Optional<Users> userOptional = userRepository.findById(id_user);
        if ( userOptional.isEmpty() ){
            throw new IllegalArgumentException("User not found");
        }
        return userOptional.get();
    }


    public ResponseDTO editUser(Long id_user, UserRequestDTO user, HttpServletRequest request){
        Optional<Users> userOptional = userRepository.findById(id_user);
        if ( userOptional.isEmpty() ){
            throw new IllegalArgumentException("User not found");
        }
        Users userToUpdate = userOptional.get();
        userToUpdate.setName(user.getName());
        userToUpdate.setLast_name(user.getLast_name());
        userToUpdate.setAddress(user.getAddress());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setAvatar(user.getAvatar());
        userRepository.save(userToUpdate);
        return getResponseDTO("P-200", "User updated successfully", request);
    }

    public ResponseDTO changePassword(Long id_user, ChangePasswordRequestDTO changePasswordRequestDTO, HttpServletRequest request){
        Optional<Users> userOptional = userRepository.findById(id_user);
        if ( userOptional.isEmpty() ){
            throw new IllegalArgumentException("User not found");
        }
        if ( !changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getConfirm_password()) ){
            throw new IllegalArgumentException("Passwords do not match");
        }
        if(changePasswordRequestDTO.getOldPassword().equals(changePasswordRequestDTO.getNewPassword())){
            throw new IllegalArgumentException("New password cannot be the same as old password");
        }

        Users userToUpdate = userOptional.get();
        if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), userToUpdate.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        userToUpdate.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
        userRepository.save(userToUpdate);
        return getResponseDTO("P-200", "Password updated successfully", request);
    }

    private ResponseDTO getResponseDTO(String code, String message, HttpServletRequest request){
        ResponseDTO response = new ResponseDTO();
        response.setCode(code);
        response.setMessage(message);
        response.setUri(request.getRequestURI());
        response.setTimestamp(dateFormat.getDate());
        return response;
    }
}
