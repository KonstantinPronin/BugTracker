package ru.prosayfer.soft.structures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Issue {

  private static final Logger LOG = LoggerFactory.getLogger(Issue.class);

  public enum IssueStatus {
    NEW,
    IN_PROGRESS,
    COMPLETED,
    CLOSED
  }

  private int number;
  private String description;
  private Issue.IssueStatus status;
  private Project project;
  private User user;

  public Issue(int number, String description, String status, Project project, User user) {
    this.number = number;
    this.description = description;
    this.project = project;
    this.user = user;
    this.status = Issue.IssueStatus.valueOf(status);
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Issue.IssueStatus getStatus() {
    return status;
  }

  public void setStatus(String status) {
    try {
      this.status = Issue.IssueStatus.valueOf(status);
    } catch (IllegalArgumentException ex) {
      LOG.warn("There is no such status", ex);
    }
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void show() {
    String text =
        "Number: "
            + number
            + " | Description: "
            + description
            + " | Status: "
            + status
            + " | Project: "
            + project.getName()
            + " | User: "
            + project.getName();
    System.out.println(text);
  }
}
