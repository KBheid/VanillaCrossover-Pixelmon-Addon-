package thingxII.vanillacrossover.mixin;

import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AbstractFurnaceTileEntity.class, remap = false)
public interface Patch_FurnaceDataAccessor {
    @Accessor("dataAccess")
    public IIntArray getDataAccess();
}
