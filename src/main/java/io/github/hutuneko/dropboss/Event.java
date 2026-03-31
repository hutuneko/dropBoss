package io.github.hutuneko.removeboss;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Event {
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Entity target = event.getTarget();
        Player attacker = event.getEntity();
        target.kill();
        attacker.getInventory().dropAll();
    }
    @SubscribeEvent
    public static void kill(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if (level.isClientSide) return;
        if (level.random.nextInt(100) == 0) {
            LivingEntity entity = event.getEntity();
            var pos = entity.position();
            ItemEntity itemEntity = new ItemEntity(level, pos.x, pos.y, pos.z, new ItemStack(RemoveBoss.BOSSKILLITEM.get()));
            level.addFreshEntity(itemEntity);
        }
    }
}
