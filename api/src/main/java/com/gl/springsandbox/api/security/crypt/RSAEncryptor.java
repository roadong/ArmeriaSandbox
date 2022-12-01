package com.gl.springsandbox.api.security.crypt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.ResourceLoaderClassLoadHelper;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
class RSAEncryptor extends DefaultEncryption {

    // 서버는 복호화, 클라이언트는 암호화 하나만 필요
    private Key rsaKey;

    private Cipher cipher;

    public RSAEncryptor(boolean isServer) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        cipher = Cipher.getInstance(getAlgorithmName());
        rsaKey = isServer ? loadPrivateKey() : loadPublicKey();
    }

    private Key loadPrivateKey() {
        try(InputStream stream = loadKeyStream("private.der")) {
            byte[] bytes = stream.readAllBytes();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            return KeyFactory.getInstance(getAlgorithmName()).generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("### 키를 불러오는 도중 오류가 발생하였습니다", e);
            throw new RuntimeException(e);
        }
    }

    private Key loadPublicKey() {
        try(InputStream stream = loadKeyStream("public.key")) {
            String keyString = convertString(stream.readAllBytes());
            //본문 스크립트만 필요하기 때문에 BEGIN, END 스크립트 문자는 삭제 및 개행문자 정리
            String bodyKey = keyString.replaceAll("-{5}[ a-zA-Z]*-{5}", "").replaceAll("\\n", "");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(bodyKey));
            return KeyFactory.getInstance(getAlgorithmName()).generatePublic(keySpec);
        } catch (Exception e) {
            log.error("### 키를 불러오는 도중 오류가 발생하였습니다", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getAlgorithmName() {
        return "RSA";
    }

    @Override
    public String encrypt(String origin) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if (rsaKey instanceof PrivateKey) throw new UnsupportedEncodingException("### 지원되지 않는 키입니다");
        cipher.init(Cipher.ENCRYPT_MODE, rsaKey);
        // RSA encrypt -> base64 encode
        byte[] encryptedBytes = cipher.doFinal(origin.getBytes(StandardCharsets.UTF_8));
        return convertString(Base64.getEncoder().encode(encryptedBytes));
    }

    @Override
    public String decrypt(String encrypt) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if(rsaKey instanceof PublicKey) throw new UnsupportedEncodingException("### 지원되지 않는 키입니다");
        cipher.init(Cipher.DECRYPT_MODE, rsaKey);
        // base64 decode -> RSA decrypt
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypt));
        return convertString(decryptedBytes);
    }
}
