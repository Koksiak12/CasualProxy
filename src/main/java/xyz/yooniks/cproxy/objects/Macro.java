package xyz.yooniks.cproxy.objects;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.*;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.packet.Packet;
import xyz.yooniks.cproxy.enums.MacroType;
import xyz.yooniks.cproxy.managers.MacroManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Macro {

    private List<Packet> packets;
    private int id;
    private SessionListener sessionListener;
    private MacroType macroType;
    private long time;
    private String owner;

    public Macro(final int id, final MacroType macroType, String owner) {
        this.packets = new CopyOnWriteArrayList<>();
        this.id = id;
        this.macroType = macroType;
        this.owner=owner;
    }

    public String getOwner() {
        return owner;
    }

    public MacroType getMacroType() {
        return this.macroType;
    }

    public int getId() {
        return this.id;
    }

    public void macroStartDoing(final Player p, final boolean infinite) {
        //p.setDoMacro(true);
        if (this.macroType == MacroType.PLAYER) {
            if (infinite) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Session s = p.getSessionConnect();
                        while (true) {
                            for (int i = 0; i <packets.size(); ++i) {
                                if (p.stopMacroPlayer || s==null){
                                    p.sendMessage("$p &cStopowanie taska z macrem..");
                                    p.stopMacroPlayer=false;
                                    Thread.currentThread().stop();
                                    break;
                                }
                                s.send(packets.get(i));
                                try {
                                    Thread.sleep(45L);
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (i >= packets.size()) {
                                    i = 0;
                                }
                            }
                        }
                    }
                }).start();
            }
            else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Session s = p.getSessionConnect();
                        for (int i = 0; i < packets.size(); ++i) {
                            if (p.stopMacroPlayer || s==null){
                                p.sendMessage("$p &cStopowanie taska z macrem..");
                                p.stopMacroPlayer=false;
                                Thread.currentThread().stop();
                                break;
                            }
                            s.send(packets.get(i));
                            try {
                                Thread.sleep(45L);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Thread.currentThread().stop();
                    }
                }).start();
            }
        }
        else if (infinite) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        for (int i = 0; i < packets.size(); ++i) {
                            if (p.stopMacroBot) {
                                p.sendMessage("$p &cStopowanie taska z macrem..");
                                p.stopMacroBot=false;
                                Thread.currentThread().stop();
                                break;
                            }
                            for (final Bot bot : p.getBots()) {
                                bot.getSession().send(packets.get(i));
                            }
                            try {
                                Thread.sleep(45L);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (i >= packets.size()) {
                                i = 0;
                            }
                        }
                        Thread.currentThread().stop();
                    }
                }
            }).start();
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < packets.size(); ++i) {
                        if (p.stopMacroBot) {
                            p.sendMessage("$p &cStopowanie taska z macrem..");
                            p.stopMacroBot=false;
                            Thread.currentThread().stop();
                            break;
                        }
                        for (final Bot bot : p.getBots()) {
                            bot.getSession().send(packets.get(i));
                        }
                        try {
                            Thread.sleep(45L);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Thread.currentThread().stop();
                }
            }).start();
        }
    }

    public void stopRecording(Player p) {
        if (p.getSessionConnect() == null) return;
        p.macroRecording=false;
        p.macro=null;
        p.getSessionConnect().removeListener(this.sessionListener);
        MacroManager.macros.add(this);
        this.time = System.currentTimeMillis() - this.time;
    }

    public void startRecording(Player p) {
        if (p.getSessionConnect() == null)return;
        p.macroRecording=true;
        this.time = System.currentTimeMillis();
        final SessionAdapter sessionListener = new SessionAdapter() {
            @Override
            public void packetSent(final PacketSentEvent event) {
                if (p.macroRecording && p.getSessionConnect() != null &&
                        event.getPacket().toString().toLowerCase().contains("client")) {
                    final int size = packets.size();
                    if (Integer.toString(size).contains("0")) { //XDXD
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" +packets.size());
                    }
                    /*else if (packets.size() == 200) {
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" +packets.size());
                    }
                    else if (packets.size() == 300) {
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" +packets.size());
                    }
                    else if (packets.size() == 400) {
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" +packets.size());
                    }
                    else if (packets.size() == 500) {
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" +packets.size());
                    }
                    else if (packets.size() == 600) {
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" + packets.size());
                    }
                    else if (packets.size() == 700) {
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" +packets.size());
                    }
                    else if (packets.size() == 800) {
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" + packets.size());
                    }
                    else if (packets.size() == 900) {
                        p.sendMessage("$p &aRejestrowanie macra, aktualnie zarejestrowanych &apakietow: &7" + packets.size());
                    }*/
                    if (event.getPacket() instanceof ClientPlayerMovementPacket ||
                            event.getPacket() instanceof ClientChangeHeldItemPacket ||
                            event.getPacket() instanceof ClientWindowActionPacket ||
                            event.getPacket() instanceof ClientPlayerActionPacket ||
                            event.getPacket() instanceof ClientPlayerStatePacket ||
                            event.getPacket() instanceof ClientChatPacket ||
                            event.getPacket() instanceof ClientSwingArmPacket ||
                            event.getPacket() instanceof ClientRequestPacket ||
                            event.getPacket() instanceof ClientCloseWindowPacket ||
                            event.getPacket() instanceof ClientCreativeInventoryActionPacket ||
                            event.getPacket() instanceof ClientConfirmTransactionPacket ||
                            event.getPacket() instanceof ClientPlayerPlaceBlockPacket ||
                            event.getPacket() instanceof ClientKeepAlivePacket) {
                        if (event.getPacket() instanceof ClientChatPacket){
                            if (((ClientChatPacket) event.getPacket()).getMessage().startsWith(","))return;
                        }
                        packets.add(event.getPacket());
                    }
                }
            }

            @Override
            public void packetReceived(final PacketReceivedEvent event) {
            }

            @Override
            public void disconnected(final DisconnectedEvent disconnectedEvent) {
                p.macroRecording=false;
                p.macro=null;
            }
        };
        p.getSessionConnect().addListener(sessionListener);
        this.sessionListener = sessionListener;
    }

    public long getTime() {
        return System.currentTimeMillis() - this.time;
    }

    public SessionListener getSessionListener() {
        return this.sessionListener;
    }

    public List<Packet> getPackets() {
        return this.packets;
    }
}
