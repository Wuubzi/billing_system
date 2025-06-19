package com.billing_system.users.Controllers;

import com.billing_system.users.DTO.Request.ChangePasswordRequestDTO;
import com.billing_system.users.DTO.Request.UserRequestDTO;
import com.billing_system.users.DTO.Response.ResponseDTO;
import com.billing_system.users.Entities.Users;
import com.billing_system.users.Services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/profile")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }
    @GetMapping("/get-profile")
    public ResponseEntity<Users> getUser(@RequestParam Long id_user) {
        System.out.println("juan es gay");
        return new ResponseEntity<>(usersService.getUserById(id_user), HttpStatus.OK);
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<ResponseDTO> editUser(@RequestParam Long id_user, @Valid @RequestBody UserRequestDTO user, HttpServletRequest request) {
         return new ResponseEntity<>(usersService.editUser(id_user,user, request), HttpStatus.OK);
    }
    @PutMapping("/change-password")
    public ResponseEntity<ResponseDTO> changePassword(@RequestParam Long id_user, @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO, HttpServletRequest request) {
        return new ResponseEntity<>(usersService.changePassword(id_user, changePasswordRequestDTO, request), HttpStatus.OK);

    }


}
