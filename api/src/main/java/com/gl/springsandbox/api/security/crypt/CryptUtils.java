package com.gl.springsandbox.api.security.crypt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.*;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Component
public class CryptUtils {

    // 단 방향만 지원 (복호화만)
    private static byte[] keyBinary;

    @Value("${crypt.key}") //
    private void setKeyBinary(String binary) {
        keyBinary = binary.getBytes(StandardCharsets.UTF_8);
    }

    enum CRYPT_TYPE {
        RSA,
        BLOWFISH
    }

    private static Cipher getInstance(String type) throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(type);
    }

    public static String encrypt(String raw, String cryptType) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getInstance(cryptType);
        cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance(cryptType).generatePublic(getDecryptKey(cryptType)));
        return new String(cipher.doFinal(raw.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(String encryptedString, String cryptType) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getInstance(cryptType);
        cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance(cryptType).generatePrivate(getDecryptKey(cryptType)));
        return new String(cipher.doFinal(encryptedString.getBytes(StandardCharsets.UTF_8)));
    }

    private static EncodedKeySpec getEncryptKey(String type) {
        return new X509EncodedKeySpec(Base64.getDecoder().decode(keyBinary), type);
    }

    private static EncodedKeySpec getDecryptKey(String type) {
        return new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyBinary), type);
    }
}
