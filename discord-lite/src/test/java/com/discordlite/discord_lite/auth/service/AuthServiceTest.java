package com.discordlite.discord_lite.auth.service;

import com.discordlite.discord_lite.auth.dto.LoginRequest;
import com.discordlite.discord_lite.auth.dto.LoginResponse;
import com.discordlite.discord_lite.auth.dto.RegisterRequest;
import com.discordlite.discord_lite.auth.dto.VerifyEmailRequest;
import com.discordlite.discord_lite.exception.ErrorCode;
import com.discordlite.discord_lite.exception.newException.ApiException;
import com.discordlite.discord_lite.security.jwt.JwtService;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.repository.UserRepository;
import lombok.extern.java.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// 1. Kích hoạt môi trường Mockito cho JUnit 5
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final org.apache.commons.logging.Log log = LogFactory.getLog(AuthServiceTest.class);
    // 2. Tạo các "Chim mồi" (Hàng giả) cho các Dependency
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailVerificationService emailVerificationService;

    // 3. Tiêm tất cả các "Chim mồi" trên vào class thật cần test
    @InjectMocks
    private AuthService authService;

    // ... (Các kịch bản Test sẽ viết ở bên dưới)
    @Test
    void register_UsernameAlreadyExists_ThrowsRuntimeException() {
        // 1. ARRANGE (Chuẩn bị kịch bản)
        // Tạo một request với username là "hacker"
        RegisterRequest request = new RegisterRequest("hacker", "test@mail.com", "Hacker", "123456");

        // Dạy con "Chim mồi" UserRepository: "Hễ ai hỏi username 'hacker' có tồn tại không, hãy gật đầu (trả về true)"
        when(userRepository.existsByUsername("hacker")).thenReturn(true);

        // 2 & 3. ACT & ASSERT (Hành động & Kiểm chứng lỗi)
        // Khi gọi hàm register, mình KỲ VỌNG nó sẽ nổ ra lỗi RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });

        // Kiểm tra xem câu chửi của hệ thống có đúng như mình dặn không
        assertEquals("Username already exist", exception.getMessage());

        // Cực kỳ quan trọng: Đảm bảo rằng hàm save() của DB KHÔNG BAO GIỜ ĐƯỢC CHẠY (bảo vệ DB)
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_ValidRequest_SavesUserSuccessfully() {
        // 1. ARRANGE
        RegisterRequest request = new RegisterRequest("newuser", "new@mail.com", "raw_password", "Newbie");

        // Dạy "Chim mồi" DB: Chưa có ai dùng username và email này cả
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(false);

        // Dạy "Chim mồi" PasswordEncoder: "Khi nhận chuỗi 'raw_password', hãy biến nó thành 'encoded_xyz'"
        when(passwordEncoder.encode("raw_password")).thenReturn("encoded_xyz");

        // 2. ACT (Tiến hành đăng ký)
        authService.register(request);

        // 3. ASSERT (Kiểm chứng kết quả lưu)
        // Dùng ArgumentCaptor như một cái "vợt" để chặn bắt đối tượng User vừa bị ném vào hàm save()
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        // Mở cái vợt ra kiểm tra xem dữ liệu nhồi bên trong có chuẩn không
        User savedUser = userCaptor.getValue();
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("new@mail.com", savedUser.getEmail());
        // Chỗ này ăn tiền nhất: Phải đảm bảo mật khẩu đem đi lưu là mật khẩu ĐÃ MÃ HÓA
        assertEquals("encoded_xyz", savedUser.getPassword());
        assertFalse(savedUser.isEmailVerified()); // Mặc định email chưa verify
    }

    @Test
    void register_EmailAlreadyExist_ReturnRuntimeException() {
        //Arrange
        RegisterRequest request = new RegisterRequest("hacker", "hacker@email.com", "123456", "hacker");
        when(userRepository.existsByEmail("hacker@email.com")).thenReturn(true);

        //Act
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
             authService.register(request);
        });

        //Assert
        verify(userRepository, never()).save(any(User.class));
        assertEquals("Email already exist", runtimeException.getMessage());
    }

    @Test
    void login_PasswordNotMatch_ThrowsApiExceptionAndNotGenerateToken() {
        //Arrange
        LoginRequest request = new LoginRequest("hacker", "123456");
        User user = new User();
        user.setUsername("hacker");
        user.setPassword("12334567");
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);
        when(userRepository.findByUsername("hacker")).thenReturn(Optional.of(user));
        //Act
        ApiException apiException = assertThrows(ApiException.class, () -> {
           authService.login(request);
        });

        //Assert
        verify(jwtService, never()).generateToken(anyLong(), anyString());
        assertEquals(ErrorCode.INVALID_PASSWORD, apiException.getErrorCode());
    }

    @Test
    void login_UserDisabled_ReturnApiException() {
        //Arrange
        LoginRequest request = new LoginRequest("hacker", "123456");
        User user = new User();
        user.setEnabled(false);
        user.setPassword("encoded_things");
        user.setEmailVerified(false);
        when(userRepository.findByUsername("hacker")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "encoded_things")).thenReturn(true);

        //Act
        ApiException apiException = assertThrows(ApiException.class, () -> {
            authService.login(request);
        });

        assertEquals(ErrorCode.USER_DISABLED, apiException.getErrorCode());
        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    void login_EmailNotVerified_ReturnApiException() {
        //Arrange
        LoginRequest request = new LoginRequest("hacker", "123456");
        User user = new User();
        user.setEnabled(true);
        user.setPassword(request.password());
        when(userRepository.findByUsername("hacker")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);

        //Act
        ApiException apiException = assertThrows(ApiException.class, () -> {
            authService.login(request);
        });

        assertEquals(ErrorCode.EMAIL_NOT_VERIFIED, apiException.getErrorCode());
    }

    @Test
    void login_ValidCredentials_ReturnsJwtToken() {
        // 1. ARRANGE
        LoginRequest request = new LoginRequest("testuser", "correct_password");

        // Xây dựng một đối tượng User "giả" trong DB (đã active, đã verify)
        User mockDbUser = new User();
        mockDbUser.setUserId(99L);
        mockDbUser.setUsername("testuser");
        mockDbUser.setPassword("encoded_password_in_db");
        mockDbUser.setEnabled(true);
        mockDbUser.setEmailVerified(true);

        // Kịch bản 1: Tìm thấy User
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockDbUser));

        // Kịch bản 2: Password gõ vào khớp với Password mã hóa trong DB
        when(passwordEncoder.matches("correct_password", "encoded_password_in_db")).thenReturn(true);

        // Kịch bản 3: Sinh ra JWT token ảo
        when(jwtService.generateToken(99L, "testuser")).thenReturn("fake-jwt-token-xyz");

        // 2. ACT
        LoginResponse response = authService.login(request);

        // 3. ASSERT
        assertNotNull(response);
        assertEquals("fake-jwt-token-xyz", response.token());

        // Xác nhận xem các "Chim mồi" có bị gọi ra làm việc thực sự không
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("correct_password", "encoded_password_in_db");
    }

    @Test
    void verifyEmail_ValidRequest_UpdateUserAndSave() {
        //Arrange
        VerifyEmailRequest request = new VerifyEmailRequest("user_random", "123456");

        User mockUser = new User();
        mockUser.setUsername("user_random");
        mockUser.setEmailVerified(false);
        when(userRepository.findByUsername("user_random")).thenReturn(Optional.of(mockUser));

        //Act
        authService.verifyEmail(request);

        //Assert
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();
        assertTrue(savedUser.isEmailVerified());
    }
}