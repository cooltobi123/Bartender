package com.drunkshulker.bartender.mixins.client;

import com.drunkshulker.bartender.Bartender;
import com.drunkshulker.bartender.util.salhack.events.blocks.EventCanCollideCheck;
import com.drunkshulker.bartender.util.salhack.events.liquid.EventLiquidCollisionBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid
{
    @Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    public void canCollideCheck(final IBlockState blockState, final boolean b, final CallbackInfoReturnable<Boolean> callbackInfoReturnable)
    {
        EventCanCollideCheck l_Event = new EventCanCollideCheck();
        Bartender.EVENT_BUS.post(l_Event);
        callbackInfoReturnable.setReturnValue(l_Event.isCancelled());
    }
    
    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, final CallbackInfoReturnable<AxisAlignedBB> callbackInfoReturnable)
    {
        EventLiquidCollisionBB l_Collision = new EventLiquidCollisionBB(pos);
        Bartender.EVENT_BUS.post(l_Collision);
        if (l_Collision.isCancelled())
        {
            callbackInfoReturnable.setReturnValue(l_Collision.getBoundingBox());
            callbackInfoReturnable.cancel();
        }
    }
}
