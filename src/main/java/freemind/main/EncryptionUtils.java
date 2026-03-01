/*
 * FreeMind - a program for creating and viewing mindmaps
 * Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * See COPYING for details
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Utility class for encryption, compression, and Base64 encoding/decoding.
 * Extracted from {@link Tools} to improve cohesion.
 */
public final class EncryptionUtils {

    private EncryptionUtils() {
        // utility class
    }

    // ---- Base64 ----

    public static String toBase64(byte[] byteBuffer) {
        return Base64.getEncoder().encodeToString(byteBuffer);
    }

    /**
     * Method to be called from XSLT
     */
    public static String toBase64(String text) {
        return toBase64(text.getBytes());
    }

    public static byte[] fromBase64(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    // ---- Compression ----

    public static String compress(String message) {
        byte[] input = message.getBytes(StandardCharsets.UTF_8);
        // Create the compressor with the highest level of compression
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);

        // Give the compressor the data to compress
        compressor.setInput(input);
        compressor.finish();

        // Create an expandable byte array to hold the compressed data.
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

        // Compress the data
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException ignored) {
        }

        // Get the compressed data
        byte[] compressedData = bos.toByteArray();
        return toBase64(compressedData);
    }

    public static String decompress(String compressedMessage) {
        byte[] compressedData = fromBase64(compressedMessage);
        // Create the decompressor and give it the data to compress
        Inflater decompressor = new Inflater();
        decompressor.setInput(compressedData);

        // Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);

        // Decompress the data
        byte[] buf = new byte[1024];
        boolean errorOccured = false;
        while (!decompressor.finished() && !errorOccured) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            } catch (DataFormatException e) {
                errorOccured = true;
            }
        }
        try {
            bos.close();
        } catch (IOException ignored) {
        }

        // Get the decompressed data
        return bos.toString(StandardCharsets.UTF_8);
    }

    // ---- Encryption ----

    /**
     * from: <a href="http://javaalmanac.com/egs/javax.crypto/PassKey.html">...</a>
     */
    public static class DesEncrypter {

        private static final String SALT_PRESENT_INDICATOR = " ";
        private static final int SALT_LENGTH = 8;

        Cipher ecipher;

        Cipher dcipher;

        // 8-byte default Salt
        byte[] salt = {
                (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
        };

        // Iteration count
        final static int iterationCount = 19;

        private final char[] passPhrase;
        private final String mAlgorithm;

        public DesEncrypter(StringBuilder pPassPhrase, String pAlgorithm) {
            passPhrase = new char[pPassPhrase.length()];
            pPassPhrase.getChars(0, passPhrase.length, passPhrase, 0);
            mAlgorithm = pAlgorithm;
        }

        private void init(byte[] mSalt) {
            if (mSalt != null) {
                this.salt = mSalt;
            }
            if (ecipher == null) {
                try {
                    // Create the key
                    KeySpec keySpec = new PBEKeySpec(passPhrase, salt,
                            iterationCount);
                    SecretKey key = SecretKeyFactory.getInstance(mAlgorithm)
                            .generateSecret(keySpec);
                    ecipher = Cipher.getInstance(mAlgorithm);
                    dcipher = Cipher.getInstance(mAlgorithm);

                    // Prepare the parameter to the ciphers
                    AlgorithmParameterSpec paramSpec = new PBEParameterSpec(
                            salt, iterationCount);

                    // Create the ciphers
                    ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                    dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
                } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException |
                         NoSuchPaddingException | InvalidKeySpecException ignored) {
                }
            }
        }

        public String encrypt(String str) {
            try {
                // Encode the string into bytes using utf-8
                byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
                // determine salt by random:
                byte[] newSalt = new byte[SALT_LENGTH];
                for (int i = 0; i < newSalt.length; i++) {
                    newSalt[i] = (byte) (Math.random() * 256L - 128L);
                }

                init(newSalt);
                // Encrypt
                byte[] enc = ecipher.doFinal(utf8);

                // Encode bytes to base64 to get a string
                return EncryptionUtils.toBase64(newSalt) + SALT_PRESENT_INDICATOR
                        + EncryptionUtils.toBase64(enc);
            } catch (BadPaddingException | IllegalBlockSizeException ignored) {
            }
            return null;
        }

        public String decrypt(String str) {
            if (str == null) {
                return null;
            }
            try {
                byte[] localSalt = null;
                // test if salt exists:
                int indexOfSaltIndicator = str.indexOf(SALT_PRESENT_INDICATOR);
                if (indexOfSaltIndicator >= 0) {
                    String saltString = str.substring(0, indexOfSaltIndicator);
                    str = str.substring(indexOfSaltIndicator + 1);
                    localSalt = EncryptionUtils.fromBase64(saltString);
                }
                // Decode base64 to get bytes
                str = str.replaceAll("\\s", "");
                byte[] dec = EncryptionUtils.fromBase64(str);
                init(localSalt);

                // Decrypt
                byte[] utf8 = dcipher.doFinal(dec);

                // Decode using utf-8
                return new String(utf8, StandardCharsets.UTF_8);
            } catch (BadPaddingException | IllegalBlockSizeException ignored) {
            }
            return null;
        }
    }

    public static class SingleDesEncrypter extends DesEncrypter {

        public SingleDesEncrypter(StringBuilder pPassPhrase) {
            super(pPassPhrase, "PBEWithMD5AndDES");
        }

    }

    public static class TripleDesEncrypter extends DesEncrypter {

        public TripleDesEncrypter(StringBuilder pPassPhrase) {
            super(pPassPhrase, "PBEWithMD5AndTripleDES");
        }

    }
}
