package poa.poapacketlistener.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import poa.poapacketlistener.util.PacketHandler121;
import poa.poapacketlistener.util.PacketInjector121;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class JoinLeave implements Listener {

    private static final Map<Player, PacketInjector121> map = new HashMap<>();

    @EventHandler
    public void join(PlayerJoinEvent e){
        final Player player = e.getPlayer();
        final PacketInjector121 value = new PacketInjector121(player);
        value.inject(player);
        map.put(player, value);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        map.get(player).uninjectPlayer();
    }
}
