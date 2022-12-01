package com.gl.springsandbox.api.security.crypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

public interface CryptHandler<T, R> extends DecryptHandler<T, R>, EncryptHandler<R, T> {
    R encrypt(T origin) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException;

    T decrypt(R encrypt) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException;
}
