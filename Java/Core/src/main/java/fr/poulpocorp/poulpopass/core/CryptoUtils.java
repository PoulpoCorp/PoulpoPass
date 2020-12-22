package fr.poulpocorp.poulpopass.core;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Utils class for encrypting and decrypting data
 *
 * References:
 *  -https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 *  -https://en.wikipedia.org/wiki/Authenticated_encryption#Authenticated_encryption_with_associated_data_(AEAD)
 *  -https://en.wikipedia.org/wiki/Galois/Counter_Mode
 *  -https://stackoverflow.com/questions/31851612/java-aes-gcm-nopadding-what-is-cipher-getiv-giving-me
 *  -https://stackoverflow.com/questions/7467798/aes-how-to-generate-key-from-password-for-every-algorithm-size
 *  -https://en.wikipedia.org/wiki/Salt_(cryptography)
 *  -https://crypto.stackexchange.com/questions/1776/can-you-help-me-understand-what-a-cryptographic-salt-is
 *  -https://en.wikipedia.org/wiki/Cryptographic_nonce
 *
 * @author PoulpoGaz
 */
public class CryptoUtils {

    private static final int AUTHENTICATION_TAG_BIT_LENGTH = 128;
    private static final int IV_BYTE_LENGTH = 12;
    private static final int SALT_BYTE_LENGTH = 512;

    /**
     * Generate an array of bytes of size {@code length}
     * @param length the length of the array
     * @return random bytes
     */
    public static byte[] getRandomBytes(int length) {
        byte[] bytes = new byte[length];

        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);

        return bytes;
    }

    /**
     * Create an AES key with a password and a salt.
     * The password and the salt are hashed with the
     * PBKDF2WithHmacSHA256 algorithm.
     *
     * @param password the user password
     * @param salt random bytes
     * @return a {@link SecretKey} for the AES algorithm
     * @throws NoSuchAlgorithmException should not append
     * @throws InvalidKeySpecException should not append
     */
    public static SecretKey createAESKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec keySpec = new PBEKeySpec(password, salt, 65536, 256);

        SecretKey key = factory.generateSecret(keySpec);

        return new SecretKeySpec(key.getEncoded(), "AES");
    }

    /**
     * Encrypt the given byte array with the given password
     *
     * @param data the data to be encrypted
     * @param password the password with which the data is encrypted
     * @return the encrypted data
     * @throws InvalidKeyException if the specified password is incorrect
     * @throws IOException when the cipher cannot encrypt
     */
    public static byte[] encrypt(byte[] data, char[] password) throws InvalidKeyException, IOException {
        try {
            if (password == null) {
                throw new InvalidKeyException("Password cannot be null");
            }

            if (data == null) {
                throw new IOException("null data");
            }

            Cipher cipher = Cipher.getInstance("AES_256/GCM/NoPadding");

            byte[] iv = getRandomBytes(IV_BYTE_LENGTH);
            byte[] salt = getRandomBytes(SALT_BYTE_LENGTH);

            SecretKey key = createAESKey(password, salt);

            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(AUTHENTICATION_TAG_BIT_LENGTH, iv));

            byte[] encryptedData = cipher.doFinal(data);

            return concat(iv, salt, encryptedData);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeySpecException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            if (e instanceof AEADBadTagException) {
                throw new InvalidKeyException(e);
            }

            throw new IOException(e);
        } catch (IllegalBlockSizeException e) {
            throw new IOException(e);
        }
    }

    /**
     * Concatenate multiples array of bytes
     *
     * @param arrays arrays to be concatenated
     * @return the concatenated array
     */
    private static byte[] concat(byte[]... arrays) {
        int size = 0;

        for (byte[] arr : arrays) {
            size += arr.length;
        }

        byte[] dest = new byte[size];

        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, dest, pos, array.length);

            pos += array.length;
        }

        return dest;
    }

    /**
     * Decrypt the given byte array with the given password
     *
     * @param data the data to be decrypted
     * @param password the password with which the data is decrypted
     * @return the decrypted data
     * @throws InvalidKeyException if the specified password is incorrect
     * @throws IOException if the data isn't correctly encrypted
     */
    public static byte[] decrypt(byte[] data, char[] password) throws IOException, InvalidKeyException {
        try {
            if (password == null) {
                throw new InvalidKeyException("Password cannot be null");
            }

            if (data == null) {
                throw new IOException("null data");
            }

            if (data.length < IV_BYTE_LENGTH + SALT_BYTE_LENGTH) {
                throw new IOException("Bad length");
            }

            byte[] iv = new byte[IV_BYTE_LENGTH];
            byte[] salt = new byte[SALT_BYTE_LENGTH];
            byte[] encryptedData = new byte[data.length - IV_BYTE_LENGTH - SALT_BYTE_LENGTH];

            System.arraycopy(data, 0, iv, 0, IV_BYTE_LENGTH);
            System.arraycopy(data, IV_BYTE_LENGTH, salt, 0, SALT_BYTE_LENGTH);
            System.arraycopy(data, IV_BYTE_LENGTH + SALT_BYTE_LENGTH, encryptedData, 0, encryptedData.length);

            SecretKey key = createAESKey(password, salt);

            Cipher cipher = Cipher.getInstance("AES_256/GCM/NoPadding");

            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(AUTHENTICATION_TAG_BIT_LENGTH, iv));

            return cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            if (e instanceof AEADBadTagException) {
                throw new InvalidKeyException(e);
            }

            throw new IOException(e);
        } catch (IllegalBlockSizeException e) {
            throw new IOException(e);
        }
    }
}