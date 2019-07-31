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

public class UpdateCommand implements CommandInterface {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateCommand.class.getName());
    private BufferedReader reader;
    private BugTracker bugTracker;

    public UpdateCommand(BufferedReader reader, BugTracker bugTracker) {
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
      updateUser(login, reader);
    } else if (object.equalsIgnoreCase("PROJECT")) {
      System.out.print("Name> ");
      String name = reader.readLine();
      updateProject(name, reader);
    } else if (object.equalsIgnoreCase("ISSUE")) {
      System.out.print("Number> ");
      int number = Integer.parseInt(reader.readLine());
      updateIssue(number, reader);
    } else {
      LOG.warn("Unknown object");
    }
  }

    private void updateUser(String login, BufferedReader reader) throws IOException {
        User user = bugTracker.getDbManager().getUser(login);
        if (user == null) {
            LOG.warn("There is no such user");
            return;
        }

        System.out.print("Change password?\n> ");
        String answer = reader.readLine();
        if (answer.equalsIgnoreCase("YES")) {
            System.out.print("New password> ");
            String password = reader.readLine();
            password = bugTracker.getSecurity().encrypt(password);
            user.setPassword(password);
        }

        System.out.print("Change email?\n> ");
        answer = reader.readLine();
        if (answer.equalsIgnoreCase("YES")) {
            System.out.print("New email> ");
            user.setEmail(reader.readLine());
        }

        System.out.print("Change post?\n> ");
        answer = reader.readLine();
        if (answer.equalsIgnoreCase("YES")) {
            System.out.print("New post> ");
            user.setPost(reader.readLine());
        }

        bugTracker.getDbManager().modifyUser(user);
    }


    private void updateProject(String name, BufferedReader reader) throws IOException {
    Project project = bugTracker.getDbManager().getProject(name);
    if (project == null) {
      LOG.warn("There is no such project");
      return;
    }

    System.out.print("Change description?\n> ");
    String answer = reader.readLine();
    if (answer.equalsIgnoreCase("YES")) {
      System.out.print("New description> ");
      project.setDescription(reader.readLine());
      bugTracker.getDbManager().modifyProject(project);
    }
  }

  private void updateIssue(int number, BufferedReader reader) throws IOException {
    Issue issue = bugTracker.getDbManager().getIssue(number);
    if (issue == null) {
      LOG.warn("There is no such issue");
      return;
    }

    System.out.print("Change description?\n> ");
    String answer = reader.readLine();
    if (answer.equalsIgnoreCase("YES")) {
      System.out.print("New description> ");
      issue.setDescription(reader.readLine());
    }

    System.out.print("Change status?\n> ");
    answer = reader.readLine();
    if (answer.equalsIgnoreCase("YES")) {
      System.out.print("New status> ");
      issue.setStatus(reader.readLine());
    }

    System.out.print("Change project?\n> ");
    answer = reader.readLine();
    if (answer.equalsIgnoreCase("YES")) {
      System.out.print("New project> ");
      String projectName = reader.readLine();
      Project project = bugTracker.getDbManager().getProject(projectName);
      if (project != null) {
        issue.setProject(project);
      }
    }

    System.out.print("Change user?\n> ");
    answer = reader.readLine();
    if (answer.equalsIgnoreCase("YES")) {
      System.out.print("New user> ");
      String userLogin = reader.readLine();
      User user = bugTracker.getDbManager().getUser(userLogin);
      if (user != null) {
        issue.setUser(user);
      }
    }

    bugTracker.getDbManager().modifyIssue(issue);
  }
}
