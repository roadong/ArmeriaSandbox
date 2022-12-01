package com.gl.springsandbox.api.security.crypt;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

abstract class DefaultEncryption implements CryptHandler<String, String> {

    protected String convertString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    protected InputStream loadKeyStream(String location) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        return resourceLoader.getResource(location).getInputStream();
    }

    protected abstract String getAlgorithmName();

    public abstract String encrypt(String origin) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException;

    public abstract String decrypt(String encrypt) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException;

}
