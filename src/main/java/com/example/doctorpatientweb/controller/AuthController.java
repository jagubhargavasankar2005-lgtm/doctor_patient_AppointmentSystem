package com.example.doctorpatientweb.controller;

import com.example.doctorpatientweb.dto.*;
import com.example.doctorpatientweb.entity.Role;
import com.example.doctorpatientweb.entity.User;
import com.example.doctorpatientweb.repository.OTPRepository;
import com.example.doctorpatientweb.repository.UserRepository;
import com.example.doctorpatientweb.security.CustomUserDetailsService;
import com.example.doctorpatientweb.security.JwtUtil;
import com.example.doctorpatientweb.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private OtpService otpService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    // ===== Signup Endpoint =====
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new SignupResponse("ERROR", "Username already exists."));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new SignupResponse("ERROR", "Email already exists."));
        }

        // ✅ Store user details temporarily in Redis (not DB yet)
        String otpCode = String.format("%06d", new Random().nextInt(999999));
        otpService.saveOtp(user.getUsername(), otpCode);

        // Also save user details temporarily in Redis with prefix "USER:"
        redisTemplate.opsForHash().put("USER:" + user.getUsername(), "username", user.getUsername());
        redisTemplate.opsForHash().put("USER:" + user.getUsername(), "password", passwordEncoder.encode(user.getPassword()));
        redisTemplate.opsForHash().put("USER:" + user.getUsername(), "email", user.getEmail());
        redisTemplate.opsForHash().put("USER:" + user.getUsername(), "role", user.getRole().toString());
        // Send OTP email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your Signup OTP");
        message.setText("Your OTP is: " + otpCode);
        mailSender.send(message);

        return ResponseEntity.ok(new SignupResponse("OTP_SENT", "OTP sent to email. Verify to complete signup."));
    }


    // ===== Verify OTP Endpoint =====
    @PostMapping("/verify-otp")
    public ResponseEntity<SignupResponse> verifyOtp(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String otp = request.get("otp");

        // 1️⃣ Validate OTP from Redis
        if (!otpService.validateOtp(username, otp)) {
            return ResponseEntity.badRequest()
                    .body(new SignupResponse("ERROR", "Invalid or expired OTP."));
        }

        // 2️⃣ Fetch user details stored temporarily in Redis
        Map<Object, Object> userMap = redisTemplate.opsForHash().entries("USER:" + username);

        if (userMap.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new SignupResponse("ERROR", "User data expired. Please signup again."));
        }

        // 3️⃣ Build User object from Redis data
        User user = new User();
        user.setUsername((String) userMap.get("username"));
        user.setPassword((String) userMap.get("password"));
        user.setEmail((String) userMap.get("email"));

        // Convert role String back to Role enum
        String roleStr = (String) userMap.get("role");
        user.setRole(Role.valueOf(roleStr));

        user.setEnabled(true);

        // 4️⃣ Save user to database
        userRepository.save(user);

        // 5️⃣ Clean up temporary data from Redis
        redisTemplate.delete("USER:" + username);

        return ResponseEntity.ok(
                new SignupResponse("SUCCESS", "User registered successfully!")
        );
    }


    // ===== Login Endpoint =====
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        // Load user from DB first
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Check if user is enabled
        if (!user.isEnabled()) {
            return ResponseEntity.status(401)
                    .body("User is not verified or enabled yet.");
        }

        // Authenticate username/password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (Exception ex) {
            return ResponseEntity.status(401)
                    .body("Invalid username or password.");
        }

        // Generate JWT
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        long expiryTime = jwtUtil.extractExpiration(jwt).getTime();

        return ResponseEntity.ok(new LoginResponse(jwt, expiryTime, "Bearer"));
    }


    // ===== Test Mail Endpoint =====
    @PostMapping("/test-mail")
    public ResponseEntity<MailResponse> sendTestMail(@RequestBody MailRequest mailRequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mailRequest.getTo());
            message.setSubject(mailRequest.getSubject());
            message.setText(mailRequest.getBody());
            mailSender.send(message);

            return ResponseEntity.ok(new MailResponse("SUCCESS", "Email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new MailResponse("ERROR", "Failed to send email: " + e.getMessage()));
        }
    }
}
