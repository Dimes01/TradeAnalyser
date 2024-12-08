package org.example.utilities;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@PropertySource("classpath:local.properties")
public class CryptUtil {

    @Value("${jwt.secret}")
    private static String KEY_ENV_VAR;
    private static final String ALGORITHM = "AES";

    public static String encrypt(String valueToEnc) throws RuntimeException {
        Key key = generateKey();
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            return Base64.getEncoder().encodeToString(encValue);
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm for encrypting");
        } catch (NoSuchPaddingException e) {
            log.error("No such padding for encrypting");
        } catch (IllegalBlockSizeException e) {
            log.error("Illegal block size for encrypting");
        } catch (BadPaddingException e) {
            log.error("Bad padding for encrypting");
        } catch (InvalidKeyException e) {
            log.error("Invalid key for encrypting");
        }
        throw new RuntimeException();
    }

    public static String decrypt(String encryptedValue) throws RuntimeException {
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, generateKey());
            byte[] decValue = Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedValue = c.doFinal(decValue);
            return new String(decryptedValue);
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm for decrypting");
        } catch (NoSuchPaddingException e) {
            log.error("No such padding for decrypting");
        } catch (IllegalBlockSizeException e) {
            log.error("Illegal block size for decrypting");
        } catch (BadPaddingException e) {
            log.error("Bad padding for decrypting");
        } catch (InvalidKeyException e) {
            log.error("Invalid key for decrypting");
        }
        throw new RuntimeException();
    }


    private static Key generateKey() {
        return new SecretKeySpec(KEY_ENV_VAR.getBytes(), ALGORITHM);
    }
}
