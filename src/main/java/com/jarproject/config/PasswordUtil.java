package com.jarproject.config;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil{
    // Workload factor (10-31), 12 là giá trị an toàn mặc định
    private static final int WORKLOAD = 12;

    /**
     * Hash password với BCrypt
     * @param plainPassword Password gốc
     * @return Hashed password
     */
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Verify password với stored hash
     * @param plainPassword Password cần kiểm tra
     * @param hashedPassword Hash đã lưu trong database
     * @return true nếu password đúng
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hash format");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
