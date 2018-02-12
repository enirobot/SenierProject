package com.mongo.board;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.*;

import com.mongodb.MongoClient;


//MVC �� Model�� ���
//DB�� ���� �����ϴ� �κ��� ��� ���⼭ ����
public class MongoDAO {
	
	 MongoOperations mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "project"));
	 //project��� �̸��� ���� �����ͺ��̽��� ����.
	 
	 ////*����
	 //sql     mongo
	 //table = collection
	 //row = document

	 
	//�Ʒ� ��� �޼������ news��� �̸��� ���� collection���� �����
	//collection �̸��� ���� ���������� �ʾƼ� �ڵ����� class�� ���� �̸��� ���� collection�� ������.
	public MongoDAO() {}
	
	//�����ͺ��̽��� ����
	public void insert(News news) {
		mongoOps.insert(news);
		System.out.println("success : " + mongoOps);
	}
	//collection���� ��� raw�� List�� ����
	public List<News> findAll() {
		List<News> news = mongoOps.findAll(News.class);		
		return news;
	}
	
	//where�� ������ value�� �ش��ϴ� raw�� list�� ����
	public List<News> find(String where, String value){
		
		//������ ���
		Query query = new Query();
		//where ���ǿ�  value ���ڿ��� ����ִ� document�� ���
		query.addCriteria(Criteria.where(where).regex(".*"+value+".*"));
		
		List<News> list = mongoOps.find(query, News.class);
		return list;
	}
	
	//���ǿ� �´� document�� �˻��ϰ� �����ϴ� ���
	public void findAndRemove(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoOps.findAndRemove(query, News.class);
	}	
	
    //�̱���
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
