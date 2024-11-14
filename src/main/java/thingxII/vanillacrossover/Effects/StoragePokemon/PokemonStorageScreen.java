package thingxII.vanillacrossover.Effects.StoragePokemon;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PokemonStorageScreen extends ContainerScreen<PokemonStorageContainer> {
    private static final int HEADER_HEIGHT = 20;

    private final int xSize, ySize;
    private final ResourceLocation backgroundLocation;

    public PokemonStorageScreen(PokemonStorageContainer container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
        backgroundLocation = container.storage.data.background;
        xSize = container.storage.data.screenSizeX;
        ySize = container.storage.data.screenSizeY;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        minecraft.getTextureManager().bind(backgroundLocation);

        blit(matrixStack, x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, ItemStack stack, int x, int y) {
        if (!stack.isEmpty()) {
            super.renderTooltip(matrixStack, stack, x, y);
        }
    }

    // Hides the "Inventory" label that tends to be in the incorrect position.
    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) { }
}
