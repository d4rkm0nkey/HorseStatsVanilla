package monkey.lumpy.horse.stats.vanilla.util;

public class Converter {
    public static double jumpStrengthToJumpHeight(double strength) {
        return -0.9078526641492174 * strength * strength * strength + 5.380296913832499 * strength * strength + 0.8696246225981936 * strength - 0.04348078381106464;
    }

    public static double genericSpeedToBlocPerSec(double speed) {
        return 42.15747598316077 * speed;
    }
}
