package security;

import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for symmetric encryptng and decrypting  byte arrays
 *
 */
public class SymmetricEncryption {

    /**
     * Responsible for persist a symetric key in bytes (bin/key)
     */
    public static void setSymmetricKeyBytes() {
        XStream XMLpersistency = new XStream();
        SecretKey secKey = null;
        secKey = generateSymmetricKey();
        try {
            byte[] keyBytes = KeyBytes.getBytesSecretKey(secKey);
            XMLpersistency.toXML(keyBytes, new FileOutputStream(BIN + PATH_SEPARATOR + KEY));
        } catch (IOException ex) {
            System.err.println(ERROR_MESSAGE + "saving the key file: " + ex.getMessage());
        }
    }

    /**
     * Responsible for getting a symetricKey from byte array to SecretKey object (bin/key)
     * @return
     */
    public static SecretKey getSymmetricKeyBytes() {
        SecretKey secKey = null;
        byte[] keyBytes = null;
        XStream XMLpersistency = new XStream();
        try {
            keyBytes = (byte[]) XMLpersistency.fromXML(new FileInputStream(BIN + PATH_SEPARATOR + KEY));
            secKey = KeyBytes.toSecretKeyFromBytes(keyBytes);
        } catch (FileNotFoundException ex) {
            System.err.println(ERROR_MESSAGE + "reading the file: " + ex.getMessage());

        }
        return secKey;
    }

    /**
     * Responsible for generating a symetric key
     * @return
     */
    public static SecretKey generateSymmetricKey() {
        SecretKey secKey = null;
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            secKey = keygen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
             System.err.println(ERROR_MESSAGE + "generating the symmetric key: " + ex.getMessage());
        }
        return secKey;
    }

    /**
     * Responsible for encrypting a byte array using a symmetric key
     * @param message
     * @param secKey
     * @return
     */
    public static byte[] encryptSymmetric(byte[] message, SecretKey secKey) {
        byte[] res = null;
        try {
            Cipher cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cifrador.init(Cipher.ENCRYPT_MODE, secKey);
            res = cifrador.doFinal(message);
        } catch (Exception ex) {
            System.err.println(ERROR_MESSAGE + "in the symmetric encryption process: " + ex.getMessage());
        }
        return res;
    }

    /**
     * Responsible for decrypting a byte array using a symmetric key
     * @param message
     * @param secKey
     * @return
     */
    public static byte[] decryptSymmetric(byte[] message, SecretKey secKey) {
        byte[] res = null;
        try {
            Cipher cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cifrador.init(Cipher.DECRYPT_MODE, secKey);
            res = cifrador.doFinal(message);
        } catch (Exception ex) {
            System.err.println(ERROR_MESSAGE + "in the symmetric decryption process: " + ex.getMessage());
        }
        return res;
    }
}//end of SymmetricEncryption
