package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ForbiddenError;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.Status;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.security.PasswordEncode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountManageServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncode passwordEncode;

    @InjectMocks
    private AccountManageService accountManageService;

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setUsername("test");
        account.setPassword("test");
        Status status = new Status();
        status.setStatusId("1");
        account.setStatus(status);
    }

    @Test
    public void testCheckUsernameAndPasswordWhenUsernameAndPasswordAreCorrectThenReturnTrue() {
        when(accountRepository.existsByUsername(account.getUsername())).thenReturn(true);
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));
        when(passwordEncode.passwordEncoder().matches(account.getPassword(), account.getPassword())).thenReturn(true);

        boolean result = accountManageService.checkUsernameAndPassword(account.getUsername(), account.getPassword());

        assertTrue(result);
    }

    @Test
    public void testCheckUsernameAndPasswordWhenUsernameIsIncorrectThenThrowNotFound() {
        when(accountRepository.existsByUsername(account.getUsername())).thenReturn(false);

        assertThrows(NotFound.class, () -> accountManageService.checkUsernameAndPassword(account.getUsername(), account.getPassword()));
    }

    @Test
    public void testCheckUsernameAndPasswordWhenPasswordIsIncorrectThenThrowBadRequest() {
        when(accountRepository.existsByUsername(account.getUsername())).thenReturn(true);
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));
        when(passwordEncode.passwordEncoder().matches("wrongPassword", account.getPassword())).thenReturn(false);

        assertThrows(BadRequest.class, () -> accountManageService.checkUsernameAndPassword(account.getUsername(), "wrongPassword"));
    }

    @Test
    public void testCheckUsernameAndPasswordWhenAccountIsBlockedThenThrowForbiddenError() {
        account.getStatus().setStatusId("2");
        when(accountRepository.existsByUsername(account.getUsername())).thenReturn(true);
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));

        assertThrows(ForbiddenError.class, () -> accountManageService.checkUsernameAndPassword(account.getUsername(), account.getPassword()));
    }
}