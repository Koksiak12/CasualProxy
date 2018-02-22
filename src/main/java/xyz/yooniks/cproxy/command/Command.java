package xyz.yooniks.cproxy.command;

import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public abstract class Command implements CommandExecutor {

    private Group group;
    private String[] aliases;
    private String description;
    private String command;
    private String usage;

    public Command(String command, String description, String usage, Group group, String... aliases) {
        this.command = command;
        this.description = description;
        this.group = group;
        this.aliases = aliases;
        this.usage = usage;
        CommandManager.addCommand(this);
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (!p.can(group)) {
            p.sendMessage("Nie masz uprawnien do tej komendy!");
            return;
        }
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }
}
