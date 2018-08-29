/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.inrating.poststat.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import top.inrating.poststat.db.entity.PostStatisticsEntity;
import top.inrating.poststat.model.PostStatistics;

@Dao
public interface PostStatisticsDao {
    @Query("SELECT * FROM post_statistics WHERE post_id = :postId")
    LiveData<List<PostStatisticsEntity>> loadAllPostStatisticss(int postId);

    @Query("SELECT * FROM post_statistics WHERE post_id = :postId")
    List<PostStatisticsEntity> loadAllPostStatisticssSync(int postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PostStatisticsEntity> postSatisticss);

    @Query("SELECT * FROM post_statistics WHERE post_id = :postId AND type = :statisticsType")
    LiveData<PostStatisticsEntity> loadPostStatistics(int postId, int statisticsType);

    @Query("SELECT * FROM post_statistics WHERE post_id = :postId AND type = :statisticsType")
    PostStatisticsEntity loadPostStatisticsSync(int postId, int statisticsType);

    @Delete
    void delete(PostStatisticsEntity postSatistics);

    @Delete
    void deleteAll(List<PostStatisticsEntity> postSatisticss);

    @Query("UPDATE post_statistics SET loading_state = :newState  WHERE post_id = :postId")
    int updateLoadingStateSync(int newState, int postId);
}
