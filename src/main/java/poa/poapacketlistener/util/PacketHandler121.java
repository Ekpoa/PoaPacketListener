package poa.poapacketlistener.util;


import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import poa.poapacketlistener.PoaPacketListener;
import poa.poapacketlistener.commands.PoaPacket;

import java.util.ArrayList;
import java.util.List;


public class PacketHandler121 extends ChannelDuplexHandler {

    //private List<ClientboundSystemChatPacket> list = new ArrayList<>();

    Player player;

    public static List<String> packetNames = new ArrayList<>();


    public PacketHandler121(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (!(msg instanceof Packet<?> packet)) {
                super.channelRead(ctx, msg);
                return;
            }
            if(!PoaPacket.enabled){
                super.channelRead(ctx, msg);
                return;
            }

            final String name = packet.getClass().getName();
            if(!packetNames.contains(name))
                packetNames.add(name);

            if(PoaPacket.cancelList.contains(name))
                return;


            String message = "";

            if(!PoaPacket.listenList.isEmpty() && PoaPacket.listenList.contains(name)){
                message = "<gray>" + player.getName() + " : <yellow>" + name;
            }

            if(!PoaPacket.broadcastList.contains(name) && PoaPacket.listenList.isEmpty()) {
                message = "<gray>" + player.getName() + " : <green>" + name;
            }




            if (!message.equalsIgnoreCase(""))
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(message));

            super.channelRead(ctx, msg);
        } catch (Exception e) {
            e.printStackTrace();
            super.channelRead(ctx, msg);
        }
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            if (!(msg instanceof Packet<?> packet)) {
                super.write(ctx, msg, promise);
                return;
            }
            if(!PoaPacket.enabled){
                super.write(ctx, msg, promise);
                return;
            }

            final String name = packet.getClass().getName();

            if(name.contains("ClientboundSystemChatPacket")){
                super.write(ctx, msg, promise);
                return;
            }

            if(!packetNames.contains(name))
                packetNames.add(name);

            if(PoaPacket.cancelList.contains(name))
                return;

            String message = "";

            if(!PoaPacket.listenList.isEmpty() && PoaPacket.listenList.contains(name)){
                message = "<gray>" + player.getName() + " : <yellow>" + name;
            }

            if(!PoaPacket.broadcastList.contains(name) && PoaPacket.listenList.isEmpty()) {
                message = "<gray>" + player.getName() + " : <green>" + name;
            }



            if (PoaPacket.listenList.contains(name) && packet instanceof ClientboundMoveEntityPacket p)
                message = message + " " + "Xa: " + p.getXa() + " Ya: " + p.getYa() + "Za: " + p.getZa() + " xRot: " + p.getxRot() + " yRot: " + p.getyRot();







            if (!message.equalsIgnoreCase(""))
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(message));



            super.write(ctx, msg, promise);
        } catch (Exception e) {
            e.printStackTrace();
            super.write(ctx, msg, promise);
        }
    }


}
