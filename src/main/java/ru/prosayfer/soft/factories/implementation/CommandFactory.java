package ru.prosayfer.soft.factories.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.prosayfer.soft.BugTracker;
import ru.prosayfer.soft.constants.Constants;
import ru.prosayfer.soft.factories.CommandFactoryInterface;
import ru.prosayfer.soft.commands.CommandInterface;
import ru.prosayfer.soft.commands.implementation.*;

import java.io.BufferedReader;
import java.io.IOException;

public class CommandFactory implements CommandFactoryInterface {
  private static final Logger LOG = LoggerFactory.getLogger(CommandFactory.class.getName());

  public CommandInterface createCommand(BufferedReader reader, BugTracker bugTracker)
      throws IOException {
    System.out.print("List of available commands: ADD, UPDATE, DELETE, GET, EXIT\n>");
    String command = reader.readLine();
    if (command.equalsIgnoreCase(Constants.ADD)) {
      return new AddCommand(reader, bugTracker);
    } else if (command.equalsIgnoreCase(Constants.UPDATE)) {
      return new UpdateCommand(reader, bugTracker);
    } else if (command.equalsIgnoreCase(Constants.DELETE)) {
      return new DeleteCommand(reader, bugTracker);
    } else if (command.equalsIgnoreCase(Constants.GET)) {
      return new GetCommand(reader, bugTracker);
    } else if (command.equalsIgnoreCase(Constants.EXIT)) {
      return new ExitCommand();
    } else {
      LOG.warn("Unknown command");
      return null;
    }
  }
}
