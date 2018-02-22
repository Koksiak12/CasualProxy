package xyz.yooniks.cproxy.command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private static final List<Command> commands = new ArrayList<>();

    public static void addCommand(Command cmd) {
        commands.add(cmd);
    }

    public static List<Command> getCommands() {
        return commands;
    }
}
