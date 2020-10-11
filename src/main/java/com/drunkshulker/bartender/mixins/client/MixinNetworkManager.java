package com.drunkshulker.bartender.mixins.client;

import com.drunkshulker.bartender.Bartender;
import com.drunkshulker.bartender.util.salhack.events.EventNetworkPacketEvent;
import com.drunkshulker.bartender.util.salhack.events.EventNetworkPostPacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

@Mixin(NetworkManager.class)
public class MixinNetworkManager
{
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> p_Packet, CallbackInfo callbackInfo)
    {
        EventNetworkPacketEvent l_Event = new EventNetworkPacketEvent(p_Packet);
        Bartender.EVENT_BUS.post(l_Event);

        if (l_Event.isCancelled())
        {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> p_Packet, CallbackInfo callbackInfo)
    {
        EventNetworkPacketEvent l_Event = new EventNetworkPacketEvent(p_Packet);
        Bartender.EVENT_BUS.post(l_Event);

        if (l_Event.isCancelled())
        {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("RETURN"))
    private void onPostSendPacket(Packet<?> p_Packet, CallbackInfo callbackInfo)
    {
        Bartender.EVENT_BUS.post(new EventNetworkPostPacketEvent(p_Packet));
    }

    @Inject(method = "channelRead0", at = @At("RETURN"))
    private void onPostChannelRead(ChannelHandlerContext context, Packet<?> p_Packet, CallbackInfo callbackInfo)
    {
        Bartender.EVENT_BUS.post(new EventNetworkPostPacketEvent(p_Packet));
    }
}
