package xyz.yooniks.cproxy;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import xyz.yooniks.cproxy.command.commands.*;
import xyz.yooniks.cproxy.command.commands.bots.*;
import xyz.yooniks.cproxy.command.commands.settings.*;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.managers.PlayerManager;
import xyz.yooniks.cproxy.objects.Player;
import xyz.yooniks.cproxy.utils.DateUtilities;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public abstract class ProxyHelper {

    private static Logger logger = Logger.getLogger("CasualProxy");
    private char[] chars;
    private XMLConfiguration config;

    protected char[] getChars() {
        return chars;
    }

    public static Logger getLogger() {
        return logger;
    }

    protected void init() {
        Arrays.fill(chars = new char[7680], ' ');
    }

    protected void loadConfig() {
        if (!new File("CasualProxy").exists()) {
            new File("CasualProxy").mkdirs();
        }
        if (!new File("CasualProxy", "settings.xml").exists()) {
            final XMLConfiguration configCreate = new XMLConfiguration();
            configCreate.setBasePath("CasualProxy");
            configCreate.setFileName("settings.xml");
            configCreate.addProperty("users", Arrays.asList("unsafe_cash;ADMIN;haslodoproxy;10-02-2019:14:00:00"));
            this.config = configCreate;
        } else {
            final XMLConfiguration configCreate = new XMLConfiguration();
            try {
                configCreate.load(new File("CasualProxy", "settings.xml"));
            } catch (ConfigurationException ex) {
                ex.printStackTrace();
            }
            this.config = configCreate;
        }
        this.config.setBasePath("CasualProxy");
        this.config.setFileName("settings.xml");
        try {
            config.save();
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
        }
        try {
            final XMLConfiguration config2 = new XMLConfiguration("CasualProxy/settings.xml");
            for (int i = 0; i < config2.getStringArray("users").length; ++i) {
                final String[] array = config2.getStringArray("users");
                final String[] split = array[i].split(";");
                final Player p = PlayerManager.getPlayer(split[0]);
                p.setGroup(Group.valueOf(split[1]));
                p.setPassword(split[2]);
                p.setExpirationDate(DateUtilities.getDateFromString(split[3]));
                CasualProxy.addAccess(p.getNick());
            }
            this.config = config2;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void loadCommands() {
        new ConnectCommand();
        new CrashCommand();
        new ConnectBotCommand();
        new QuitCommand();
        new DetachCommand();
        new BotListCommand();
        new BotChatCommand();
        new MotherCommand();
        new TimeOutCommand();
        new HelpCommand();
        new PingCommand();
        new BotQuitCommand();
        new StopCommand();
        new ChatFromBotCommand();
        new ChatFromServerCommand();
        new AutoReconnectCommand();
        new AutoReconnectTimeCommand();
        new AutoCaptchaCommand();
        new MacroCommand();
        new AutoLoginCommand();
        new MessagesBotCommand();
        new ProxyCommand();
        //new TestObejscieCommand();
    }

}
