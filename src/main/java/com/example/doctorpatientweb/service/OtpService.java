package com.example.doctorpatientweb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final StringRedisTemplate redisTemplate;

    @Value("${otp.redis.prefix:OTP:}")
    private String otpPrefix;

    @Value("${otp.expiration.minutes:5}")
    private long otpExpiryMinutes;

    public OtpService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Save OTP in Redis
    public void saveOtp(String username, String otp) {
        String key = otpPrefix + username;
        redisTemplate.opsForValue().set(key, otp, otpExpiryMinutes, TimeUnit.MINUTES);
    }

    // Validate OTP and delete after success
    public boolean validateOtp(String username, String otp) {
        String key = otpPrefix + username;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(key); // remove OTP after success
            return true;
        }
        return false;
    }

    // Check if OTP exists
    public boolean hasOtp(String username) {
        String key = otpPrefix + username;
        Boolean exists = redisTemplate.hasKey(key);
        return exists != null && exists;
    }

    // Delete OTP manually
    public void deleteOtp(String username) {
        String key = otpPrefix + username;
        redisTemplate.delete(key);
    }

    // Get OTP value (for testing or debugging)
    public String getOtp(String username) {
        String key = otpPrefix + username;
        return redisTemplate.opsForValue().get(key);
    }
}
