package com.idda.project.card_service.service;


public interface EncryptionService {
    String encrypt(String data);

    String decrypt(String encryptedData);
}
