package com.underoneroof.mmuboard.Utility;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Hii on 09/09/2015.
 */
public class MD5 {

    public static String md5(String string) {
        return new String(Hex.encodeHex(DigestUtils.md5(string)));
    }
}