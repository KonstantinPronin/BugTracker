package ru.prosayfer.soft.commands.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.prosayfer.soft.BugTracker;
import ru.prosayfer.soft.commands.CommandInterface;
import ru.prosayfer.soft.structures.Issue;
import ru.prosayfer.soft.structures.Project;
import ru.prosayfer.soft.structures.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class AddCommand implements CommandInterface {
  private static final Logger LOG = LoggerFactory.getLogger(AddCommand.class.getName());
  private BufferedReader reader;
  private BugTracker bugTracker;

  public AddCommand(BufferedReader reader, BugTracker bugTracker) {
    this.reader = reader;
    this.bugTracker = bugTracker;
  }

  @Override
  public void execute() throws IOException {
    System.out.print("List of available objects: USER, PROJECT, ISSUE\n>");
    String object = reader.readLine();
    if (object.equalsIgnoreCase("USER")) {
      System.out.print("Login> ");
      String login = reader.readLine();
      System.out.print("Password> ");
      String password = reader.readLine();
      System.out.print("Email> ");
      String email = reader.readLine();
      System.out.print("Post> ");
      String post = reader.readLine();
      addUser(login, password, email, post);
    } else if (object.equalsIgnoreCase("PROJECT")) {
      System.out.print("Name> ");
      String name = reader.readLine();
      System.out.print("Description> ");
      String description = reader.readLine();
      addProject(name, description);
    } else if (object.equalsIgnoreCase("ISSUE")) {
      addIssue(reader);
    } else {
      LOG.warn("Unknown object");
    }
  }

  private User addUser(String login, String password, String email, String post) {
    password = bugTracker.getSecurity().encrypt(password);
    User newUser = new User(login, password, email, post);
    if (bugTracker.getDbManager().addUser(newUser)) {
      LOG.info("User {} has been added", login);
      return newUser;
    } else {
      LOG.warn("User {} has not been added", login);
      return null;
    }
  }

  private Project addProject(String name, String description) {
    Project newProject = new Project(name, description);
    if (bugTracker.getDbManager().addProject(newProject)) {
      LOG.info("Project {} has been added", name);
      return newProject;
    } else {
      LOG.warn("Project {} has not been added", name);
      return null;
    }
  }

  private Issue addIssue(BufferedReader reader) throws IOException {
    List<Project> projects = bugTracker.getDbManager().getAllProjects();
    Issue issue = null;
    System.out.println("Choose project: ");
    for (Project project : projects) {
      System.out.println(project.getName());
    }
    System.out.print("> ");
    String projectName = reader.readLine();
    Project project = bugTracker.getDbManager().getProject(projectName);
    System.out.println("Issue description: ");
    String description = reader.readLine();
    issue =
        new Issue(
            bugTracker.getIssueNumber(), description, "NEW", project, bugTracker.getCurrentUser());
    if (bugTracker.getDbManager().addIssue(issue)) {
      LOG.info("Issue {} has been added", bugTracker.getIssueNumber());
      bugTracker.increaseIssueNumber();
    } else {
      LOG.warn("Issue {} has not been added", bugTracker.getIssueNumber());
    }

    return issue;
  }
}
