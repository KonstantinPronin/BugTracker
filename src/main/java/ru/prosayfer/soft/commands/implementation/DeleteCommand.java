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

public class DeleteCommand implements CommandInterface {
  private static final Logger LOG = LoggerFactory.getLogger(DeleteCommand.class.getName());
  private BufferedReader reader;
  private BugTracker bugTracker;

  public DeleteCommand(BufferedReader reader, BugTracker bugTracker) {
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
      deleteUser(login);
    } else if (object.equalsIgnoreCase("PROJECT")) {
      System.out.print("Name> ");
      String name = reader.readLine();
      deleteProject(name);
    } else if (object.equalsIgnoreCase("ISSUE")) {
      System.out.print("Number> ");
      int number = Integer.parseInt(reader.readLine());
      deleteIssue(number);
    } else {
      LOG.warn("Unknown object");
    }
  }

  private void deleteUser(String login) {
    User user = bugTracker.getDbManager().getUser(login);
    if (user == null) {
      LOG.warn("There is no such user");
      return;
    }
    if (bugTracker.getDbManager().deleteUser(user)) {
      LOG.info("User {} has been deleted", login);
    } else {
      LOG.warn("User {} has not been deleted", login);
    }
  }

  private void deleteProject(String name) {
    Project project = bugTracker.getDbManager().getProject(name);
    if (project == null) {
      LOG.warn("There is no such project");
      return;
    }
    if (bugTracker.getDbManager().deleteProject(project)) {
      LOG.info("Project {} has been deleted", name);
    } else {
      LOG.warn("Project {} has not been deleted", name);
    }
  }

  private void deleteIssue(int number) {
    Issue issue = bugTracker.getDbManager().getIssue(number);
    if (issue == null) {
      LOG.warn("There is no such issue");
      return;
    }
    if (bugTracker.getDbManager().deleteIssue(issue)) {
      LOG.info("Issue {} has been deleted", number);
    } else {
      LOG.warn("Issue {} has not been deleted", number);
    }
  }
}
