//package com.idda.project.card_service.service.impl;
//
//import com.idda.project.card_service.service.EncryptionService;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Base64;
//
//@Service
//public class EncrytionHardcodedImpl implements EncryptionService {
//    private static final String ALGORITHM = "AES";
//    private final Key secretKey;
//
//    // Konstruktoru dəyişirik. Artıq @Value parametrini almır.
//    public EncrytionHardcodedImpl() {
//        // ---- TEST ÜÇÜN DƏYİŞİKLİK ----
//        // Açar dəyərini birbaşa kodun içində təyin edirik.
//        // Bu, application.properties faylından asılılığı aradan qaldırır.
//        String hardcodedSecretKey = "pW3n$Z@9sQ!aF7jK"; // 16 simvol
//        // --------------------------------
//
//        if (hardcodedSecretKey.length() != 16 && hardcodedSecretKey.length() != 24 && hardcodedSecretKey.length() != 32) {
//            throw new IllegalArgumentException("Invalid hardcoded AES key length: must be 16, 24, or 32 bytes.");
//        }
//
//        byte[] keyBytes = hardcodedSecretKey.getBytes(StandardCharsets.UTF_8);
//        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
//    }
//
//    @Override
//    public String encrypt(String data) {
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
//            return Base64.getEncoder().encodeToString(encryptedBytes);
//        } catch (Exception e) {
//            throw new RuntimeException("Error occurred while encrypting data", e);
//        }
//    }
//
//    @Override
//    public String decrypt(String encryptedData) {
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            byte[] originalBytes = Base64.getDecoder().decode(encryptedData);
//            byte[] decryptedBytes = cipher.doFinal(originalBytes);
//            return new String(decryptedBytes, StandardCharsets.UTF_8);
//        } catch (Exception e) {
//            throw new RuntimeException("Error occurred while decrypting data", e);
//        }
//    }
//}
