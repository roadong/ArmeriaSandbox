package com.gl.springsandbox.api.security.crypt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Component
public class CryptUtils {

    public static CryptHandler<String, String> selectCrypt(String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return switch (algorithm) {
            case "encrypt-RSA" -> new RSAEncryptor(false);
            case "decrypt-RSA" -> new RSAEncryptor(true);
            default -> throw new UnsupportedEncodingException("### 지원 되지 않은 암복호화 알고리즘 입니다");
        };
    }
}
