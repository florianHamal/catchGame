package at.flori4n.catchgame.listeners;

import at.flori4n.catchgame.data.DatabaseHandler;
import at.flori4n.catchgame.data.GameData;
import at.flori4n.catchgame.gameManager.GameManager;
import at.flori4n.catchgame.scoreboard.ScoreboardHandler;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    GameManager gameManager;
    GameData gameData = GameData.getInstance();
    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
    public PlayerJoinListener(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        databaseHandler.loadPlayerStats(player);
        FastBoard scoreboard = new FastBoard(player);
        scoreboard.updateTitle(ChatColor.GOLD+"Catch Game");
        gameData.getScoreBoards().add(scoreboard);
        ScoreboardHandler.updateAllScoreboards();
        if (Bukkit.getOnlinePlayers().size()<2){//wenn weniger als 2 spieler
           return;
        }
        if (GameData.getInstance().isGameRunning()){//wenn game bereits läuft
            return;
        }

        gameManager.startGame();
    }
}
