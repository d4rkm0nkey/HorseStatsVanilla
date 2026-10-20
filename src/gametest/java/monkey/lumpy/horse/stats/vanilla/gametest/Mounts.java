package monkey.lumpy.horse.stats.vanilla.gametest;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.StatLines;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Mounts for the tests, summoned with fixed stats instead of the random ones vanilla rolls.
 * They are summoned by command, which reads the same in every Minecraft version.
 */
final class Mounts {
    private static final AtomicInteger TAGS = new AtomicInteger();

    private Mounts() {}

    /** A spot on the superflat test world, {@code i} mounts apart from the first. */
    static BlockPos pos(int i) {
        return new BlockPos(-20 + 5 * i, -60, 6);
    }

    /** Summons a mount that stands still; returns its entity id, which the client shares. */
    static int summon(ClientGameTestContext context, TestSingleplayerContext world, String type, BlockPos pos, String nbt) {
        String tag = "horsestatsvanilla_test_" + TAGS.incrementAndGet();
        world.getServer().runCommand(String.format(Locale.ROOT, "summon minecraft:%s %d %d %d {NoAI:1b,Tags:[\"%s\"]%s}",
            type, pos.getX(), pos.getY(), pos.getZ(), tag, nbt.isEmpty() ? "" : "," + nbt));
        int id = world.getServer().computeOnServer(server -> {
            for (Entity entity : server.getOverworld().iterateEntities()) {
                if (entity.getCommandTags().contains(tag)) {
                    return entity.getId();
                }
            }
            throw new AssertionError("Could not summon a " + type + " with " + nbt);
        });
        // The client sees the mount (and its synced stats) a moment later
        context.waitFor(minecraft -> minecraft.world.getEntityById(id) != null);
        return id;
    }

    /** NBT for the base speed and jump strength attributes and the max health, as vanilla stores them. */
    static String stats(double speed, double jump, double health) {
        return String.format(Locale.ROOT, "attributes:[{id:\"minecraft:movement_speed\",base:%sd},"
            + "{id:\"minecraft:jump_strength\",base:%sd},{id:\"minecraft:max_health\",base:%sd}],Health:%sf",
            speed, jump, health, health);
    }

    /** The client's copy of the mount, call on the client thread. */
    static AbstractHorseEntity client(MinecraftClient minecraft, int id) {
        return (AbstractHorseEntity) minecraft.world.getEntityById(id);
    }

    /** Sets a config option for the following checks; the config has no setters, the options screen writes the fields. */
    static void config(String option, boolean value) {
        try {
            Field field = ModConfig.class.getDeclaredField(option);
            field.setAccessible(true);
            field.setBoolean(StatLines.config(), value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No config option " + option, e);
        }
    }
}
