package xyz.yooniks.cproxy.command;

import xyz.yooniks.cproxy.objects.Player;

public interface CommandExecutor {

    public abstract void onCommand(Player p, Command command, String[] args);
}
