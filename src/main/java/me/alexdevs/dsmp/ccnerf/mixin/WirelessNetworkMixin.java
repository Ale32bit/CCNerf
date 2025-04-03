package me.alexdevs.dsmp.ccnerf.mixin;

import dan200.computercraft.api.network.Packet;
import dan200.computercraft.api.network.PacketReceiver;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessNetwork;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WirelessNetwork.class)
public abstract class WirelessNetworkMixin {
    /**
     * @author Alessandro "AlexDevs" Proto
     * @reason Nerfing the ender modem.
     */
    @Overwrite(remap = false)
    private static void tryTransmit(PacketReceiver receiver, Packet packet, double range, boolean interdimensional) {
        var sender = packet.sender();
        if (receiver.getLevel() == sender.getLevel()) {
            var receiveRange = Math.max(range, receiver.getRange()); // Ensure range is symmetrical
            var distanceSq = receiver.getPosition().distanceToSqr(sender.getPosition());
            if (distanceSq <= receiveRange * receiveRange) {
                receiver.receiveSameDimension(packet, Math.sqrt(distanceSq));
            }
        } else {
            // Only ender wireless modems are capable of sending to other dimensions, but only if the block position is the same.
            if (interdimensional) {
                var distanceSq = receiver.getPosition().distanceToSqr(sender.getPosition());
                if(distanceSq <= 1d) {
                    receiver.receiveDifferentDimension(packet);
                }
            }
        }
    }
}
