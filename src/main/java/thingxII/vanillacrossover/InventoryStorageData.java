package thingxII.vanillacrossover;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class InventoryStorageData {
    public static HashMap<StorageType, InventoryStorageData> Data = new HashMap();

    static {
        Data.put(StorageType.VERY_SMALL, new InventoryStorageData(
                "verysmall",
                9,
                176, 124,
                9,
                1,
                8, 41+21+1,
                8, 99+21+1,
                8, 22+21-2
        ));

        Data.put(StorageType.SMALL, new InventoryStorageData(
                "small",
                18,
                176, 142,
                9,
                2,
                8, 59+14-1,
                8, 117+14-1,
                8, 20+14-2
        ));

        Data.put(StorageType.MEDIUM, new InventoryStorageData(
                "medium",
                27,
                176, 160,
                9,
                3,
                8, 77+4,
                8, 135+4,
                8, 19+4
        ));

        Data.put(StorageType.LARGE, new InventoryStorageData(
                "large",
                54,
                176, 214,
                9,
                6,
                8, 131-23,
                8, 189-23,
                8, 19-23
        ));

        Data.put(StorageType.VERY_LARGE, new InventoryStorageData(
                "verylarge",
                104,
                256, 250,
                13,
                8,
                47-40, 167-41,
                47-40, 226-42,
                11-40, 20-42
        ));
    }

    public final ResourceLocation background;
    public final int slotCount;
    public final int slotsPerRow;
    public final int numberOfRows;

    public final int screenSizeX;
    public final int screenSizeY;

    public final int playerInventoryX;
    public final int playerInventoryY;

    public final int playerHotbarX;
    public final int playerHotbarY;

    public final int storageInventoryX;
    public final int storageInventoryY;

    InventoryStorageData(String storageName, int slotCount, int screenSizeX, int screenSizeY, int slotsPerRow, int numberOfRows, int playerInventoryX, int playerInventoryY, int playerHotbarX, int playerHotbarY, int storageInventoryX, int storageInventoryY) {
        background = new ResourceLocation("vanillacrossover", "textures/gui/" + storageName + "_inventory_bg.png");
        this.slotCount = slotCount;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.slotsPerRow = slotsPerRow;
        this.numberOfRows = numberOfRows;
        this.playerInventoryX = playerInventoryX;
        this.playerInventoryY = playerInventoryY;
        this.playerHotbarX = playerHotbarX;
        this.playerHotbarY = playerHotbarY;
        this.storageInventoryX = storageInventoryX;
        this.storageInventoryY = storageInventoryY;
    }

}
