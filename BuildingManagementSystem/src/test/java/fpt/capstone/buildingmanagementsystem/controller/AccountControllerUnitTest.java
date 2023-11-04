package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ForbiddenError;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.request.ChangePasswordRequest;
import fpt.capstone.buildingmanagementsystem.model.request.ResetPasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import fpt.capstone.buildingmanagementsystem.model.request.LoginRequest;
import fpt.capstone.buildingmanagementsystem.model.response.JwtResponse;
import fpt.capstone.buildingmanagementsystem.security.JwtTokenUtil;
import fpt.capstone.buildingmanagementsystem.service.AccountManageService;
import fpt.capstone.buildingmanagementsystem.service.AuthenticateService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountControllerUnitTest {

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountManageService accountManageService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticateService authenticationManager;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAuthenticationToken_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("tungsec");
        loginRequest.setPassword("123");
//        loginRequest.setUsername("tunghr");
//        loginRequest.setPassword("1234");

        ResponseEntity<?> response = accountController.createAuthenticationToken(loginRequest);

        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(((JwtResponse) response.getBody()).getJwtToken(), jwtResponse.getJwtToken());
        assertEquals("security", jwtResponse.getRole());
        assertEquals("ab0d5969-d2af-49be-9aba-0e288fbf1e09", jwtResponse.getAccountId());
    }

    @Test
    public void testCreateAuthenticationToken_InvalidUsername() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("namnon123");
        loginRequest.setPassword("123");

        NotFound exception = org.junit.jupiter.api.Assertions.assertThrows(NotFound.class,
                () -> accountController.createAuthenticationToken(loginRequest));

        assertEquals("username_not_found", exception.getMessage());
    }

    @Test
    public void testCreateAuthenticationToken_InvalidPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("namnon");
        loginRequest.setPassword("123234234234234234234234");

        BadRequest exception = assertThrows(BadRequest.class, () -> {
            accountController.createAuthenticationToken(loginRequest);
        });

        assertEquals("password_wrong", exception.getMessage());
    }

    @Test
    public void testCreateAuthenticationToken_AccountBlocked() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("namnontay");
        loginRequest.setPassword("123");

        ForbiddenError exception = assertThrows(ForbiddenError.class, () -> {
            accountController.createAuthenticationToken(loginRequest);
        });

        assertEquals("account_blocked", exception.getMessage());
    }

    //Test on server (mailsender fix)
    @Test
    public void testResetPassword_Success() throws Exception {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setUsername("namnon");

        boolean result = accountController.resetPassword(resetPasswordRequest);

        assertTrue(result);
    }

    @Test
    public void testResetPassword_returnNotFound() throws Exception {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setUsername("namnon123");

        NotFound exception = assertThrows(NotFound.class, () -> {
            accountController.resetPassword(resetPasswordRequest);
        });

        assertEquals("user_not_found", exception.getMessage());
    }

    @Test
    public void testResetPassword_returnBadRequest() throws Exception {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setUsername(null);

        BadRequest exception = assertThrows(BadRequest.class, () -> {
            accountController.resetPassword(resetPasswordRequest);
        });

        assertEquals("request_fail", exception.getMessage());
    }

    @Test
    public void testChangePassword_Success() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setAccountId("98cd178c-7ca2-4d75-8463-2187aa8f1462");
        changePasswordRequest.setOldPassword("123");
        changePasswordRequest.setNewPassword("1234");

        boolean result = accountController.changPassword(changePasswordRequest);

        assertTrue(result);
    }

//    @Test
//    public void testAuthenticate_UsernamePasswordValid() throws Exception {
//        // Prepare test data
//        String username = "testUser";
//        String password = "testPassword";
//
//        UserDetails userDetails = new User(username, password, null);
//        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()));
//
//        // Call the method under test
//        accountController.authenticate(username, password);
//
//        // No exception should be thrown
//    }
//
//    @Test
//    public void testAuthenticate_UsernamePasswordDisabledException() {
//        // Prepare test data
//        String username = "testUser";
//        String password = "testPassword";
//
//        UserDetails userDetails = new User(username, password, null);
//        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new DisabledException("USER_DISABLED"));
//
//        // Call the method under test and verify the exception
//        try {
//            accountController.authen(username, password);
//        } catch (Exception e) {
//            assertEquals("USER_DISABLED", e.getMessage());
//        }
//    }
//
//    @Test
//    public void testAuthenticate_UsernamePasswordBadCredentialsException() {
//        // Prepare test data
//        String username = "testUser";
//        String password = "testPassword";
//
//        UserDetails userDetails = new User(username, password, null);
//        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new BadCredentialsException("INVALID_CREDENTIALS"));
//
//        // Call the method under test and verify the exception
//        try {
//            accountController.authenticate(username, password);
//        } catch (Exception e) {
//            assertEquals("INVALID_CREDENTIALS", e.getMessage());
//        }
//    }
}
