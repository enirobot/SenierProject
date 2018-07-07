package com.team.news.Repository;

import com.team.news.Form.WCForm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface WCFormRepository extends MongoRepository<WCForm, String> {
    @Nullable
    List<WCForm> findAll();   // 모든 내용
}
