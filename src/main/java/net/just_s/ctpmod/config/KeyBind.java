package net.just_s.ctpmod.config;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.minecraft.network.chat.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeyBind {
    private String translationKey;
    private short modifier;

    public static KeyBind of(ModifierKeyCode modifierKeyCode) {
        return new KeyBind(modifierKeyCode.getKeyCode().getName(), modifierKeyCode.getModifier().getValue());
    }

    public ModifierKeyCode toModifierKeyCode() {
        return ModifierKeyCode.of(InputConstants.getKey(translationKey), Modifier.of(modifier));
    }

    public String toString() {
        return this.getLocalizedName().getString();
    }

    public Component getLocalizedName() {
        Component base = InputConstants.getKey(translationKey).getDisplayName();
        Modifier modifier = Modifier.of(this.modifier);

        if (modifier.hasShift()) {
            base = Component.translatable("modifier.cloth-config.shift", base);
        }

        if (modifier.hasControl()) {
            base = Component.translatable("modifier.cloth-config.ctrl", base);
        }

        if (modifier.hasAlt()) {
            base = Component.translatable("modifier.cloth-config.alt", base);
        }

        return base;
    }
}
