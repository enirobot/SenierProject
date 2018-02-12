package com.mongo.board;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.*;

import com.mongodb.MongoClient;


//MVC 증 Model을 담당
//DB와 직접 접속하는 부분은 모두 여기서 수행
public class MongoDAO {
	
	 MongoOperations mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "project"));
	 //project라는 이름을 가진 데이터베이스에 접속.
	 
	 ////*참고
	 //sql     mongo
	 //table = collection
	 //row = document

	 
	//아래 모든 메서드들은 news라는 이름을 가진 collection에서 수행됨
	//collection 이름을 따로 지정해주지 않아서 자동으로 class와 같은 이름을 가진 collection이 생성됨.
	public MongoDAO() {}
	
	//데이터베이스에 삽입
	public void insert(News news) {
		mongoOps.insert(news);
		System.out.println("success : " + mongoOps);
	}
	//collection내의 모든 raw를 List에 저장
	public List<News> findAll() {
		List<News> news = mongoOps.findAll(News.class);		
		return news;
	}
	
	//where의 조건이 value에 해당하는 raw를 list에 저장
	public List<News> find(String where, String value){
		
		//쿼리문 사용
		Query query = new Query();
		//where 조건에  value 문자열이 들어있는 document를 출력
		query.addCriteria(Criteria.where(where).regex(".*"+value+".*"));
		
		List<News> list = mongoOps.find(query, News.class);
		return list;
	}
	
	//조건에 맞는 document를 검색하고 삭제하는 기능
	public void findAndRemove(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoOps.findAndRemove(query, News.class);
	}	
	
    //미구현
    public void update(News news) {
    	mongoOps.save(news);
    }
 
    public void delete(News news) {
    	mongoOps.remove(news);
    }
 
    public void deleteAll() {
    	mongoOps.remove(new Query());
    }
}
