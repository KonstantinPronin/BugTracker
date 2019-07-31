package ru.prosayfer.soft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.prosayfer.soft.commands.CommandInterface;
import ru.prosayfer.soft.commands.implementation.ExitCommand;
import ru.prosayfer.soft.constants.Constants;
import ru.prosayfer.soft.database.DBManager;
import ru.prosayfer.soft.factories.implementation.CommandFactory;
import ru.prosayfer.soft.factories.CommandFactoryInterface;
import ru.prosayfer.soft.security.Security;
import ru.prosayfer.soft.structures.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/** Main class with ability to initialize connection to database and read commands from console */
public class BugTracker {
  private static final Logger LOG = LoggerFactory.getLogger(BugTracker.class);
  private static int issueNumber = 0;
  private CommandFactoryInterface commandFactory;
  private DBManager dbManager;
  private Security security;
  private User currentUser;

  private BugTracker(CommandFactoryInterface commandFactory) {
      this.commandFactory = commandFactory;
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      LOG.error("There is no configuration");
      return;
    }
    BugTracker bugTracker = new BugTracker(new CommandFactory());
    bugTracker.start(args[0]);
  }

  private DBManager initialize(String filepath) {
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(filepath))) {
      String str;
      String url = "";
      String login = "";
      String password = "";
      while ((str = reader.readLine()) != null) {
        if (str.startsWith(Constants.URL)) {
          url = str.split("=")[1];
        } else if (str.startsWith(Constants.LOGIN)) {
          login = str.split("=")[1];
        } else if (str.startsWith(Constants.PASSWORD)) {
          password = str.split("=")[1];
        }
      }
      return new DBManager(url, login, password);
    } catch (IOException e) {
      LOG.error("Error while file reading: ", e);
      return null;
    }
  }

  private void start(String args) {
    security = new Security();
    dbManager = initialize(args);
    if (dbManager == null) {
      return;
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      System.out.print("Enter login> ");
      String login = reader.readLine();
      System.out.print("Enter password> ");
      String password = reader.readLine();
      if (!security.authorization(dbManager, login, password)) {
        LOG.error("Login fail");
        return;
      }
      currentUser = dbManager.getUser(login);

      while (true) {
        CommandInterface command = commandFactory.createCommand(reader, this);
        if (command instanceof ExitCommand) {
            LOG.info("Exit");
            return;
        }
        command.execute();
      }
    } catch (IOException e) {
      LOG.error("error", e);
    }
  }

  public int getIssueNumber() {
    return issueNumber;
  }

  public DBManager getDbManager() {
    return dbManager;
  }

  public Security getSecurity() {
    return security;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public static void increaseIssueNumber() {
    ++issueNumber;
  }
}
