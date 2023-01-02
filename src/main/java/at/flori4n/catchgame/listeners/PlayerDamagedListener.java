package at.flori4n.catchgame.listeners;

import at.flori4n.catchgame.data.GameData;
import at.flori4n.catchgame.gameManager.GameManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class PlayerDamagedListener implements Listener {
    GameManager gameManager;
    GameData gameData = GameData.getInstance();
    public PlayerDamagedListener(GameManager gameManager){
        this.gameManager=gameManager;
    }
    @EventHandler
    public void onEntityDamaged(EntityDamageByEntityEvent event){
        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();
        event.setCancelled(true);
        if (!(damaged instanceof Player)||!(damager instanceof Player)){
            return;
        }
        if (damaged != gameData.getCatcher()){
            return;
        }
        gameManager.makePlayerToCacher((Player) damager);

    }
}
