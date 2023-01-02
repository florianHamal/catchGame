package at.flori4n.catchgame.scoreboard;

import at.flori4n.catchgame.data.GameData;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ScoreboardHandler {

    private static GameData gameData = GameData.getInstance();

    public static void updateScoreboard(FastBoard scoreboard){
        if (gameData.isGameRunning()){
            setRunningScoreboard(scoreboard);
        }else {
            setWaitingScoreboard(scoreboard);
        }
    }

    private static void setRunningScoreboard(FastBoard scoreboard){
        Player player = scoreboard.getPlayer();
        int totalTime = gameData.getTotalCacherTime().get(player.getUniqueId());
        int highscore = gameData.getHighscore().get(player.getUniqueId());
        int currentCacherTime = (int) ChronoUnit.SECONDS.between(gameData.getTimestampLastSwitch(),LocalDateTime.now());

        if(gameData.getCatcher()==player){
            totalTime+=currentCacherTime;

            if (currentCacherTime>highscore){
                highscore=currentCacherTime;
            }

        }

        if (gameData.isGameRunning()){
            scoreboard.updateLines(
                    "",
                    ChatColor.AQUA+"current Catcher: "+ ChatColor.RED +gameData.getCatcher().getName(),
                    ChatColor.AQUA+"time: "+ChatColor.RED+currentCacherTime+"s",
                    ChatColor.AQUA+"total time as Catcher: "+ChatColor.RED+totalTime+"s" ,
                    ChatColor.AQUA+ "highscore: " +ChatColor.RED+highscore+"s"
            );
        }
    }

    private static void setWaitingScoreboard(FastBoard scoreboard){
        scoreboard.updateLines("waiting for other Players",
                                gameData.getScoreBoards().size()+"/"+2); // using scoreboards cuz they get updatet in the listener and players online are updated after the listeners execution
    }

    public static void updateAllScoreboards(){
        gameData.getScoreBoards().forEach(i -> updateScoreboard(i));
    }
}
