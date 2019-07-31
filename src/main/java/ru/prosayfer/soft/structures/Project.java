package ru.prosayfer.soft.structures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Project {

  private static final Logger LOG = LoggerFactory.getLogger(Project.class);
  private String name;
  private String description;

  public Project(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void show() {
    String text = "Name: " + name + " | Description: " + description;
    System.out.println(text);
  }
}
