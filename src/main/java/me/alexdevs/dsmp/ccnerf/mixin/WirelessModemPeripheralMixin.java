package me.alexdevs.dsmp.ccnerf.mixin;

import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WirelessModemPeripheral.class, remap = false)
public abstract class WirelessModemPeripheralMixin {
    @Unique
    private static final double ADVANCED_RANGE_MULTIPLIER = 2d;

    @Final
    @Shadow
    private boolean advanced;

    @Inject(method = "isInterdimensional", at = @At("HEAD"), cancellable = true)
    private void ccnerf$overrideIsInterdimensional(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Redirect(
            method = "getRange",
            at = @At(
                    value = "FIELD",
                    target = "Ldan200/computercraft/shared/peripheral/modem/wireless/WirelessModemPeripheral;advanced:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean ccnerf$overrideGetRangeAdvanced(WirelessModemPeripheral instance) {
        return false;
    }

    @Inject(method = "getRange", at = @At("RETURN"), cancellable = true)
    private void ccnerf$getRangeValue(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(cir.getReturnValue() * (advanced ? ADVANCED_RANGE_MULTIPLIER : 1d));
    }
}
