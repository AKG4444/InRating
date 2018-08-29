package top.inrating.poststat.db;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import top.inrating.poststat.db.entity.PostStatisticsEntity;
import top.inrating.poststat.model.PostStatistics;

/**
 * Generates data to pre-populate the database
 */
public class DataGenerator {

    private static final String[] USER_NAMES = new String[]{
            "leralatya", "Georges", "Syaka", "kyiv14", "Bshyrova", "ShpadoDo"};
    private static final String[] USER_AVATARS = new String[]{
            "https://httpbin.org/image/png", "https://httpbin.org/image/png",
            "https://httpbin.org/image/png", "https://httpbin.org/image/png",
            "https://httpbin.org/image/png", "https://httpbin.org/image/png"};

    public static List<PostStatisticsEntity> generatePostStatisticss(int postId) {
        final int STATISTICS_AMOUNT = 6;
        List<PostStatisticsEntity> postStatisticss = new ArrayList<>(STATISTICS_AMOUNT);
        Random rnd = new Random();
        int usersAmount;
        int rndIndex;
        int zeroStatIndex = rnd.nextInt(STATISTICS_AMOUNT);
        for (int i = 0; i < STATISTICS_AMOUNT; i++) {
            PostStatisticsEntity postStatistics = new PostStatisticsEntity();
            postStatistics.setPostId(postId);
            postStatistics.setType(i);
            usersAmount = i==zeroStatIndex? 0 : rnd.nextInt(10);
            postStatistics.setUsersAmount(usersAmount);
            String[] nicknames = new String[usersAmount];
            String[] avatars = new String[usersAmount];
            for (int j = 0; j < usersAmount; j++) {
                rndIndex = rnd.nextInt(6);
                nicknames[j] = USER_NAMES[rndIndex];
                avatars[j] = USER_AVATARS[rndIndex];
            }

            Gson gson = new Gson();
            String inputString = gson.toJson(new ArrayList<>(Arrays.asList(nicknames)));
            postStatistics.setUsersNicknames(inputString);
            inputString = gson.toJson(new ArrayList<>(Arrays.asList(avatars)));
            postStatistics.setUsersAvatars(inputString);
            postStatistics.setLoadingState(2);
            postStatisticss.add(postStatistics);
        }
        return postStatisticss;
    }

}
