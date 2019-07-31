package ru.prosayfer.soft.structures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {
  private static final Logger LOG = LoggerFactory.getLogger(User.class);
  private String login;
  private String password;
  private String email;
  private String post;

  public User(String login, String password, String email, String post) {
    this.login = login;
    this.password = password;
    this.email = email;
    this.post = post;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPost() {
    return post;
  }

  public void setPost(String post) {
    this.post = post;
  }

  public void show() {
    String text = "Login: " + login + " | Email: " + email + " | Post: " + post;
    System.out.println(text);
  }
}

