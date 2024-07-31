package net.just_s.ctpmod.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.minecraft.client.util.InputUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeyBind {
    private String translationKey;
    private short modifier;

    public static KeyBind of(ModifierKeyCode modifierKeyCode) {
        return new KeyBind(modifierKeyCode.getKeyCode().getTranslationKey(), modifierKeyCode.getModifier().getValue());
    }

    public ModifierKeyCode toModifierKeyCode() {
        return ModifierKeyCode.of(InputUtil.fromTranslationKey(translationKey), Modifier.of(modifier));
    }
}
