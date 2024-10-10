package poa.poapacketlistener.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poapacketlistener.PoaPacketListener;
import poa.poapacketlistener.util.PacketHandler121;

import java.util.ArrayList;
import java.util.List;

public class PoaPacket implements CommandExecutor, TabCompleter {

    public static List<String> cancelList = new ArrayList<>();
    public static List<String> broadcastList = new ArrayList<>();
    public static List<String> listenList = new ArrayList<>();
    public static boolean enabled = true;

    static {
        if (PoaPacketListener.config.isSet("Cancel"))
            cancelList = PoaPacketListener.config.getStringList("Cancel");

        if (PoaPacketListener.config.isSet("Broadcast"))
            broadcastList = PoaPacketListener.config.getStringList("Broadcast");

        if (PoaPacketListener.config.isSet("Enabled"))
            enabled = PoaPacketListener.config.getBoolean("Enabled");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            if (args.length == 0) {
                player.showTitle(Title.title(Component.text(""), Component.text("/poapacket <toggle/cancel/broadcast/clear <gray>(clears all previous cancels/broadcasts> <packet>")));
                player.sendRichMessage("<red>/poapacket <listen/toggle/cancel/broadcast/clear <gray>(clears all previous cancels/broadcasts> <packet>");
                return false;
            }

            switch (args[0].toLowerCase()) {
                case "cancel" -> {
                    String packetName = nameToPacketName(args[1]);
                    if(packetName == null){
                        player.showTitle(Title.title(Component.text(args[1]), Component.text("No packet found")));
                        return false;
                    }

                    if (cancelList.contains(packetName)) {
                        cancelList.remove(packetName);
                        player.showTitle(Title.title(Component.text("Not cancelled"), Component.text(packetName)));
                    } else {
                        cancelList.add(packetName);
                        player.showTitle(Title.title(Component.text("Cancelled"), Component.text(packetName)));
                    }
                    PoaPacketListener.config.set("Cancel", cancelList);
                }
                case "broadcast" -> {
                    String packetName = nameToPacketName(args[1]);
                    if(packetName == null){
                        player.showTitle(Title.title(Component.text(args[1]), Component.text("No packet found")));
                        return false;
                    }

                    if (broadcastList.contains(packetName)) {
                        broadcastList.remove(packetName);
                        player.showTitle(Title.title(Component.text("Broadcasting"), Component.text(packetName)));
                    } else {
                        broadcastList.add(packetName);
                        player.showTitle(Title.title(Component.text("Not broadcasting"), Component.text(packetName)));
                    }
                    PoaPacketListener.config.set("Broadcast", broadcastList);
                }
                case "toggle" -> {
                    enabled = !enabled;
                    PoaPacketListener.config.set("Enabled", enabled);

                    player.showTitle(Title.title(Component.text("Plugin state: " + enabled), Component.text("")));
                }
                case "clear" -> {
                    broadcastList.clear();
                    cancelList.clear();

                    PoaPacketListener.config.set("Broadcast", broadcastList);
                    PoaPacketListener.config.set("Cancel", cancelList);
                    listenList.clear();

                    player.showTitle(Title.title(Component.text("Plugin reset"), Component.text("")));
                }
                case "listen" -> {
                    String packetName = nameToPacketName(args[1]);
                    if(packetName == null){
                        player.showTitle(Title.title(Component.text(args[1]), Component.text("No packet found")));
                        return false;
                    }

                    if (listenList.contains(packetName)) {
                        listenList.remove(packetName);
                        player.showTitle(Title.title(Component.text("No Longer Listening"), Component.text(packetName)));
                    } else {
                        listenList.add(packetName);
                        player.showTitle(Title.title(Component.text("Listening"), Component.text(packetName)));
                    }
                }
            }
            PoaPacketListener.INSTANCE.saveConfig();

        }
        return false;
    }

    public static String nameToPacketName(String name) {
        for (String packetName : PacketHandler121.packetNames) {
            if (packetName.toLowerCase().contains(name)) {
                return packetName;
            }
        }
        return null;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        List<String> tr = new ArrayList<>();

        switch (args.length){
            case 1 -> list = List.of("listen", "toggle", "clear", "broadcast", "cancel");
            case 2 -> {
                if(!List.of("toggle", "clear").contains(args[1].toLowerCase()))
                    list = PacketHandler121.packetNames;
            }
        }


        for (String s : list) {
            if(s.toLowerCase().startsWith(args[args.length -1].toLowerCase()))
                tr.add(s);
        }

        return tr;
    }
}
