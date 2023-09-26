package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.dto.RoleDto;
import fpt.capstone.buildingmanagementsystem.model.request.ChangePasswordRequest;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeStatusAccountRequest;
import fpt.capstone.buildingmanagementsystem.model.request.LoginRequest;
import fpt.capstone.buildingmanagementsystem.model.request.RegisterRequest;
import fpt.capstone.buildingmanagementsystem.model.response.JwtResponse;
import fpt.capstone.buildingmanagementsystem.security.JwtTokenUtil;
import fpt.capstone.buildingmanagementsystem.service.AccountManageService;
import fpt.capstone.buildingmanagementsystem.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AccountController {
    @Autowired
    private AuthenticateService authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AccountManageService accountManageService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody RegisterRequest registerRequest) throws Exception {
        return ResponseEntity.ok(accountManageService.saveNewAccount(registerRequest));
    }
    @RequestMapping(value = "/changeStatusAccount", method = RequestMethod.POST)
    public boolean changeStatusAccount(@RequestBody ChangeStatusAccountRequest changeStatusAccountRequest) throws Exception {
        return accountManageService.changeStatusAccount(changeStatusAccountRequest);
    }
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public boolean changPassword(@RequestBody ChangePasswordRequest changePasswordRequest) throws Exception {
        return accountManageService.changePassword(changePasswordRequest);
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        JwtResponse jwtResponse=new JwtResponse();
        if (accountManageService.checkUsernameAndPassword(authenticationRequest.getUsername(), authenticationRequest.getPassword())){
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            final UserDetails userDetails = accountManageService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            RoleDto role = accountManageService.getGettingRole(authenticationRequest.getUsername());
            String userId = accountManageService.getAccountId(authenticationRequest.getUsername());
            jwtResponse=new JwtResponse(token,role.getRoleName(),userId);
    }
        return ResponseEntity.ok(jwtResponse);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}