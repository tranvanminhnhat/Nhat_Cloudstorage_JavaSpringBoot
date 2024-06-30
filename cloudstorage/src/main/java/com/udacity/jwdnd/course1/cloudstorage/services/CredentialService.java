package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getAllCredentials() {
        return credentialMapper.getAllCredentials();
    }

    public int insertCredential(Credential credential) {
        return credentialMapper.insertCredential(credential);
    }

    public String createKey(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public int deleteCredential(int id) {
        return credentialMapper.deleteCredential(id);
    }

    public boolean existCredential(int id) {
        for (Credential credential : getAllCredentials()) {
            if (credential.getCredentialId() == id){
                return true;
            }
        }
        return false;
    }

    public int updateCredential(Credential credential) {
        return credentialMapper.updateCredential(credential);
    }
}

