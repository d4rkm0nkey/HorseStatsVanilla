package monkey.lumpy.horse.stats.vanilla.gametest;

import static monkey.lumpy.horse.stats.vanilla.gui.StatLines.Stat.HEALTH;
import static monkey.lumpy.horse.stats.vanilla.gui.StatLines.Stat.JUMP;
import static monkey.lumpy.horse.stats.vanilla.gui.StatLines.Stat.SPEED;
import static monkey.lumpy.horse.stats.vanilla.gui.StatLines.Stat.STRENGTH;

import java.util.List;

import monkey.lumpy.horse.stats.vanilla.HorseStatsVanilla;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.StatLines;
import monkey.lumpy.horse.stats.vanilla.gui.StatLines.Line;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.network.chat.Component;

/**
 * What the mod shows for each kind of mount: values, colors and vanilla ranges, read from the
 * stats the client gets synced. The default limits color above 11 / 4 / 25 (speed / jump /
 * hearts) and 9 slots good, below 7 / 2.5 / 20 and 6 slots bad.
 */
public class StatLinesTest implements FabricClientGameTest {
    private static final String SPEED_RANGE = "4.7-14.2";
    private static final String JUMP_RANGE = "1.1-5.3";
    private static final String HEALTH_RANGE = "15-30";
    private static final String SLOTS_RANGE = "3-15";

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            ModConfig config = HorseStatsVanilla.config();
            int good = config.getGoodColor();
            int neutral = config.getNeutralColor();
            int bad = config.getBadColor();

            int bestHorse = Mounts.summon(context, world, "horse", Mounts.pos(0), Mounts.stats(0.3375, 1.0, 30));
            expect(context, "the best horse", bestHorse, List.of(
                new Line(SPEED, "14.2", good, SPEED_RANGE),
                new Line(JUMP, "5.3", good, JUMP_RANGE),
                new Line(HEALTH, "30", good, HEALTH_RANGE)));

            int worstHorse = Mounts.summon(context, world, "horse", Mounts.pos(1), Mounts.stats(0.1125, 0.4, 15));
            expect(context, "the worst horse", worstHorse, List.of(
                new Line(SPEED, "4.7", bad, SPEED_RANGE),
                new Line(JUMP, "1.1", bad, JUMP_RANGE),
                new Line(HEALTH, "15", bad, HEALTH_RANGE)));

            int averageHorse = Mounts.summon(context, world, "horse", Mounts.pos(2), Mounts.stats(0.2, 0.7, 22));
            expect(context, "an average horse", averageHorse, List.of(
                new Line(SPEED, "8.4", neutral, SPEED_RANGE),
                new Line(JUMP, "2.9", neutral, JUMP_RANGE),
                new Line(HEALTH, "22", neutral, HEALTH_RANGE)));

            // Vanilla only rolls the health of donkeys, speed and jump are the same for all
            int donkey = Mounts.summon(context, world, "donkey", Mounts.pos(3), Mounts.stats(0.175, 0.5, 20));
            expect(context, "a donkey", donkey, List.of(
                new Line(SPEED, "7.4", neutral, null),
                new Line(JUMP, "1.6", bad, null),
                new Line(HEALTH, "20", neutral, HEALTH_RANGE)));

            // 6 slots are average for the strength limits, but would count as good for the jump limits
            int weakLlama = Mounts.summon(context, world, "llama", Mounts.pos(4), Mounts.stats(0.175, 0.5, 20) + ",Strength:2");
            expect(context, "a llama of strength 2", weakLlama, List.of(
                new Line(SPEED, "7.4", neutral, null),
                new Line(JUMP, "1.6", bad, null),
                new Line(HEALTH, "20", neutral, HEALTH_RANGE),
                new Line(STRENGTH, "6", neutral, SLOTS_RANGE)));

            int strongLlama = Mounts.summon(context, world, "llama", Mounts.pos(5), Mounts.stats(0.175, 0.5, 20) + ",Strength:5");
            expectLine(context, "a llama of strength 5", strongLlama, new Line(STRENGTH, "15", good, SLOTS_RANGE));

            int weakestLlama = Mounts.summon(context, world, "llama", Mounts.pos(6), Mounts.stats(0.175, 0.5, 20) + ",Strength:1");
            expectLine(context, "a llama of strength 1", weakestLlama, new Line(STRENGTH, "3", bad, SLOTS_RANGE));

            // Skeleton horses only roll their jump strength
            int skeletonHorse = Mounts.summon(context, world, "skeleton_horse", Mounts.pos(7), Mounts.stats(0.2, 0.7, 15));
            expect(context, "a skeleton horse", skeletonHorse, List.of(
                new Line(SPEED, "8.4", neutral, null),
                new Line(JUMP, "2.9", neutral, JUMP_RANGE),
                new Line(HEALTH, "15", bad, null)));

            // Hover tooltips: value with unit, vanilla range (if any) and the color limits
            expectTooltip(context, "the best horse", bestHorse, SPEED,
                "Speed: 14.2 blocks/s", "Vanilla range: 4.7-14.2", "Good above 11, bad below 7");
            expectTooltip(context, "the worst horse", worstHorse, HEALTH,
                "Health: 15 (7.5 hearts)", "Vanilla range: 15-30", "Good above 25, bad below 20");
            expectTooltip(context, "a donkey", donkey, JUMP,
                "Jump height: 1.6 blocks", "Good above 4, bad below 2.5");
            expectTooltip(context, "a llama of strength 2", weakLlama, STRENGTH,
                "Inventory: 6 slots", "Vanilla range: 3-15", "Good above 9, bad below 6");

            int camel = Mounts.summon(context, world, "camel", Mounts.pos(8), "");
            StatLines camelStats = context.computeOnClient(minecraft -> StatLines.of(Mounts.client(minecraft, camel)));
            if (camelStats != null) {
                throw new AssertionError("A camel should show no stats, got " + camelStats.getLines());
            }

            Mounts.config("useColors", false);
            try {
                expect(context, "the best horse without colors", bestHorse, List.of(
                    new Line(SPEED, "14.2", neutral, SPEED_RANGE),
                    new Line(JUMP, "5.3", neutral, JUMP_RANGE),
                    new Line(HEALTH, "30", neutral, HEALTH_RANGE)));
                expectTooltip(context, "the best horse without colors", bestHorse, SPEED,
                    "Speed: 14.2 blocks/s", "Vanilla range: 4.7-14.2");
            } finally {
                Mounts.config("useColors", true);
            }
        }
    }

    private static List<Line> lines(ClientGameTestContext context, String mount, int id) {
        StatLines stats = context.computeOnClient(minecraft -> StatLines.of(Mounts.client(minecraft, id)));
        if (stats == null) {
            throw new AssertionError("No stats for " + mount);
        }
        return stats.getLines();
    }

    private static void expect(ClientGameTestContext context, String mount, int id, List<Line> expected) {
        List<Line> actual = lines(context, mount, id);
        if (!actual.equals(expected)) {
            throw new AssertionError("Wrong stats for " + mount + ":\n  expected " + describe(expected) + "\n  but got  " + describe(actual));
        }
    }

    private static void expectLine(ClientGameTestContext context, String mount, int id, Line expected) {
        List<Line> actual = lines(context, mount, id);
        if (!actual.contains(expected)) {
            throw new AssertionError("Wrong stats for " + mount + ": expected " + describe(List.of(expected)) + " in " + describe(actual));
        }
    }

    private static void expectTooltip(ClientGameTestContext context, String mount, int id, StatLines.Stat stat, String... expected) {
        List<String> actual = context.computeOnClient(minecraft -> StatLines.of(Mounts.client(minecraft, id)).getTooltip(stat).stream()
            .map(Component::getString).toList());
        if (!actual.equals(List.of(expected))) {
            throw new AssertionError("Wrong " + stat + " tooltip for " + mount + ":\n  expected " + List.of(expected) + "\n  but got  " + actual);
        }
    }

    /** Like "JUMP 1.6 #FF0000 (1.1-5.3)", with readable colors. */
    private static String describe(List<Line> lines) {
        return lines.stream()
            .map(line -> String.format("%s %s #%06X (%s)", line.stat(), line.value(), line.color() & 0xFFFFFF, line.range()))
            .toList().toString();
    }
}
