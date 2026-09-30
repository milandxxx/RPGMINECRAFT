package com.aventura.rpg.mixin;

import com.aventura.rpg.profile.RpgProfile;
import com.aventura.rpg.profile.RpgProfileHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements RpgProfileHolder {
    @Unique private final RpgProfile aventuraRpg$profile = new RpgProfile();
    @Override public RpgProfile aventuraRpg$getProfile() { return aventuraRpg$profile; }
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void aventuraRpg$writeProfile(NbtCompound nbt, CallbackInfo ci) { aventuraRpg$profile.writeNbt(nbt); }
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void aventuraRpg$readProfile(NbtCompound nbt, CallbackInfo ci) { aventuraRpg$profile.readNbt(nbt); }
}
