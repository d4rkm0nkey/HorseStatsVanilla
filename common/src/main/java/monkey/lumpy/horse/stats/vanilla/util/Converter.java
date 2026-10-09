package monkey.lumpy.horse.stats.vanilla.util;

public class Converter {
    public static double jumpStrengthToJumpHeight(double strength) {
        double height = 0;
        double velocity = strength;
        while(velocity > 0) {
            height += velocity;
            velocity = (velocity - .08) * .98 * .98;
        }
        return height;
    }

    public static double genericSpeedToBlocPerSec(double speed) {
        return 42.157796 * speed;
    }
}
