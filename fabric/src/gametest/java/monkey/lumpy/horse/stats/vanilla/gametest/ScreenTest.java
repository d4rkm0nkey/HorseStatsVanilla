package monkey.lumpy.horse.stats.vanilla.gametest;

import monkey.lumpy.horse.stats.vanilla.ModMenuApiImpl;
import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.equine.AbstractHorse;

/**
 * Opens the screens of the mod the way a player does: the inventory of tamed mounts in each
 * layout, and the panel on shift + right click on untamed ones. Rendering them must not fail;
 * whether they look right is for a person to judge from the screenshots
 * ({@code fabric/build/run/clientGameTest/screenshots}).
 */
public class ScreenTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        // The options screen as Mod Menu opens it, from the title screen
        context.setScreen(() -> new ModMenuApiImpl().getModConfigScreenFactory().create(null));
        context.waitTicks(5);
        context.takeScreenshot("options");
        context.setScreen(() -> null);

        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            world.getConnection().waitForChunksRender();
            world.getServer().runCommand("time set noon");

            int horse = Mounts.summon(context, world, "horse", Mounts.pos(0), Mounts.stats(0.3375, 0.7, 18) + ",Tame:1b");
            inventory(context, world, "horse", horse);
            Mounts.config("showMaxMin", true);
            inventory(context, world, "horse_max_min", horse);
            Mounts.config("valueUp", true);
            inventory(context, world, "horse_values_up_max_min", horse);
            Mounts.config("valueUp", false);
            // The speed value, in window coordinates of the 176 x 166 inventory
            inventory(context, world, "horse_hover_speed", horse, 96, 29);

            int donkey = Mounts.summon(context, world, "donkey", Mounts.pos(1), Mounts.stats(0.175, 0.5, 26) + ",Tame:1b,ChestedHorse:1b");
            inventory(context, world, "donkey_chest", donkey);

            int llama = Mounts.summon(context, world, "llama", Mounts.pos(2), Mounts.stats(0.175, 0.5, 20) + ",Tame:1b,Strength:4");
            inventory(context, world, "llama_max_min", llama);
            int chestLlama = Mounts.summon(context, world, "llama", Mounts.pos(3), Mounts.stats(0.175, 0.5, 20) + ",Tame:1b,Strength:4,ChestedHorse:1b");
            inventory(context, world, "llama_chest", chestLlama);
            Mounts.config("valueUp", true);
            inventory(context, world, "llama_values_up_max_min", llama);
            Mounts.config("valueUp", false);
            Mounts.config("showMaxMin", false);

            int camel = Mounts.summon(context, world, "camel", Mounts.pos(4), "Tame:1b");
            inventory(context, world, "camel_no_stats", camel);

            BlockPos wildHorse = Mounts.pos(5);
            Mounts.summon(context, world, "horse", wildHorse, Mounts.stats(0.1125, 0.4, 22));
            panel(context, world, "panel_horse", wildHorse);
            BlockPos wildLlama = Mounts.pos(6);
            Mounts.summon(context, world, "llama", wildLlama, Mounts.stats(0.175, 0.5, 20) + ",Strength:4");
            panel(context, world, "panel_llama", wildLlama);
            BlockPos hoverHorse = Mounts.pos(7);
            Mounts.summon(context, world, "horse", hoverHorse, Mounts.stats(0.2, 0.7, 23));
            panel(context, world, "panel_hover_health", hoverHorse, true);
        }
    }

    /** Opens the inventory of a tamed mount, as on shift + right click, and takes a screenshot. */
    private static void inventory(ClientGameTestContext context, TestSingleplayerContext world, String name, int id) {
        inventory(context, world, name, id, -1, -1);
    }

    /** Same, with the mouse at ({@code x}, {@code y}) in the inventory window, or off it for negative values. */
    private static void inventory(ClientGameTestContext context, TestSingleplayerContext world, String name, int id, int x, int y) {
        world.getServer().runOnServer(server -> {
            ServerPlayer player = server.getPlayerList().getPlayers().getFirst();
            AbstractHorse mount = (AbstractHorse) server.overworld().getEntity(id);
            player.teleportTo(mount.getX(), mount.getY(), mount.getZ() - 2.5);
            mount.openCustomInventoryScreen(player);
        });
        context.waitForScreen(HorseInventoryScreen.class);
        if (x >= 0) {
            hover(context, x, y, 176, 166);
        } else {
            context.getInput().setCursorPos(0, 0);
        }
        context.waitTicks(5);
        context.takeScreenshot(name);
        context.setScreen(() -> null);
        context.waitTicks(2);
    }

    /** Shift + right clicks an untamed mount: the panel opens, and closes again with shift. */
    private static void panel(ClientGameTestContext context, TestSingleplayerContext world, String name, BlockPos mount) {
        panel(context, world, name, mount, false);
    }

    /** Same, with the mouse on the health row of the panel ({@code hoverHealth}). */
    private static void panel(ClientGameTestContext context, TestSingleplayerContext world, String name, BlockPos mount, boolean hoverHealth) {
        world.getServer().runOnServer(server -> server.getPlayerList().getPlayers().getFirst()
            .teleportTo(mount.getX() + 0.5, mount.getY(), mount.getZ() - 2.0));
        context.waitTicks(5);
        context.getInput().lookAt(mount);
        context.waitTicks(2);
        context.getInput().holdKey(options -> options.keyShift);
        context.waitTicks(2);
        context.getInput().pressKey(options -> options.keyUse);
        context.waitForScreen(ToolTipGui.class);
        if (hoverHealth) {
            // The middle of the third row: the panel is centered and 38 pixels high for three rows
            hover(context, 0, 29, 0, 38);
        }
        context.waitTicks(3);
        context.takeScreenshot(name);
        context.getInput().releaseKey(options -> options.keyShift);
        context.waitForScreen(null);
        context.waitTicks(2);
    }

    /**
     * Moves the mouse to ({@code x}, {@code y}) in a window of the given size centered on the
     * screen; width 0 means the screen's horizontal center.
     */
    private static void hover(ClientGameTestContext context, int x, int y, int width, int height) {
        double[] cursor = context.computeOnClient(minecraft -> {
            int scale = minecraft.getWindow().getGuiScale();
            int left = width == 0 ? minecraft.getWindow().getGuiScaledWidth() / 2 : (minecraft.getWindow().getGuiScaledWidth() - width) / 2;
            int top = (minecraft.getWindow().getGuiScaledHeight() - height) / 2;
            return new double[] {(left + x) * scale, (top + y) * scale};
        });
        context.getInput().setCursorPos(cursor[0], cursor[1]);
    }
}
