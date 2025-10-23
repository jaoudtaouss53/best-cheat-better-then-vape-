package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.MovementInput;

public class MovementAssist extends Module {
    private final Setting<Float> jumpHeight = register(new Setting<>("JumpHeight", 1.0f, 0.5f, 2.0f));
    private final Setting<Boolean> autoJump = register(new Setting<>("AutoJump", true));
    private final Setting<Boolean> autoSprint = register(new Setting<>("AutoSprint", true));
    private final Setting<Boolean> sneakDisable = register(new Setting<>("DisableWhenSneaking", true));

    private final Minecraft mc = Minecraft.getInstance();

    public MovementAssist() {
        super("MovementAssist", "Assists movement: auto jump, sprint, etc.", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        PlayerEntity player = mc.player;

        // Disable if sneaking and setting enabled
        if (sneakDisable.getValue() && player.isSneaking()) {
            return;
        }

        // Auto sprint
        if (autoSprint.getValue()) {
            if (!player.isSprinting() && player.moveForward > 0 && !player.isCollidedHorizontally && !player.isSneaking()) {
                player.setSprinting(true);
            }
        }

        // Auto jump for parkour
        if (autoJump.getValue()) {
            MovementInput input = mc.player.input;
            if (input.forward > 0 && player.onGround && !player.isSneaking() && !player.isInWater() && !player.isInLava()) {
                // Jump with adjustable height (Minecraft jump velocity is fixed, so this is a simulation)
                player.jump();
                // Optionally, you can modify jump velocity here if your client supports it
            }
        }
    }
}
