package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.player.LocalPlayer;

public class MovementAssist extends Module {
    private final Setting<Float> jumpHeight = register(new Setting<>("JumpHeight", 1.0f, 0.5f, 2.0f));
    private final Setting<Boolean> autoJump = register(new Setting<>("AutoJump", true));
    private final Setting<Boolean> autoSprint = register(new Setting<>("AutoSprint", true));
    private final Setting<Boolean> sneakDisable = register(new Setting<>("DisableWhenSneaking", true));

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public MovementAssist() {
        super("MovementAssist", "Assists movement: auto jump, sprint, etc.", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        LocalPlayer player = mc.player;

        if (sneakDisable.getValue() && player.isSneaking()) {
            return;
        }

        // Auto sprint
        if (autoSprint.getValue()) {
            if (!player.isSprinting() && player.input.forward > 0 && !player.horizontalCollision && !player.isSneaking()) {
                player.setSprinting(true);
            }
        }

        // Auto jump
        if (autoJump.getValue()) {
            if (player.input.forward > 0 && player.isOnGround() && !player.isSneaking() && !player.isInWater() && !player.isInLava()) {
                player.jump();

                // To modify jump height, you can adjust velocity here if your client supports it:
                // player.setVelocity(player.getVelocity().x, jumpHeight.getValue(), player.getVelocity().z);
            }
        }
    }

    private boolean nullCheck() {
        return mc.player == null || mc.level == null;
    }
}
