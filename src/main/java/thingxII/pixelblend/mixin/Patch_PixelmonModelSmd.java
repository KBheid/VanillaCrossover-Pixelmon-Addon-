package thingxII.pixelblend.mixin;

import com.pixelmonmod.pixelmon.client.models.*;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimation;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.entities.pixelmon.AbstractClientEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DualModelFactory.TransparentImpl.class, remap = false)
public abstract class Patch_PixelmonModelSmd<M extends AbstractClientEntity> extends PixelmonModelBase<M> {
    @Shadow
    protected ValveStudioModel model2;

    @Inject(method = "updateAnimation", at = @At("HEAD"), cancellable = true)
    protected void updateAnimation(IncrementingVariable variable, CallbackInfo ci) {
        DualModelFactory.TransparentImpl<M> casted = (DualModelFactory.TransparentImpl<M>) (Object) this;

        if (casted.theModel == null || casted.theModel.currentSequence == null) {
            casted.theModel.animate();
            this.model2.animate();
            ci.cancel();
            return;
        }

        IncrementingVariable ghostClone = variable.makeGhostClone();
        SmdAnimation animation = casted.theModel.currentSequence.checkForIncrement(variable);
        int frame = (int) Math.floor(variable.value % (float) animation.totalFrames);
        animation.setCurrentFrame(frame);
        animation = this.model2.currentSequence.checkForIncrement(ghostClone);
        frame = (int) Math.floor(variable.value % (float) animation.totalFrames);
        animation.setCurrentFrame(frame);
        Minecraft.getInstance().getProfiler().push("pixelmon_animate");
        casted.theModel.animate();
        this.model2.animate();
        Minecraft.getInstance().getProfiler().pop();

        ci.cancel();
    }

}