package ru.prosayfer.soft.security;

import org.apache.commons.codec.digest.DigestUtils;
import ru.prosayfer.soft.database.DBManager;
import ru.prosayfer.soft.structures.User;

import java.security.SecureRandom;

/** Class goal is to provide authorization and to encode passwords*/
public class Security {
  private static final int SEED_BYTES = 32;
  private SecureRandom random = new SecureRandom();

  private String generateSalt() {
    byte[] seed = random.generateSeed(SEED_BYTES);
    return DigestUtils.sha256Hex(new String(seed)).substring(0, 16);
  }

  private String encrypt(String password, String salt) {
    String hash = DigestUtils.sha256Hex(password + salt);
    return hash + ":" + salt;
  }

  public String encrypt(String password) {
    String salt = generateSalt();
    return encrypt(password, salt);
  }

  private boolean matches(String passwordToCheck, String storedPassword) {
    if (storedPassword == null) {
      throw new NullPointerException("storedPassword can not be null");
    }
    if (passwordToCheck == null) {
      throw new NullPointerException("passwordToCheck can not be null");
    }
    int divider = storedPassword.indexOf(':') + 1;
    if (divider == storedPassword.length()) {
      throw new IllegalArgumentException("stored password does not contain salt");
    }
    String storedSalt = storedPassword.substring(divider);
    return encrypt(passwordToCheck, storedSalt).equalsIgnoreCase(storedPassword);
  }

  public boolean authorization(DBManager dbManager, String login, String password) {
    User user = dbManager.getUser(login);
    if (user == null) {
      return false;
    } else {
      return matches(password, user.getPassword());
    }
  }
}
