package poly.persistance.mongo.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;

import poly.persistance.mongo.IMongoTestMapper;

@Component("MongoTestMapper")
public class MongoTestMapper implements IMongoTestMapper {

	@Autowired
	private MongoTemplate mongodb;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean createCollection(String colNm) throws Exception {
		log.info(this.getClass().getName() + "createCollection Start!");

		boolean res = false;

		// 기존 콜렉션 체크, 있는경우 삭제
		if (mongodb.collectionExists(colNm)) {
			mongodb.dropCollection(colNm);
		}

		// 컬렉션 및 인덱스 생성, MongoDB에서 데이터 가져오는 방식에 맞게 인덱스는 반드시 생성
		// 데이터 양이 많지 않으면 문제가 되지 않으나, 최소 10만건 이상 데이터 저장시 속도차이 10배 이상
		mongodb.createCollection(colNm).createIndex(new BasicDBObject("user_id", 1), "testIdx");

		res = true;

		log.info(this.getClass().getName() + "createCollection End!");

		return res;
	}

}
