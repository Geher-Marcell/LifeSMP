package Listeners;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OnGetEffect implements Listener {
    @EventHandler
    public void onGetEffect(EntityPotionEffectEvent event) {
        if(event.getCause() != EntityPotionEffectEvent.Cause.POTION_SPLASH) return;

        if(event.getAction() == EntityPotionEffectEvent.Action.REMOVED) return;

        if(event.getEntity().getType() == EntityType.PLAYER) {
            event.setCancelled(true);

            PotionEffect rawEffect = event.getNewEffect();
            Player player = (Player) event.getEntity();

            PotionEffect newEffect = new PotionEffect(rawEffect.getType(), rawEffect.getDuration()/ Seaseon2.getInstance().getConfig().getInt("potion_duration_denominator"), rawEffect.getAmplifier());

            player.addPotionEffect(newEffect, true);
        }
    }
}
