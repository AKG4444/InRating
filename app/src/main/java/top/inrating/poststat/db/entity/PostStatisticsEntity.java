package top.inrating.poststat.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import top.inrating.poststat.R;
import top.inrating.poststat.model.PostStatistics;

@Entity(tableName = "post_statistics")
public class PostStatisticsEntity implements PostStatistics {
    @PrimaryKey(autoGenerate = true)
    private int id;      // id

    @ColumnInfo(name = "post_id")
    private int postId;

    private int type;    // statistics type
    private int usersAmount;
    private String usersNicknames;
    private String usersAvatars;

    @ColumnInfo(name = "loading_state")
    private int loadingState;
                         // 0 - ещё нет данных
                         // 1 - updating
                         // 2 - updated with success
                         // 3 - updated with error
                         // 4 - update interrupted
    //private String errMsg;


    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getUsersAmount() {
        return usersAmount;
    }

    public void setUsersAmount(int usersAmount) {
        this.usersAmount = usersAmount;
    }

    @Override
    public String getUsersNicknames() {
        return usersNicknames;
    }

    public void setUsersNicknames(String usersNicknames) {
        this.usersNicknames = usersNicknames;
    }

    @Override
    public String getUsersAvatars() {
        return usersAvatars;
    }

    public void setUsersAvatars(String usersAvatars) {
        this.usersAvatars = usersAvatars;
    }

    @Override
    public int getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(int loadingState) {
        this.loadingState = loadingState;
    }


    public PostStatisticsEntity() {
    }

    public PostStatisticsEntity(int postId,          // post id
                                int type,            // satistics type
                                int usersAmount,
                                String usersNicknames,
                                String usersAvatars,
                                int loadingState)
    {
        this.postId = postId;
        this.type = type;
        this.usersAmount = usersAmount;
        this.usersNicknames = usersNicknames;
        this.usersAvatars = usersAvatars;
        this.loadingState = loadingState;
    }
}
