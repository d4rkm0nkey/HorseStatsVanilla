package monkey.lumpy.horse.stats.vanilla.gui;

public class Tooltip extends StatLines {
    public Tooltip(double speed, double jump, int health) {
        add("➟", speed, rate(speed, config.getGoodHorseSpeedValue(), config.getBadHorseSpeedValue()));
        add("⇮", jump, rate(jump, config.getGoodHorseJumpValue(), config.getBadHorseJumpValue()));
        add("♥", health, rate(health, config.getGoodHorseHeartsValue(), config.getBadHorseHeartsValue()));
    }
}
