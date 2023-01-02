package at.flori4n.catchgame.listeners;

import at.flori4n.catchgame.data.GameData;
import at.flori4n.catchgame.gameManager.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private GameData gameData = GameData.getInstance();
    private GameManager gameManager;
    public PlayerDeathListener(GameManager gameManager){
        this.gameManager = gameManager;
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (gameData.getCatcher()!=player){
            return;
        }
        gameManager.selectRandomCatcher();
    }
}
