package monkey.lumpy.horse.stats.vanilla.util;

public class Converter {
    public static double jumpStrengthToJumpHeight(double strength) {
        return -0.1817584952 * strength * strength * strength + 3.689713992 * strength * strength + 2.128599134 * strength - 0.343930367;
    }

    public static double genericSpeedToBlocPerSec(double speed) {
        return 0.132 * speed * speed + 42.119 * speed;
    }
}