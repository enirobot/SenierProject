package com.team.news.Repository;

import com.team.news.Form.SankeyForm;
import com.team.news.Form.SankeyFormAndDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface SankeyRepository extends MongoRepository<SankeyFormAndDate, String> {

    @Nullable
    SankeyFormAndDate findTopByGroupOrderByDateDesc(String group);
}
