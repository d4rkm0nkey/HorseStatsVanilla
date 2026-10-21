package monkey.lumpy.horse.stats.vanilla.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import org.jspecify.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * The options screen for Mod Menu and NeoForge's mod list, built from vanilla widgets so it needs no other mod.
 * Changes apply right away and are saved when the screen closes. The translation keys are the ones Cloth Config's
 * AutoConfig used, so the existing translations keep working.
 */
public final class ModConfigScreen extends Screen {
    private static final String PREFIX = "text.autoconfig.horsestatsvanilla.";
    private static final int WIDGET_WIDTH = 100;
    private static final int INVALID_COLOR = 0xffff5555;
    private static final int VALID_COLOR = 0xffe0e0e0;

    private final @Nullable Screen parent;
    private final ModConfig config = ModConfig.get();
    private HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private @Nullable OptionList list;

    public ModConfigScreen(@Nullable Screen parent) {
        super(Component.translatable(PREFIX + "title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        layout = new HeaderAndFooterLayout(this);
        layout.addTitleHeader(title, font);
        list = layout.addToContents(new OptionList(minecraft, width, layout.getContentHeight(), layout.getHeaderHeight()));
        list.setRows(rows());
        LinearLayout footer = layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(Button.builder(Component.translatable("horsestatsvanilla.config.reset"), button -> {
            config.resetToDefaults();
            list.setRows(rows());
        }).width(150).build());
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(150).build());
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
        if (list != null) {
            list.updateSizeAndPosition(width, layout.getContentHeight(), layout.getHeaderHeight());
        }
    }

    @Override
    public void onClose() {
        config.save();
        minecraft.gui.setScreen(parent);
    }

    private List<Row> rows() {
        List<Row> rows = new ArrayList<>();
        rows.add(section("category.default"));
        rows.add(toggle("showValue", config.showValue, false, v -> config.showValue = v));
        rows.add(toggle("showMaxMin", config.showMaxMin, true, v -> config.showMaxMin = v));
        rows.add(toggle("enableTooltip", config.enableTooltip, true, v -> config.enableTooltip = v));
        rows.add(toggle("valueUp", config.valueUp, false, v -> config.valueUp = v));
        rows.add(toggle("useColors", config.useColors, true, v -> config.useColors = v));
        rows.add(color("goodColor", config.goodColor, v -> config.goodColor = v));
        rows.add(color("neutralColor", config.neutralColor, v -> config.neutralColor = v));
        rows.add(color("badColor", config.badColor, v -> config.badColor = v));

        rows.add(section("category.coloring"));
        rows.add(limit("goodHorseJumpValue", config.goodHorseJumpValue, v -> config.goodHorseJumpValue = v));
        rows.add(limit("badHorseJumpValue", config.badHorseJumpValue, v -> config.badHorseJumpValue = v));
        rows.add(limit("goodHorseSpeedValue", config.goodHorseSpeedValue, v -> config.goodHorseSpeedValue = v));
        rows.add(limit("badHorseSpeedValue", config.badHorseSpeedValue, v -> config.badHorseSpeedValue = v));
        rows.add(limit("goodHorseHeartsValue", config.goodHorseHeartsValue, v -> config.goodHorseHeartsValue = v));
        rows.add(limit("badHorseHeartsValue", config.badHorseHeartsValue, v -> config.badHorseHeartsValue = v));
        rows.add(limit("goodStrengthValue", config.goodStrengthValue, v -> config.goodStrengthValue = v));
        rows.add(limit("badStrengthValue", config.badStrengthValue, v -> config.badStrengthValue = v));
        return rows;
    }

    private Row section(String key) {
        return new Row(font, Component.translatable(PREFIX + key).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD), null, null, null);
    }

    private Row toggle(String option, boolean value, boolean tooltip, Consumer<Boolean> save) {
        CycleButton<Boolean> button = CycleButton.onOffBuilder(value).displayOnlyValue()
            .create(0, 0, WIDGET_WIDTH, 20, Component.empty(), (b, v) -> save.accept(v));
        return new Row(font, label(option), tooltip ? tooltip(option) : null, button, null);
    }

    /** A color as {@code #RRGGBB}; the config stores opaque ARGB. */
    private Row color(String option, int value, IntConsumer save) {
        EditBox box = new EditBox(font, 0, 0, WIDGET_WIDTH, 18, label(option));
        box.setMaxLength(7);
        box.setValue(String.format(Locale.ROOT, "#%06X", value & 0xffffff));
        int[] shown = {value};
        box.setResponder(text -> {
            Integer rgb = parseColor(text);
            box.setTextColor(rgb == null ? INVALID_COLOR : VALID_COLOR);
            if (rgb != null) {
                shown[0] = 0xff000000 | rgb;
                save.accept(shown[0]);
            }
        });
        return new Row(font, label(option), null, box, () -> shown[0]);
    }

    private Row limit(String option, float value, Consumer<Float> save) {
        EditBox box = new EditBox(font, 0, 0, WIDGET_WIDTH, 18, label(option));
        box.setMaxLength(10);
        box.setValue(formatNumber(value));
        box.setResponder(text -> {
            Float number = parseNumber(text);
            box.setTextColor(number == null ? INVALID_COLOR : VALID_COLOR);
            if (number != null) {
                save.accept(number);
            }
        });
        return new Row(font, label(option), tooltip(option), box, null);
    }

    private static Component label(String option) {
        return Component.translatable(PREFIX + "option." + option);
    }

    private static Component tooltip(String option) {
        return Component.translatable(PREFIX + "option." + option + ".@Tooltip");
    }

    static @Nullable Integer parseColor(String text) {
        String hex = text.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        if (!hex.matches("[0-9a-fA-F]{6}")) {
            return null;
        }
        return Integer.parseInt(hex, 16);
    }

    static @Nullable Float parseNumber(String text) {
        try {
            float number = Float.parseFloat(text.trim().replace(',', '.'));
            return Float.isFinite(number) && number >= 0 ? number : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String formatNumber(float value) {
        return value == Math.rint(value) ? Integer.toString((int) value) : Float.toString(value);
    }

    private static final class OptionList extends ContainerObjectSelectionList<Row> {
        private static final int ROW_HEIGHT = 24;

        OptionList(Minecraft minecraft, int width, int height, int y) {
            super(minecraft, width, height, y, ROW_HEIGHT);
        }

        void setRows(List<Row> rows) {
            double scroll = scrollAmount();
            clearEntries();
            rows.forEach(this::addEntry);
            setScrollAmount(scroll);
        }

        @Override
        public int getRowWidth() {
            return Math.min(340, width - 24);
        }
    }

    /** A label on the left and a widget on the right, or a section title. */
    private static final class Row extends ContainerObjectSelectionList.Entry<Row> {
        private final Font font;
        private final Component label;
        private final @Nullable Component tooltip;
        private final @Nullable AbstractWidget widget;
        private final @Nullable IntSupplier preview;

        Row(Font font, Component label, @Nullable Component tooltip, @Nullable AbstractWidget widget, @Nullable IntSupplier preview) {
            this.font = font;
            this.label = label;
            this.tooltip = tooltip;
            this.widget = widget;
            this.preview = preview;
            if (widget != null && tooltip != null) {
                widget.setTooltip(Tooltip.create(tooltip));
            }
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            int right = getContentRight();
            if (widget != null) {
                right -= widget.getWidth();
                widget.setPosition(right, getContentYMiddle() - widget.getHeight() / 2);
                widget.extractRenderState(graphics, mouseX, mouseY, a);
                right -= 4;
            }
            if (preview != null) {
                int size = 14;
                int top = getContentYMiddle() - size / 2;
                graphics.fill(right - size, top, right, top + size, 0xff000000);
                graphics.fill(right - size + 1, top + 1, right - 1, top + size - 1, preview.getAsInt());
            }
            int x = getContentX();
            int y = getContentYMiddle() - font.lineHeight / 2;
            graphics.text(font, label, x, y, 0xffffffff);
            if (tooltip != null && mouseX >= x && mouseX < x + font.width(label) && mouseY >= y && mouseY < y + font.lineHeight) {
                graphics.setTooltipForNextFrame(tooltip, mouseX, mouseY);
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return widget == null ? List.of() : List.of(widget);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return widget == null ? List.of() : List.of(widget);
        }
    }
}
