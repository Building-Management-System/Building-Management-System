package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ForbiddenError;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.AccountMapper;
import fpt.capstone.buildingmanagementsystem.mapper.RoleMapper;
import fpt.capstone.buildingmanagementsystem.model.dto.RoleDto;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.request.*;
import fpt.capstone.buildingmanagementsystem.model.response.GetAllAccountResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.security.PasswordEncode;
import fpt.capstone.buildingmanagementsystem.until.EmailSender;
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
import static fpt.capstone.buildingmanagementsystem.until.Until.getRandomString;

@Service
public class AccountManageService implements UserDetailsService {
    @Autowired
    DailyLogRepository dailyLogRepository;
    @Autowired
    OverTimeRepository overTimeRepository;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    RequestTicketRepository requestTicketRepository;
    @Autowired
    RequestMessageRepository requestMessageRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    EmailSender emailSender;
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
                    Optional<Department> department = departmentRepository.findByDepartmentName(registerRequest.getDepartmentName());
                    if (role.isPresent() && department.isPresent()) {
                        Optional<Status> status = statusRepository.findByStatusId("1");
                        Account newAccount = accountMapper.convertRegisterAccount(registerRequest, status.get(), role.get());
                        User user = User.builder().city("unknown").country("unknown").email("unknown").firstName("unknown")
                                .lastName("unknown").dateOfBirth("unknown").telephoneNumber("unknown").gender("unknown").createdDate(
                                        generateRealTime()).image("unknown").updatedDate(generateRealTime()).account(newAccount).department(department.get())
                                .build();
                        if (Objects.equals(registerRequest.getRole(), "manager")) {
                            if (checkManagerOfDepartment(registerRequest.getDepartmentName())) {
                                accountRepository.save(newAccount);
                                userRepository.save(user);
                            } else {
                                throw new BadRequest("department_exist_manager");
                            }
                        }else {
                            accountRepository.save(newAccount);
                            userRepository.save(user);
                        }
                        return true;
                    } else {
                        throw new NotFound("not_found");
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
            String accountId = changeStatusAccountRequest.getAccountId();
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
                    accountRepository.updateStatusAccount(status.get().statusId, accountId);
                    return true;
                } else {
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
            String accountId = changeRoleRequest.getAccountId();
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
                    Department department = userRepository.findByUserId(changeRoleRequest.accountId).get().getDepartment();
                    if (Objects.equals(changeRoleRequest.getRoleName(), "manager")) {
                        if (checkManagerOfDepartment(department.getDepartmentName())) {
                            accountRepository.updateRoleAccount(newRoleId, accountId);
                            return true;
                        } else {
                            throw new BadRequest("department_exist_manager");
                        }
                    }
                    accountRepository.updateRoleAccount(newRoleId, accountId);
                    return true;
                } else {
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
                String newPassword = getRandomString(8);
                String toEmail = accountRepository.findByUsername(username).get().getUser().getEmail();
                String newPasswordEncode = passwordEncode.passwordEncoder().encode(newPassword);
                accountRepository.updatePassword(newPasswordEncode, generateRealTime(), username);
                emailSender.setMailSender(toEmail, "[Notification] - Password has been successfully reset!", "Your newly reset password is: " + newPassword);
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
    public boolean deleteAccount(String username) {
        if(username!=null) {
            Optional<Account> userAccount = accountRepository.findByUsername(username);
            if (userAccount.isPresent()) {
                User user = userAccount.get().getUser();
                user.setAccount(null);
                List<RequestTicket> checkpoint1 = requestTicketRepository.findAllByUser(user);
                List<RequestMessage> checkpoint2 = requestMessageRepository.findAllBySender(user);
                List<RequestMessage> checkpoint3 = requestMessageRepository.findAllByReceiver(user);
                List<OvertimeLog> checkpoint4 = overTimeRepository.findAllByUser(user);
                List<ChatMessage> checkpoint5 = chatMessageRepository.findAllBySender(user);
                List<ChatMessage> checkpoint6 = chatMessageRepository.findAllByReceiver(user);
                List<DailyLog> checkpoint7 = dailyLogRepository.findAllByUser(user);
                if (checkpoint1.size() == 0 &&
                        checkpoint2.size() == 0
                        && checkpoint3.size() == 0
                        && checkpoint4.size() == 0
                        && checkpoint5.size() == 0
                        && checkpoint6.size() == 0
                        && checkpoint7.size() == 0) {
                    accountRepository.delete(userAccount.get());
                    return true;
                } else {
                    throw new ServerError("can_not_delete");
                }
            } else {
                throw new NotFound("username_not_found");
            }
        } else {
            throw new BadRequest("username_is_null");
        }
    }

    public RoleDto getGettingRole2(GetUserInfoRequest getUserInfoRequest) {
        if (getUserInfoRequest.getUserId() != null) {
            Optional<Account> userAccount = accountRepository.findByAccountId(getUserInfoRequest.getUserId());
            if (userAccount.isPresent()) {
                Optional<Role> role = roleRepository.findByRoleId(userAccount.get().getRole().getRoleId());
                return roleMapper.convertRegisterAccount(role.get());
            } else {
                throw new NotFound("user_not_found");
            }
        } else {
            throw new BadRequest("request_fail");
        }
    }

    public boolean checkManagerOfDepartment(String departmentName) {
        Optional<Department> department = departmentRepository.findByDepartmentName(departmentName);
        List<User> userOfDepartment = userRepository.findAllByDepartment(department.get());
        boolean checkPoint = true;
        for (int i = 0; i < userOfDepartment.size(); i++) {
            Optional<Account> account = accountRepository.findByAccountId(userOfDepartment.get(i).getUserId());
            if (Objects.equals(account.get().getRole().getRoleId(), "3")) {
                checkPoint = false;
            }
        }
        return checkPoint;
    }

    public List<GetAllAccountResponse> getGetAllAccount() {
        List<Account> account = accountRepository.findAll();
        List<GetAllAccountResponse> getAllAccountResponses = new ArrayList<>();
        if (account.size() == 0) {
            return getAllAccountResponses;
        }
        account.forEach(element -> getAllAccountResponses.add(accountMapper.convertGetAllAccount(element)));
        return getAllAccountResponses;
    }

    public String getAccountId(String username) {
        Optional<Account> userAccount = accountRepository.findByUsername(username);
        return userAccount.get().getAccountId();
    }
}
