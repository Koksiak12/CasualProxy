package xyz.yooniks.cproxy.command.commands.bots;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.enums.MacroType;
import xyz.yooniks.cproxy.managers.MacroManager;
import xyz.yooniks.cproxy.objects.Macro;
import xyz.yooniks.cproxy.objects.Player;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MacroCommand extends Command {

    public MacroCommand() {
        super("macro", "Nagraj ruchy, a potem je powtarzaj!", ",macro",
                Group.GRACZ, "makro");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Poprawne uzycie: &a,macro start [bot/runnables]");
            p.sendMessage("&a,macro play [id macra] [typ macra (bot/human)] [infinite, true/false]");
            p.sendMessage("&a,macro stop&7 - konczy nagrywanie macra");
            p.sendMessage("&a,stop &7- komendy do stopowania wykonywania macra");
            return;
        }
        if (args[1].equalsIgnoreCase("start")) {
            if (args.length == 3) {
                if (p.macroRecording) {
                    p.sendMessage("$p &cNagrywasz juz macro! Mozesz wylaczyc nagrywanie macra uzywajac: &6,macro stop");
                    return;
                }
                if (p.getSessionConnect() == null) {
                    p.sendMessage("$p &cNie jestes polaczony z zadnym serwerem!");
                    return;
                }
                if (args[2].equalsIgnoreCase("bot")) {
                    final Random rand = new Random();
                    int id = rand.nextInt(400);
                    for (final Macro m : MacroManager.macros) {
                        if (m.getId() == id) {
                            id = rand.nextInt(410);
                        }
                    }
                    final Macro macro = new Macro(id, MacroType.BOT,p.getNick());
                    p.sendMessage("$p &aZaczynam nagrywac macro.. ID macra: &7" + macro.getId() + "&a, typ macra: &7BOT");
                    p.macro=macro;
                    macro.startRecording(p);
                } else if (args[2].equalsIgnoreCase("runnables")) {
                    final Random rand = new Random();
                    int id = rand.nextInt(400);
                    for (final Macro m : MacroManager.macros) {
                        if (m.getId() == id) {
                            id = rand.nextInt(410);
                        }
                    }
                    final Macro macro = new Macro(id, MacroType.PLAYER,p.getNick());
                    p.sendMessage("$p &aZaczynam nagrywac macro.. ID macra: &7" + macro.getId() + "&a, typ macra: &7PLAYER");
                    p.macro=macro;
                    macro.startRecording(p);
                }
            }
            else {
                p.sendMessage("$p &7Poprawne uzycie: &a,macro start [bot/runnables]");
                p.sendMessage("&a,macro play [id macra] [typ macra (bot/human)] [infinite, true/false]");
                p.sendMessage("&a,macro stop&7 - konczy nagrywanie macra");
                p.sendMessage("&a,stop &7- komendy do stopowania wykonywania macra");
            }
        }
        else if (args[1].equalsIgnoreCase("play")) {
            if (args.length > 4) {
                Macro macro2 = null;
                for (final Macro m : MacroManager.macros) {
                    if (m.getId() == Integer.valueOf(args[2])) {
                        macro2 = m;
                    }
                }
                if (macro2 == null) {
                    p.sendMessage("$p &cMacro z ID &6" + args[2] + " &cnie istnieje!");
                    return;
                }
                final boolean infinite = Boolean.valueOf(args[4]);
                final String type = args[3];
                if (type.equalsIgnoreCase("runnables") || type.equalsIgnoreCase("human")) {
                    if (p.getSessionConnect()==null) {
                        p.sendMessage("$p &cNie jestes polaczony z zadnym serwerem!");
                        return;
                    }
                    p.sendMessage("$p &aOdtwarzam macro, ID: &7" + macro2.getId() + "&a, typ macra: " +
                            "&7PLAYER&a, infinite: &7" + infinite+"&a, zastopuj uzywajac: &7,stop macro");
                    macro2.macroStartDoing(p, infinite);
                }
                else if (type.equalsIgnoreCase("bot") || type.equalsIgnoreCase("bots")) {
                    if (p.getBots().size() <1) {
                        p.sendMessage("$p &cNie masz zadnych botow!");
                        return;
                    }
                    p.sendMessage("$p &aOdtwarzam macro, ID: &7" + macro2.getId() + "&a, typ macra: " +
                            "&7BOT&a, infinite: &7" + infinite+"&a, zastopuj uzywajac: &7,stop macrobot");
                    macro2.macroStartDoing(p, infinite);
                }
            }
        }
        else if (args[1].equalsIgnoreCase("stop")) {
            if (!p.macroRecording) {
                p.sendMessage("$p &cNie nagrywasz zadnego macra! Mozesz zaczac nagrywac macro uzywajac: &6,macro start");
                return;
            }
            if (p.macro != null) {
                p.sendMessage("$p &aMacro nagrane i gotowe do uzytku. &a("
                        + p.macro.getPackets().size() + "&a pakietow, &aczas: "
                        + p.macro.getTime() + "ms ("+ TimeUnit.MILLISECONDS.toSeconds(p.macro.getTime())+"sek)&a, id: &7" + p.macro.getId() + "&a)");
                p.macro.stopRecording(p);
                //p.macroRecording=false;
                //MacroManager.macros.add(p.macro);
                //p.macro=null;
            }
        }
        else if (args[1].equalsIgnoreCase("list")) {
            if (MacroManager.macros.size() == 0) {
                p.sendMessage("$p &cBrak stworzonych makier!");
                return;
            }
            p.sendMessage("$p &cLista makier:");
            for (Macro macro : MacroManager.macros)
                p.sendMessage("&6" + macro.getId() + "&7, typ: &6" + macro.getMacroType()
                        + "&7, ilosc pakietow: &6" + macro.getPackets().size() + "&7, czas: &6"
                        + macro.getTime() + "ms ("+ TimeUnit.MILLISECONDS.toSeconds(p.macro.getTime())+"sek)&7, autor: &6"+macro.getOwner());

        }
    }
}