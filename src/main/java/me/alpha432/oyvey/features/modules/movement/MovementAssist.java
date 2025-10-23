package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class MovementAssist extends Module {
    private final Setting<Float> jumpHeight = num("JumpHeight", 1.0f, 0.5f, 2.0f);
    private final Setting<Boolean> autoJump = b("AutoJump", true);
    private final Setting<Boolean> autoSprint = b("AutoSprint", true);
    private final Setting<Boolean> sneakDisable = b("DisableWhenSneaking", true);

    private final Minecraft mc = Minecraft.getInstance();

    public MovementAssist() {
        super("MovementAssist", "Assists movement: auto jump, sprint, etc.", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) return;

        PlayerEntity player = mc.player;

        if (sneakDisable.getValue() && player.isSneaking()) {
            return;
        }

        // Auto sprint
        if (autoSprint.getValue()) {
            if (!player.isSprinting() && player.forwardSpeed > 0 && !player.isCollidedHorizontally && !player.isSneaking()) {
                player.setSprinting(true);
            }
        }

        // Auto jump
        if (autoJump.getValue()) {
            if (player.forwardSpeed > 0 && player.isOnGround() && !player.isSneaking() && !player.isInWater() && !player.isInLava()) {
                player.jump();
                // Note: Minecraft jump height is fixed; modifying jumpHeight requires motionY manipulation elsewhere
            }
        }
    }

    // Helper methods to register settings (assuming your Module class has these)
    private Setting<Float> num(String name, float defaultValue, float min, float max) {
        return register(new Setting<>(name, defaultValue, min, max));
    }

    private Setting<Boolean> b(String name, boolean defaultValue) {
        return register(new Setting<>(name, defaultValue));
    }

    // nullCheck method to check if player or world is null
    private boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }
}
