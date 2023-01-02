package at.flori4n.catchgame.data;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.wrapper.util.UpdateResult;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseQueries extends QueryFactory {

    String tableName;

    public DatabaseQueries(DataSource dataSource,String tableName) {
        super(dataSource);
        this.tableName =tableName;
    }

    public CompletableFuture<UpdateResult> createDatabaseEntry(UUID uuid, int highscore, int totalTime) {
        return builder()
                .query("INSERT INTO "+ tableName +" (uuid,highscore,totalTime) VALUES (?,?,?)")
                .parameter(stmt -> stmt.setString(uuid.toString()).setInt(highscore).setInt(totalTime))
                .insert()
                .send();
    }

    public CompletableFuture<Optional<String>> getStats(UUID uuid) {
        return builder(String.class)
                .query("SELECT * FROM "+ tableName +" WHERE uuid = ?")
                .parameter(stmt -> stmt.setString(uuid.toString()))
                .readRow(rs -> new String(rs.getString("highscore"))+";"+rs.getString("totalTime"))
                .first();

    }
    public CompletableFuture<Boolean> updateAll(UUID uuid, int highscre,int totalTime) {
        return builder()
                .query("UPDATE "+ tableName +" SET highscore = ?, totalTime = ? WHERE uuid = ?")
                .parameter(stmt -> stmt.setInt(highscre).setInt(totalTime).setString(uuid.toString()))
                .update()
                .send()
                .thenApply(UpdateResult::changed);
    }
    public CompletableFuture<Boolean> updateTotalTime(UUID uuid,int totalTime) {
        return builder()
                .query("UPDATE "+ tableName +" SET totalTime = ? WHERE uuid = ?")
                .parameter(stmt -> stmt.setInt(totalTime).setString(uuid.toString()))
                .update()
                .send()
                .thenApply(UpdateResult::changed);
    }
    public CompletableFuture<Boolean> updateHighscore(UUID uuid,int highscore) {
        return builder()
                .query("UPDATE "+ tableName +" SET highscore = ? WHERE uuid = ?")
                .parameter(stmt -> stmt.setInt(highscore).setString(uuid.toString()))
                .update()
                .send()
                .thenApply(UpdateResult::changed);
    }
}
