package xyz.yooniks.cproxy.command.commands;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.command.CommandManager;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Wyswietla wszystkie komendy", ",help",
                Group.GRACZ, "pomoc","pomocy");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        p.sendMessage("&7Lista komend: &8(&a"+CommandManager.getCommands().size()+"&8)");
        for (Command cmd : CommandManager.getCommands())
            p.sendMessage("&a,"+cmd.getCommand()+"&7 - "+cmd.getDescription());
    }
}