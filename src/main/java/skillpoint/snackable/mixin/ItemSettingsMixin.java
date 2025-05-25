package skillpoint.snackable.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.Settings.class)
public class ItemSettingsMixin {

    @Inject(at = @At("HEAD"), method = "food(Lnet/minecraft/component/type/FoodComponent;Lnet/minecraft/component/type/ConsumableComponent;)Lnet/minecraft/item/Item$Settings;")
    private void food(FoodComponent foodComponent, ConsumableComponent consumableComponent, CallbackInfoReturnable<Item.Settings> cir, @Local(argsOnly = true) LocalRef<ConsumableComponent> localRef) {
        // use saturation to nutrition ratio to override default consume time
        var saturationModifier = foodComponent.saturation() / foodComponent.nutrition();
        var timeOverride = saturationModifier * ConsumableComponent.DEFAULT_CONSUME_SECONDS;

        // copy consumable component replacing consume time
        localRef.set(new ConsumableComponent(
                foodComponent.canAlwaysEat() ? consumableComponent.consumeSeconds() : timeOverride,
                consumableComponent.useAction(),
                consumableComponent.sound(),
                consumableComponent.hasConsumeParticles(),
                consumableComponent.onConsumeEffects()));
    }
}
