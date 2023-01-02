package at.flori4n.catchgame.listeners;

import at.flori4n.catchgame.data.DatabaseHandler;
import at.flori4n.catchgame.data.GameData;
import at.flori4n.catchgame.gameManager.GameManager;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    GameData gameData = GameData.getInstance();
    GameManager gameManager;
    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
    public PlayerQuitListener(GameManager gameManager){
        this.gameManager = gameManager;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        gameData.removePlayerScoreboard(player);

        if (!gameData.isGameRunning()){
            return;
        }

        if (Bukkit.getOnlinePlayers().size()<3){ //cuz player who is quitting is still in the list
            gameManager.stopGame();
        }else {
            if (player==gameData.getCatcher()){
                gameManager.selectRandomCatcher();
            }

        }
        databaseHandler.unloadPlayerStats(player);


    }

}
