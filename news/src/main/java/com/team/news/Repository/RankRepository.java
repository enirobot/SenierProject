package com.team.news.Repository;

import com.team.news.Form.RankForm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface RankRepository extends MongoRepository<RankForm, String> {

    @Nullable
    RankForm findRankFormByWord(String word);

    boolean findRankFormByWordEquals(String word);

    @Nullable
    List<RankForm> findRankFormsByNodeListIn();
}
