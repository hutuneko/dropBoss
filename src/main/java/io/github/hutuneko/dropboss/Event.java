package io.github.hutuneko.dropboss;

import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Event {
    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        Player attacker = event.getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            PlayerAdvancements advancements = serverPlayer.getAdvancements();
            MinecraftServer server = serverPlayer.serverLevel().getServer();

            var manager = server.getAdvancements();
            Advancement advancement = manager.getAdvancement(TARGET_ADVANCEMENT);
            if (advancement != null) {
                if (advancements.getOrStartProgress(advancement).isDone()){

                    Entity target = event.getTarget();

                    target.kill();
                    attacker.getInventory().dropAll();
                }
            }
        }

    }
    @SubscribeEvent
    public void kill(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if (level.isClientSide) return;
        if (level.random.nextInt(100) == 0) {
            LivingEntity entity = event.getEntity();
            var pos = entity.position();
            ItemEntity itemEntity = new ItemEntity(level, pos.x, pos.y, pos.z, new ItemStack(DropBoss.BOSSKILLITEM.get()));
            level.addFreshEntity(itemEntity);
        }
    }
    private static final ResourceLocation TARGET_ADVANCEMENT = new ResourceLocation(DropBoss.MODID,"bosskillitem");

//
//    private static final Map<UUID, ServerBossEvent> activeBars = new HashMap<>();
//
//    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            checkAndShowBossBar(player);
//        }
//    }
//
//    public static void onAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            if (event.getAdvancement().getId().equals(TARGET_ADVANCEMENT)) {
//                removeBossBar(player);
//            }
//        }
//    }
//
//    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            removeBossBar(player);
//        }
//    }
//
//    private static void checkAndShowBossBar(ServerPlayer player) {
//
//
//        PlayerAdvancements advancements = player.getAdvancements();
//        MinecraftServer server = player.serverLevel().getServer();
//
//        var manager = server.getAdvancements();
//        Advancement advancement = manager.getAdvancement(TARGET_ADVANCEMENT);
//        if (advancement != null) {
//            if (advancements.getOrStartProgress(advancement).isDone()){
//                if (!activeBars.containsKey(player.getUUID())) {
//
//                    ServerBossEvent bossBar = new ServerBossEvent(
//                            Component.literal("DropBoss"),
//                            BossEvent.BossBarColor.BLUE,
//                            BossEvent.BossBarOverlay.PROGRESS
//                    );
//
//                    bossBar.addPlayer(player);
//                    activeBars.put(player.getUUID(), bossBar);
//                }
//            }
//        }
//    }
//
//    private static void removeBossBar(ServerPlayer player) {
//        ServerBossEvent bossBar = activeBars.remove(player.getUUID());
//        if (bossBar != null) {
//            bossBar.removePlayer(player);
//        }
//    }
}
