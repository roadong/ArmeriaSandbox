package com.gl.springsandbox.api;


import com.gl.springsandbox.api.security.crypt.CryptHandler;
import com.gl.springsandbox.api.security.crypt.CryptUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CryptTest {

    private CryptHandler<String, String> encryptHandler;
    private CryptHandler<String, String> decryptHandler;

    private final String encryptString = "ifW2fnkVS2Hnz3olK2UhGIoF44yWHJApgvfgpgxVQ9GFWQr/VoJe63+hmpNGR+aTYdNNLu7uS+2PhAnu1XrwmMWDBElvSiy02xzzgiGwlLkjeQrV1Vjhle9g9K2pvk0kuwBD3zM22sNk+sAzNxSH75znKeh0qUg7qeUWxGBWSbU=";

    @BeforeEach
    public void setup() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.encryptHandler = CryptUtils.selectCrypt("encrypt-RSA");
        this.decryptHandler = CryptUtils.selectCrypt("decrypt-RSA");
    }

    @Test
    public void decryptTest() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        String decrypt = decryptHandler.decrypt(encryptString);
        Assertions.assertEquals("username:password", decrypt);
    }

    @Test
    public void encryptTest() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        String encrypt = encryptHandler.encrypt("username:password");
        Assertions.assertEquals("username:password", decryptHandler.decrypt(encrypt));
    }
}
