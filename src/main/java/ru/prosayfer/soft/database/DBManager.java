package ru.prosayfer.soft.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.prosayfer.soft.structures.Issue;
import ru.prosayfer.soft.structures.Project;
import ru.prosayfer.soft.structures.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Class with opportunity to connect to and interact with the database */
public class DBManager {
  private static final Logger LOG = LoggerFactory.getLogger(DBManager.class);

  private String url;
  private String login;
  private String password;

  public DBManager(String url, String login, String password) {
    this.url = url;
    this.login = login;
    this.password = password;
  }

  public long getId(User user) {
    String query =
        "SELECT * FROM \"BugTracker\".\"Users\" where \"login\" = '" + user.getLogin() + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      if (result.next()) {
        return result.getLong(1);
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return -1;
  }

  public long getId(Project project) {
    String query =
        "SELECT * FROM \"BugTracker\".\"Projects\" where \"name\" = '" + project.getName() + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      if (result.next()) {
        return result.getLong(1);
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return -1;
  }

  public User getUser(String login) {
    String query = "SELECT * FROM \"BugTracker\".\"Users\" where \"login\" = '" + login + "'";
    try (Connection connection = DriverManager.getConnection(url, this.login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      if (result.next()) {
        return new User(
            result.getString(2), result.getString(3), result.getString(4), result.getString(5));
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return null;
  }

  private User getUser(long id) {
    String query = "SELECT * FROM \"BugTracker\".\"Users\" where \"id\" = '" + id + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      if (result.next()) {
        return new User(
            result.getString(2), result.getString(3), result.getString(4), result.getString(5));
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return null;
  }

  public Project getProject(String name) {
    String query = "SELECT * FROM \"BugTracker\".\"Projects\" where \"name\" = '" + name + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      if (result.next()) {
        return new Project(result.getString(2), result.getString(3));
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return null;
  }

  private Project getProject(long id) {
    String query = "SELECT * FROM \"BugTracker\".\"Projects\" where \"id\" = '" + id + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      if (result.next()) {
        return new Project(result.getString(2), result.getString(3));
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return null;
  }

  public List<Project> getAllProjects() {
    List<Project> projects = new ArrayList<>();
    String query = "SELECT * FROM \"BugTracker\".\"Projects\"";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      if (result.next()) {
        projects.add(new Project(result.getString(2), result.getString(3)));
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return projects;
  }

  public Issue getIssue(int number) {
    String query = "SELECT * FROM \"BugTracker\".\"Issues\" where \"number\" = '" + number + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      if (result.next()) {
        Project project = getProject(result.getLong(5));
        User user = getUser(result.getLong(6));
        return new Issue(number, result.getString(3), result.getString(4), project, user);
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return null;
  }

  public List<Issue> getIssues(User user) {
    List<Issue> issues = new ArrayList<>();
    long userId = getId(user);

    if (userId == -1) {
      LOG.error("There is no such user");
      return issues;
    }

    String query = "SELECT * FROM \"BugTracker\".\"Issues\" where \"user\" = '" + userId + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      while (result.next()) {
        issues.add(getIssue(result.getInt(2)));
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return issues;
  }

  public List<Issue> getIssues(Project project) {
    List<Issue> issues = new ArrayList<>();
    long projectId = getId(project);

    if (projectId == -1) {
      LOG.error("There is no such project");
      return issues;
    }

    String query =
        "SELECT * FROM \"BugTracker\".\"Issues\" where \"project\" = '" + projectId + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      while (result.next()) {
        issues.add(getIssue(result.getInt(2)));
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return issues;
  }

  public List<Issue> getIssues(User user, Project project) {
    List<Issue> issues = new ArrayList<>();
    long userId = getId(user);
    long projectId = getId(project);

    if (projectId == -1) {
      LOG.error("There is no such project");
      return issues;
    }

    if (userId == -1) {
      LOG.error("There is no such user");
      return issues;
    }

    String query =
        "SELECT * FROM \"BugTracker\".\"Issues\" where \"project\" = '"
            + projectId
            + "' and \"user\" = '"
            + userId
            + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query)) {
      while (result.next()) {
        issues.add(getIssue(result.getInt(2)));
      }
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return issues;
  }

  public boolean addUser(User user) {
    String query =
        "INSERT INTO \"BugTracker\".\"Users\" VALUES (DEFAULT,'"
            + user.getLogin()
            + "','"
            + user.getPassword()
            + "','"
            + user.getEmail()
            + "','"
            + user.getPost()
            + "')";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }

  public boolean addProject(Project project) {
    String query =
        "INSERT INTO \"BugTracker\".\"Projects\" VALUES (DEFAULT,'"
            + project.getName()
            + "','"
            + project.getDescription()
            + "')";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }

  public boolean addIssue(Issue issue) {
    long userId = getId(issue.getUser());
    long projectId = getId(issue.getProject());

    if (projectId == -1) {
      LOG.error("There is no such project");
      return false;
    }

    if (userId == -1) {
      LOG.error("There is no such user");
      return false;
    }

    String query =
        "INSERT INTO \"BugTracker\".\"Issues\" VALUES (DEFAULT,"
            + issue.getNumber()
            + ",'"
            + issue.getDescription()
            + "','"
            + issue.getStatus()
            + "',"
            + projectId
            + ","
            + userId
            + ")";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }

  public boolean modifyUser(User user) {
    String query =
        "UPDATE \"BugTracker\".\"Users\" SET \"password\"='"
            + user.getPassword()
            + "', \"email\"='"
            + user.getEmail()
            + "', \"post\"='"
            + user.getPost()
            + "' WHERE \"login\" = '"
            + user.getLogin()
            + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }

  public boolean modifyProject(Project project) {
    String query =
        "UPDATE \"BugTracker\".\"Projects\" SET \"name\"='"
            + project.getName()
            + "', \"description\"='"
            + project.getDescription()
            + "' WHERE \"name\" = '"
            + project.getName()
            + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }

  public boolean modifyIssue(Issue issue) {
    long userId = getId(issue.getUser());
    long projectId = getId(issue.getProject());

    if (projectId == -1) {
      LOG.error("There is no such project");
      return false;
    }

    if (userId == -1) {
      LOG.error("There is no such user");
      return false;
    }

    String query =
        "UPDATE \"BugTracker\".\"Issues\" SET \"description\"='"
            + issue.getDescription()
            + "', \"status\"='"
            + issue.getStatus()
            + "', \"project\"='"
            + projectId
            + "', \"user\"='"
            + userId
            + "' WHERE \"number\" = '"
            + issue.getNumber()
            + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }

  public boolean deleteUser(User user) {
    String query =
        "DELETE FROM \"BugTracker\".\"Users\" where \"login\" = '" + user.getLogin() + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }

  public boolean deleteProject(Project project) {
    List<Issue> issues = getIssues(project);
    for (Issue issue : issues) {
      deleteIssue(issue);
    }
    String query =
        "DELETE FROM \"BugTracker\".\"Projects\" where \"name\" = '" + project.getName() + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }

  public boolean deleteIssue(Issue issue) {
    String query =
        "DELETE FROM \"BugTracker\".\"Issues\" where \"number\" = '" + issue.getNumber() + "'";
    try (Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement()) {
      statement.executeQuery(query);
      return true;
    } catch (SQLException e) {
      LOG.error("Error while connecting to database", e);
    }
    return false;
  }
}
