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

public class GetCommand implements CommandInterface {
  private static final Logger LOG = LoggerFactory.getLogger(GetCommand.class.getName());
  private BufferedReader reader;
  private BugTracker bugTracker;

  public GetCommand(BufferedReader reader, BugTracker bugTracker) {
    this.reader = reader;
    this.bugTracker = bugTracker;
  }

  @Override
  public void execute() throws IOException {
    System.out.print("List of available objects: USER, PROJECT, ISSUE, ALL PROJECTS\n>");
    String object = reader.readLine();

    if (object.equalsIgnoreCase("USER")) {
      System.out.print("Login> ");
      String login = reader.readLine();
      User user = bugTracker.getDbManager().getUser(login);
      if (user == null) {
        LOG.warn("There is no such user");
      } else {
        user.show();
      }
    } else if (object.equalsIgnoreCase("PROJECT")) {
      System.out.println("Name> ");
      String name = reader.readLine();
      Project project = bugTracker.getDbManager().getProject(name);
      if (project == null) {
        LOG.warn("There is no such project");
      } else {
        project.show();
      }
    } else if (object.equalsIgnoreCase("ISSUE")) {
      getIssue(reader);
    } else if (object.equalsIgnoreCase("ALL PROJECTS")) {
      for (Project project : bugTracker.getDbManager().getAllProjects()) {
        project.show();
      }
    } else {
      LOG.warn("Unknown object");
    }
  }

  private void getIssue(BufferedReader reader) throws IOException {
    System.out.print("Search parameters: MY, BY NUMBER, BY PROJECT, BY USER&PROJECT\n>");
    String parameter = reader.readLine();
    if (parameter.equalsIgnoreCase("MY")) {
      for (Issue issue : bugTracker.getDbManager().getIssues(bugTracker.getCurrentUser())) {
        issue.show();
      }
    } else if (parameter.equalsIgnoreCase("BY NUMBER")) {
      System.out.print("Number> ");
      int number = Integer.parseInt(reader.readLine());
      Issue issue = bugTracker.getDbManager().getIssue(number);
      if (issue != null) {
        issue.show();
      }
    } else if (parameter.equalsIgnoreCase("BY PROJECT")) {
      System.out.print("Project name> ");
      String name = reader.readLine();
      Project project = bugTracker.getDbManager().getProject(name);
      for (Issue issue : bugTracker.getDbManager().getIssues(project)) {
        issue.show();
      }
    } else if (parameter.equalsIgnoreCase("BY USER&PROJECT")) {
      System.out.print("User login> ");
      String login = reader.readLine();
      User user = bugTracker.getDbManager().getUser(login);
      System.out.print("Project name> ");
      String name = reader.readLine();
      Project project = bugTracker.getDbManager().getProject(name);
      for (Issue issue : bugTracker.getDbManager().getIssues(user, project)) {
        issue.show();
      }
    } else {
      LOG.warn("Unknown parameter");
    }
  }
}
