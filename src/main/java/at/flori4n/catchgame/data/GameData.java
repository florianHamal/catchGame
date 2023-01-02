package at.flori4n.catchgame.data;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameData {
    private static GameData instance;

    private Player catcher;
    private LocalDateTime timestampLastSwitch;
    private boolean gameRunning = false;
    private List<FastBoard> scoreBoards = new ArrayList<>();

    private HashMap<UUID,Integer> highscore = new HashMap<>();
    private HashMap<UUID,Integer> totalCacherTime = new HashMap<>();


    private GameData(){
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public static GameData getInstance(){
        if (instance==null){
            instance = new GameData();
        }
        return instance;
    }

    public Player getCatcher() {
        return catcher;
    }

    public void setCatcher(Player catcher) {
        this.catcher = catcher;
    }

    public LocalDateTime getTimestampLastSwitch() {
        return timestampLastSwitch;
    }

    public void setTimestampLastSwitch(LocalDateTime timestampLastSwitch) {
        this.timestampLastSwitch = timestampLastSwitch;
    }
    public List<FastBoard> getScoreBoards() {
        synchronized(scoreBoards) {
            return scoreBoards;
        }
    }

    public HashMap<UUID, Integer> getHighscore() {
        return highscore;
    }

    public HashMap<UUID, Integer> getTotalCacherTime() {
        return totalCacherTime;
    }

    public void removePlayerScoreboard(Player player){
        synchronized(scoreBoards) {
            FastBoard boardToRemove = null;
            for (FastBoard board : scoreBoards) {
                if (board.getPlayer() == player) {
                    boardToRemove=board;
                }
            }
            scoreBoards.remove(boardToRemove);
        }
    }

}
