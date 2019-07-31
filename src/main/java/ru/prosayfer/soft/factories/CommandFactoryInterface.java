package ru.prosayfer.soft.factories;

import ru.prosayfer.soft.BugTracker;
import ru.prosayfer.soft.commands.CommandInterface;

import java.io.BufferedReader;
import java.io.IOException;

public interface CommandFactoryInterface {
    CommandInterface createCommand(BufferedReader reader, BugTracker bugTracker) throws IOException;
}
