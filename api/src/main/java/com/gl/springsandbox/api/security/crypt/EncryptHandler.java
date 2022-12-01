package com.gl.springsandbox.api.security.crypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

public interface EncryptHandler<R, T> {
    R encrypt(T origin) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException;
}
