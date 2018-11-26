package com.team.news.Repository;

import com.team.news.Form.BubbleFormAndDate;
import com.team.news.Form.SankeyFormAndDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

public interface BubbleRepository2 extends MongoRepository<BubbleFormAndDate, String> {

    @Nullable
    BubbleFormAndDate findTopByGroupOrderByDateDesc(String group);
}
