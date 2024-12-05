package org.example.auth.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@PropertySource("classpath:local.properties")
public class CryptUtil {

    @Value("${jwt.secret}")
    private static String KEY_ENV_VAR;

    private static final String ALGORITHM = "AES";

    public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        return Base64.getEncoder().encodeToString(encValue);
    }

    public static String decrypt(String encryptedValue) throws Exception {
        byte[] decryptedValue = new byte[0];
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, generateKey());
        byte[] decValue = Base64.getDecoder().decode(encryptedValue);
        decryptedValue = c.doFinal(decValue);
        return new String(decryptedValue);
    }

    private static Key generateKey() {
        return new SecretKeySpec(KEY_ENV_VAR.getBytes(), ALGORITHM);
    }
}
