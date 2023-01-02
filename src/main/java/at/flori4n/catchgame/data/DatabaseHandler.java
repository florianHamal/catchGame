package at.flori4n.catchgame.data;

import at.flori4n.catchgame.main.Main;
import de.chojo.sadu.databases.MariaDb;
import de.chojo.sadu.datasource.DataSourceCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class DatabaseHandler {

    private DatabaseQueries queries;

    private static DatabaseHandler instance;

    private GameData gameData = GameData.getInstance();;

    public void updateStats(Player player, LocalDateTime timestampLastSwitch){
        UUID uuid = player.getUniqueId();
        int oldHighscore = gameData.getHighscore().get(uuid);
        int oldTotalTime = gameData.getTotalCacherTime().get(uuid);
        int  currentTime= (int) ChronoUnit.SECONDS.between(timestampLastSwitch,LocalDateTime.now());
        int newTotalTime = currentTime+oldTotalTime;

        gameData.getTotalCacherTime().put(uuid,newTotalTime); //update totalTime in gameData
        if (currentTime>oldHighscore){
            gameData.getHighscore().put(uuid,currentTime);//update highscore in gameData
            queries.updateAll(uuid,currentTime,newTotalTime);//update all in database
        }else{
            queries.updateTotalTime(uuid,newTotalTime);//update totalTime in database
        }
    }

    public void loadPlayerStats(Player player){
        UUID uuid = player.getUniqueId();
        Optional<String> result = null;

        try {
            result = queries.getStats(uuid).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        if (result.isEmpty()) {     //wenn nicht in datenbank gefunden -> neu anlegen mit werten 0
            queries.createDatabaseEntry(uuid, 0, 0);
            gameData.getHighscore().put(uuid,0);
            gameData.getTotalCacherTime().put(uuid,0);
        }else {                                 // wenn in datenbank gefunden
            String contents[] = result.get().split(";");
            gameData.getHighscore().put(uuid,Integer.parseInt(contents[0]));
            gameData.getTotalCacherTime().put(uuid,Integer.parseInt(contents[1]));
        }


    }

    public void unloadPlayerStats(Player player){
        gameData.getHighscore().remove(player.getUniqueId());
        gameData.getTotalCacherTime().remove(player.getUniqueId());
    }

    private DatabaseHandler(){}

    public void setupConnection(String ip,int port,String user,String password,String database,String tableName){
        queries = new DatabaseQueries(
                DataSourceCreator.create(MariaDb.get())
                        .configure(config ->
                                config.host(ip)
                                        .port(port)
                                        .user(user)
                                        .password(password)
                                        .database(database))
                        .create()
                        .withMaximumPoolSize(3)
                        .withMinimumIdle(1)
                        .build(),
                tableName);
    }

    public static DatabaseHandler getInstance(){
        if (instance==null){
            instance = new DatabaseHandler();
        }
        return instance;
    }


}
