package security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.crypto.SecretKey;
import static com.losandes.utils.Constants.*;

/**
 * Responsible for transforming symmetric key objects into byte arrays and vice versa
 * @author Eduardo Rosales
 */
public class KeyBytes {

    /**
     * Responsible for transforming a symmetric key into a byte array
     * @param obj
     * @return
     */
    public static byte[] getBytesSecretKey(SecretKey obj) {
        ObjectOutputStream oos = null;
        byte[] data;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            data = bos.toByteArray();
        } catch (IOException ex) {
            System.err.println(ERROR_MESSAGE+"transforming a symmetric key into a byte array: " + ex.getMessage());
            return null;
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                System.err.println(ERROR_MESSAGE+"transforming a symmetric key into a byte array: " + ex.getMessage());
            }
        }
        return data;
    }

    /**
     * Responsible for transforming a byte array to a symmetric key
     * @param bytes
     * @return
     */
    public static SecretKey toSecretKeyFromBytes(byte[] bytes) {
        SecretKey key = null;

        try {
            key = (SecretKey) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        } catch (Exception ex) {
            System.err.println(ERROR_MESSAGE+"transforming a byte array to symmetric key: " + ex.getMessage());
        }
        return key;
    }
}//end of KeyBytes
