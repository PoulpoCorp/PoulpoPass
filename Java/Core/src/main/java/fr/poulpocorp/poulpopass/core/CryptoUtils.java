package fr.poulpocorp.poulpopass.core;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * References:
 *  -https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 *  -https://en.wikipedia.org/wiki/Authenticated_encryption#Authenticated_encryption_with_associated_data_(AEAD)
 *  -https://en.wikipedia.org/wiki/Galois/Counter_Mode
 *  -https://stackoverflow.com/questions/31851612/java-aes-gcm-nopadding-what-is-cipher-getiv-giving-me
 *  -https://stackoverflow.com/questions/7467798/aes-how-to-generate-key-from-password-for-every-algorithm-size
 *  -https://en.wikipedia.org/wiki/Salt_(cryptography)
 *  -https://crypto.stackexchange.com/questions/1776/can-you-help-me-understand-what-a-cryptographic-salt-is
 *  -https://en.wikipedia.org/wiki/Cryptographic_nonce
 */

// IV = Initialized vector
public class CryptoUtils {

    private static final int AUTHENTICATION_TAG_BIT_LENGTH = 128;
    private static final int IV_BYTE_LENGTH = 12;
    private static final int SALT_BYTE_LENGTH = 512;

    public static byte[] getRandomBytes(int length) {
        byte[] bytes = new byte[length];

        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);

        return bytes;
    }

    public static SecretKey createAESKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec keySpec = new PBEKeySpec(password, salt, 1024, 256); // password + bytes

        SecretKey key = factory.generateSecret(keySpec);

        return new SecretKeySpec(key.getEncoded(), "AES");
    }

    public static byte[] encrypt(byte[] data, char[] password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            encrypt(data, password, baos);
        } catch (IOException ignored) {}

        return baos.toByteArray();
    }

    public static byte[] decrypt(byte[] data, char[] password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        return decrypt(bais, password);
    }

    public static void encrypt(byte[] data, char[] password, OutputStream os) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidKeySpecException, IOException {
        Cipher cipher = Cipher.getInstance("AES_256/GCM/NoPadding");

        byte[] iv = getRandomBytes(IV_BYTE_LENGTH);
        byte[] salt = getRandomBytes(SALT_BYTE_LENGTH);

        SecretKey key = createAESKey(password, salt);

        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(AUTHENTICATION_TAG_BIT_LENGTH, iv));

        os.write(iv);
        os.write(salt);

        CipherOutputStream cos = new CipherOutputStream(os, cipher);
        cos.write(data);
        cos.close();
    }

    public static byte[] decrypt(InputStream is, char[] password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException, IOException {
        byte[] iv = new byte[IV_BYTE_LENGTH];
        byte[] salt = new byte[SALT_BYTE_LENGTH];

        int i = is.read(iv);
        if (i != IV_BYTE_LENGTH) {
            throw new IOException();
        }

        i = is.read(salt);
        if (i != SALT_BYTE_LENGTH) {
            throw new IOException();
        }

        SecretKey key = createAESKey(password, salt);

        Cipher cipher = Cipher.getInstance("AES_256/GCM/NoPadding");

        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(AUTHENTICATION_TAG_BIT_LENGTH, iv));

        CipherInputStream cis = new CipherInputStream(is, cipher);

        byte[] data = cis.readAllBytes();
        cis.close();

        return data;
    }
}