package poa.poapacketlistener.util;

import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import poa.poapacketlistener.PoaPacketListener;

import java.util.logging.Level;

public class PacketInjector121 {


    CraftPlayer craftPlayer;
    Player player;
    String id;

    public PacketInjector121(Player player){
        this.craftPlayer = (CraftPlayer) player;
        this.player = player;
        this.id = player.getName() + "-PacketListener-";
    }

    public void inject(Player player) {
        PoaPacketListener.INSTANCE.getLogger().log(Level.INFO, "Injected " + player.getName());
        ChannelPipeline pipeline = getChannelPipeline((CraftPlayer) player);
        pipeline.addBefore("packet_handler", id, new PacketHandler121(player));
    }

    private static ChannelPipeline getChannelPipeline(CraftPlayer player) {
        PoaPacketListener.INSTANCE.getLogger().log(Level.INFO, "Got pipeline for " + player.getName());
        return player.getHandle().connection.connection.channel.pipeline();
    }

//    public void injectPlayer() {
//        final ServerConnectionListener serverConnection = MinecraftServer.getServer().getConnection();
//
//        List<Connection> connections = serverConnection.getConnections();
//
//
//        final Connection playerConnection = connections.stream()
//                .filter(connection -> connection.getRemoteAddress() instanceof InetSocketAddress)
//                .filter(connection -> ((InetSocketAddress) connection.getRemoteAddress()).getAddress() == address)
//                .findAny().orElseThrow(IllegalArgumentException::new);
//        playerConnection.channel.pipeline().addBefore(
//                "packet_handler", id, new PacketHandler1206(this.player));
//
//        System.out.println("Injected packet listener into " + this.player.getName());
//
//    }
//
    public void uninjectPlayer() {
        if (this.craftPlayer.getHandle().connection.connection.channel.pipeline().get(id) != null) {
            this.craftPlayer.getHandle().connection.connection.channel.pipeline().remove(id);
            System.out.println("Uninjected packet listener into " + this.player.getName());
        }
    }
}