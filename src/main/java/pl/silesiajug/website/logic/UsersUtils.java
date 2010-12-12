/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.silesiajug.website.logic;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.silesiajug.website.model.User;


/**
 *
 * @author Adam Szecowka
 */
public class UsersUtils {

    static public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("[a-zA-Z][a-zA-Z0-9_\\-\\.]*@[a-zA-Z][a-zA-Z0-9_\\-\\.]*\\.[a-zA-Z]{2,4}");
    }

    static public String getMD5(String text) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
