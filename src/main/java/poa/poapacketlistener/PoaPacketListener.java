package poa.poapacketlistener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import poa.poapacketlistener.commands.PoaPacket;
import poa.poapacketlistener.events.JoinLeave;

public final class PoaPacketListener extends JavaPlugin {

    public static PoaPacketListener INSTANCE;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();

        config = getConfig();

        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinLeave(), this);

        getCommand("poapacket").setExecutor(new PoaPacket());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
