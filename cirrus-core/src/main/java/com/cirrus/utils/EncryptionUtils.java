/*
 * *
 *  * Copyright (c) 2014 Antoine Jullien
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cirrus.utils;


import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class EncryptionUtils {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private static final Random random = new SecureRandom();

    //==================================================================================================================
    // Public
    //==================================================================================================================
    public static String encrypt(final String value) {
        final String salt = Integer.toString(random.nextInt());
        return makePasswordHash(value, salt);
    }

    public static boolean match(final String noEncryptedValue, final String encryptedValue) {
        final String salt = encryptedValue.split("_")[1];

        return encryptedValue.equals(makePasswordHash(noEncryptedValue, salt));
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private static String makePasswordHash(final String value, final String salt) {
        try {

            final String saltedAndHashed = value + "," + salt;
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(saltedAndHashed.getBytes());
            final byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
            final Base64 base64 = new Base64();
            return Arrays.toString(base64.encode(hashedBytes)) + '_' + salt;
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 is not available", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
        }
    }
}
