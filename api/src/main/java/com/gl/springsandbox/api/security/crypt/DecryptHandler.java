package com.gl.springsandbox.api.security.crypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

public interface DecryptHandler<T, R> {
    T decrypt(R encrypt) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException;
}
