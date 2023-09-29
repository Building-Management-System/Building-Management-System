package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ForbiddenError;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.AccountMapper;
import fpt.capstone.buildingmanagementsystem.mapper.RoleMapper;
import fpt.capstone.buildingmanagementsystem.model.dto.RoleDto;
import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.Role;
import fpt.capstone.buildingmanagementsystem.model.entity.Status;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.*;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoleRepository;
import fpt.capstone.buildingmanagementsystem.repository.StatusRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.security.PasswordEncode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime;

@Service
public class AccountManageService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncode passwordEncode;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> userAccount = accountRepository.findByUsername(username);
        if (!userAccount.isPresent()) {
            throw new NotFound("user_not_found");
        }
        Optional<Role> role = roleRepository.findByRoleId(userAccount.get().getRole().getRoleId());
        if (!role.isPresent()) {
            throw new NotFound("role_not_found");
        }
        List<GrantedAuthority> grantList = new ArrayList<>();
        grantList.add(new SimpleGrantedAuthority(role.get().getRoleName()));
        return new org.springframework.security.core.userdetails.User(userAccount.get().getUsername(), userAccount.get().getPassword(),
                grantList);
    }

    public boolean saveNewAccount(RegisterRequest registerRequest) {
        try {
            if (registerRequest.getPassword() != null && registerRequest.getUsername() != null) {
                if (!accountRepository.existsByUsername(registerRequest.getUsername())) {
                    Optional<Role> role = roleRepository.findByRoleName(registerRequest.getRole());
                    if (role.isPresent()) {
                        Optional<Status> status = statusRepository.findByStatusId("1");
                        Account newAccount = accountMapper.convertRegisterAccount(registerRequest, status.get(), role.get());
                        accountRepository.save(newAccount);
                        User user= User.builder().city("unknown").country("unknown").email("unknown").first_name("unknown")
                                .last_name("unknown").date_of_birth("unknown").telephone_number("unknown").gender("unknown").createdDate(
                                        generateRealTime()).updatedDate(generateRealTime()).account(newAccount).build();
                        userRepository.save(user);
                        return true;
                    } else {
                        throw new NotFound("role_name_not_found");
                    }
                } else {
                    throw new BadRequest("username_exist");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }

    public boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            if (changePasswordRequest.getAccountId() != null && changePasswordRequest.getOldPassword() != null && changePasswordRequest.getNewPassword() != null) {
                Optional<Account> userAccount = accountRepository.findByAccountId(changePasswordRequest.getAccountId());
                if (!userAccount.isPresent()) {
                    throw new NotFound("user_not_found");
                }
                String username = accountRepository.findById(changePasswordRequest.getAccountId()).get().getUsername();
                if (checkUsernameAndPassword(username, changePasswordRequest.getOldPassword())) {
                    String newPassword = passwordEncode.passwordEncoder().encode(changePasswordRequest.getNewPassword());
                    accountRepository.updatePassword(newPassword, generateRealTime(), username);
                    return true;
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
        return false;
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        try {
            if (accountRepository.existsByUsername(username)) {
                String passwordOld = accountRepository.findByUsername(username).get().getPassword();
                if (Objects.equals(accountRepository.findByUsername(username).get().getStatus().getStatusId(), "1")) {
                    if (passwordEncode.passwordEncoder().matches(password, passwordOld)) {
                        return true;
                    }
                } else {
                    throw new ForbiddenError("account_blocked");
                }
            } else {
                throw new NotFound("username_not_found");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
        throw new BadRequest("password_wrong");
    }
    public boolean changeStatusAccount(ChangeStatusAccountRequest changeStatusAccountRequest) {
        try {
            String accountId=changeStatusAccountRequest.getAccountId();
            if (accountId != null && changeStatusAccountRequest.getStatusName() != null) {
                if (!accountRepository.existsById(changeStatusAccountRequest.getAccountId())) {
                    throw new NotFound("user_not_found");
                }
                String oldStatus = accountRepository.findByAccountId(accountId).get().getStatus().getStatusName();
                if (!oldStatus.equals(changeStatusAccountRequest.getStatusName())) {
                    Optional<Status> status = statusRepository.findByStatusName(changeStatusAccountRequest.getStatusName());
                    if (!status.isPresent()) {
                        throw new NotFound("status_not_found");
                    }
                    accountRepository.updateStatusAccount(status.get().statusId,accountId);
                    return true;
                }
                else{
                    throw new BadRequest("new_status_existed");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }
    public boolean changeRoleAccount(ChangeRoleRequest changeRoleRequest) {
        try {
            String accountId=changeRoleRequest.getAccountId();
            if (accountId != null && changeRoleRequest.getRoleName() != null) {
                if (!accountRepository.existsById(changeRoleRequest.getAccountId())) {
                    throw new NotFound("user_not_found");
                }
                String oldRole = accountRepository.findByAccountId(accountId).get().getRole().getRoleName();
                if (!oldRole.equals(changeRoleRequest.getRoleName())) {
                    Optional<Role> role = roleRepository.findByRoleName(changeRoleRequest.getRoleName());
                    if (!role.isPresent()) {
                        throw new NotFound("role_not_found");
                    }
                    String newRoleId = role.get().getRoleId();
                    accountRepository.updateRoleAccount(newRoleId,accountId);
                    return true;
                }
                else{
                    throw new BadRequest("new_role_existed");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }
    public boolean resetPassword(ResetPasswordRequest resetPassword) {
        try {
            String username = resetPassword.getUsername();
            if (username != null) {
                if (!accountRepository.existsByUsername(resetPassword.getUsername())) {
                    throw new NotFound("user_not_found");
                }
                String newPassword = passwordEncode.passwordEncoder().encode("123aA@");
                accountRepository.updatePassword(newPassword, generateRealTime(), username);
                return true;
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }
    public RoleDto getGettingRole(String username) {
        Optional<Account> userAccount = accountRepository.findByUsername(username);
        Optional<Role> role = roleRepository.findByRoleId(userAccount.get().getRole().getRoleId());
        return roleMapper.convertRegisterAccount(role.get());
    }
    public String getAccountId(String username) {
        ;
        Optional<Account> userAccount = accountRepository.findByUsername(username);
        return userAccount.get().getAccountId();
    }
}
