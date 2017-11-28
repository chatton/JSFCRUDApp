package com.geog.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.geog.model.HeadOfState;
import com.geog.util.Pages;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDao {

	private MongoDatabase db;
	private MongoClient mongoClient;

	public MongoDao() {
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase("headsOfStateDB");
	}

	public List<HeadOfState> getHeadsOfState() {
		final MongoCollection<Document> headsOfStateCollection = db.getCollection("headsOfState");
		final FindIterable<Document> hos = headsOfStateCollection.find();
		final Gson gson = new Gson();
		final List<HeadOfState> headsOfState = new ArrayList<>();
		for (Document doc : hos) {
			HeadOfState h = gson.fromJson(doc.toJson(), HeadOfState.class);
			headsOfState.add(h);
		}
		return headsOfState;
	}

	public String delete(final HeadOfState hos) {
		final MongoCollection<Document> headsOfStateCollection = db.getCollection("headsOfState");
		headsOfStateCollection.deleteOne(new Document("_id", hos.get_id()));
		return Pages.HEADS_OF_STATE;
	}

	public String add(final HeadOfState hos) {
		final MongoCollection<Document> headsOfStateCollection = db.getCollection("headsOfState");
		final Document doc = new Document();
		doc.append("_id", hos.get_id());
		doc.append("headOfState", hos.getHeadOfState());
		headsOfStateCollection.insertOne(doc);
		return Pages.HEADS_OF_STATE;
	}

}
