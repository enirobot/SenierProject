package com.team.news.Repository;

import com.team.news.Form.SankeyForm;
import com.team.news.Form.SankeyFormAndDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface GraphRepository extends MongoRepository<SankeyFormAndDate, String> {

    @Nullable
    List<SankeyFormAndDate> findSankeyFormAndDateByGroupAndDateGreaterThanEqual(String group, String date);

}
