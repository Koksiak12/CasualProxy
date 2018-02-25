package xyz.yooniks.cproxy.objects;

import org.spacehq.mc.protocol.data.game.values.MessageType;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.spacehq.packetlib.Session;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.managers.ProxyManager;
import xyz.yooniks.cproxy.objects.player.BotOptions;
import xyz.yooniks.cproxy.objects.player.Connector;
import xyz.yooniks.cproxy.objects.player.PlayerOptions;
import xyz.yooniks.cproxy.utils.ChatUtilities;

import java.util.LinkedList;
import java.util.List;

public class Player extends Connector {

    /*private boolean stopCrash;
    private boolean stopChatBot;
    private boolean stopCrashBot;*/
    public boolean stopMacroBot; //to tez kiedys poprawie, jak bedzie mi sie chcialo xd
    public boolean stopMacroPlayer; //to tez kiedys poprawie, jak bedzie mi sie chcialo xd
    public boolean macroRecording;
    public Macro macro;
    public PlayerOptions playerOptions;
    public BotOptions botOptions;
    public Thread crashPlayerThread, crashBotsThread, chatBotsSpamThread;
    private String nick;
    private Group group;
    private Session session;
    private long expirationDate;
    private String password;
    private boolean logged;
    private boolean mother;
    private List<Bot> bots = new LinkedList<>();

    public Player(String name, Group group) {
        this.nick = name;
        this.group = group;
        this.playerOptions = new PlayerOptions();
        this.botOptions = new BotOptions();
    }

    public boolean can(Group group) {
        return this.group.getPermissionLevel() >= group.getPermissionLevel();
    }

    public void sendMessage(String message) {
        this.session.send(new ServerChatPacket(ChatUtilities.fixColor(message)));
    }

    public void updateTab() {
        if (sessionConnect != null && !getLastPacket().toLowerCase().contains("rozlaczono")) {
            final Long ms = (System.currentTimeMillis() - getLastPacketMs() == 0L) ? 1L :
                    (System.currentTimeMillis() - getLastPacketMs());
            session.send(new ServerChatPacket(
                    ChatUtilities.fixColor("&8>> &7Ostatni pakiet z serwera otrzymano: &a" + ms + "ms temu"),
                    MessageType.NOTIFICATION));
            session.send(new ServerPlayerListDataPacket(
                    new TextMessage(ChatUtilities.fixColor("&2Casual&aProxy &7created by &ayooniks\n")),
                    new TextMessage(ChatUtilities.fixColor("\n&7Ostatni pakiet z serwera &8(&7" + (System.currentTimeMillis() - getLastPacketMs()
                    ) + "ms temu&8):\n&8( &a" + getLastPacket() + "&8 )\n&7Twoj nick: &a" + nick + "\n&7Serwer docelowy: &a" +
                            sessionConnect.getHost() + ":" + sessionConnect.getPort() + "\n " +
                            "&7Liczba twoich botow: &a" + bots.size() + "\n&7Lista aktywnych proxy: &a" +
                            ProxyManager.proxies.size() + "&8/&c" + ProxyManager.allproxies +
                            "\n\n&7Zakup dostep do proxy na skype:" +
                            " &ayooniksyooniks@gmail.com"))));
        } else if (sessionConnect == null || getLastPacket().toLowerCase().contains("rozlaczono")) {
            session.send(new ServerPlayerListDataPacket(
                    new TextMessage(ChatUtilities.fixColor("&2Casual&aProxy &7created by &ayooniks\n")),
                    new TextMessage(ChatUtilities.fixColor("\n&7Ostatni pakiet z serwera &8(&70ms temu&8):\n&8( &cRozlaczono &8)\n&7Twoj nick: &a" + nick + "\n&7Serwer docelowy: &a" +
                            "brak" + "\n " +
                            "&7Liczba twoich botow: &a" + bots.size() + "\n&7Lista aktywnych proxy: &a" +
                            ProxyManager.proxies.size() + "&8/&c" + ProxyManager.allproxies +
                            "\n\n&7Zakup dostep do proxy na skype:" +
                            " &ayooniksyooniks@gmail.com"))));
        }
    }

    public void updateLag() {
        if (sessionConnect != null && System.currentTimeMillis() - getLastPacketMs() > 1050L
                && System.currentTimeMillis() - getLastPacketMs() < 1000000L) {
            session.send(new ServerTitlePacket("", false));
            session.send(new ServerTitlePacket(ChatUtilities.fixColor("&8>> &cSerwer nie odpowiada! (LAG?)"), false));
            session.send(new ServerTitlePacket(ChatUtilities.fixColor("&8>> &fOd: &7" + (System.currentTimeMillis()
                    - getLastPacketMs()) + "ms"), true));
            session.send(new ServerTitlePacket(10, 17, 10));
        }
    }

    public boolean canJoin() {
        return this.expirationDate > System.currentTimeMillis();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getNick() {
        return nick;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public List<Bot> getBots() {
        return bots;
    }

    public void addBot(Bot bot) {
        this.bots.add(bot);
    }

    public void removeBot(Bot bot) {
        this.bots.remove(bot);
    }

    public boolean isMother() {
        return mother;
    }

    public void setMother(boolean mother) {
        this.mother = mother;
    }

    /*public boolean isStopCrash() {
        return stopCrash;
    }

    public boolean isStopChatBot() {
        return stopChatBot;
    }

    public boolean isStopCrashBot() {
        return stopCrashBot;
    }

    public void setStopChatBot(boolean stopChatBot) {
        this.stopChatBot = stopChatBot;
    }

    public void setStopCrash(boolean stopCrash) {
        this.stopCrash = stopCrash;
    }

    public void setStopCrashBot(boolean stopCrashBot) {
        this.stopCrashBot = stopCrashBot;
    }*/
}
