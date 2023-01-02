package at.flori4n.catchgame.gameManager;

import at.flori4n.catchgame.data.DatabaseHandler;
import at.flori4n.catchgame.data.GameData;
import at.flori4n.catchgame.main.Main;
import at.flori4n.catchgame.scoreboard.ScoreboardHandler;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class GameManager {

    private GameData gameData= GameData.getInstance();
    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
    int task;
    public void startGame(){
        GameData.getInstance().setGameRunning(true);
        selectRandomCatcher();
        Bukkit.broadcastMessage("catch game started");
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(),new gameTask(),0,20);
    }
    public void stopGame(){
        Bukkit.getScheduler().cancelTask(task);
        GameData.getInstance().setGameRunning(false);
        undoCacher();
        Bukkit.broadcastMessage("catch game ended");
        ScoreboardHandler.updateAllScoreboards();
    }

    public void selectRandomCatcher(){
        Collection<? extends Player> players= Bukkit.getOnlinePlayers();
        int playerIndex = new Random().nextInt(0,players.size());
        makePlayerToCacher(players.stream().collect(Collectors.toCollection(ArrayList::new)).get(playerIndex));
    }

    public void makePlayerToCacher(Player player){
        if (gameData.getCatcher()!=null){
            undoCacher();
        }
        player.getInventory().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
        gameData.setCatcher(player);
        gameData.setTimestampLastSwitch(LocalDateTime.now());
        Bukkit.broadcastMessage(player.getName()+" is the new catcher");
        Bukkit.getOnlinePlayers().forEach(i->i.playSound(player.getLocation(),Sound.BLOCK_SLIME_BLOCK_BREAK,1,1));
    }
    public void undoCacher(){
        databaseHandler.updateStats(gameData.getCatcher(),gameData.getTimestampLastSwitch());
        gameData.getCatcher().getInventory().setHelmet(null);
        gameData.setCatcher(null);
        gameData.setTimestampLastSwitch(null);
    }


    private class gameTask implements Runnable{
        @Override
        public void run() {
            ScoreboardHandler.updateAllScoreboards();
            Bukkit.getOnlinePlayers().forEach(i->i.spawnParticle(Particle.LAVA,gameData.getCatcher().getLocation(),5));
            gameData.getCatcher().getLocation().getWorld().spawnEntity(gameData.getCatcher().getLocation(), EntityType.FIREWORK);
        }
    }








}
