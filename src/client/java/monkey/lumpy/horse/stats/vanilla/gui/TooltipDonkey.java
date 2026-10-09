package monkey.lumpy.horse.stats.vanilla.gui;

public class TooltipDonkey extends StatLines {
    public TooltipDonkey(double speed, double jump, int health, int strength) {
        add("➟", speed, rate(speed, config.getGoodHorseSpeedValue(), config.getBadHorseSpeedValue()));
        add("⇮", jump, rate(jump, config.getGoodHorseJumpValue(), config.getBadHorseJumpValue()));
        add("♥", health, rate(health, config.getGoodHorseHeartsValue(), config.getBadHorseHeartsValue()));
        add("▦", strength, rate(strength, config.getGoodStrengthValue(), config.getBadStrengthValue()));
    }
}
