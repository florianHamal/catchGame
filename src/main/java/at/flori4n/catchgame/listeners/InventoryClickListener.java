package at.flori4n.catchgame.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void inInventoryClick(InventoryClickEvent event){
        event.setCancelled(true);
    }

}
