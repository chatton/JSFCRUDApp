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

/*
 * Mongo Database Access object, used to communicate with the mongo database.
 */
public class MongoDao {

	private final MongoDatabase db;
	private final MongoClient mongoClient;

	public MongoDao() {
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase("headsOfStateDB");
	}

	/*
	 * returns a list of HeadOfState objects. The values of these objects are read
	 * in from the database.
	 */
	public List<HeadOfState> getHeadsOfState() {
		final MongoCollection<Document> headsOfStateCollection = db.getCollection("headsOfState");
		final FindIterable<Document> hos = headsOfStateCollection.find(); // get every document in the collection
		final Gson gson = new Gson();
		final List<HeadOfState> headsOfState = new ArrayList<>();
		for (Document doc : hos) { // looking at every document
			HeadOfState h = gson.fromJson(doc.toJson(), HeadOfState.class); // convert it into a HeadOfState object
			headsOfState.add(h);
		}
		return headsOfState;
	}

	/*
	 * Deletes a HeadOfState from the database by using its unique "_id" attribute.
	 */
	public String delete(final HeadOfState hos) {
		final MongoCollection<Document> headsOfStateCollection = db.getCollection("headsOfState");
		headsOfStateCollection.deleteOne(new Document("_id", hos.get_id()));
		return Pages.HEADS_OF_STATE;
	}

	/*
	 * Adds a head of state to the database.
	 */
	public String add(final HeadOfState hos) {
		final MongoCollection<Document> headsOfStateCollection = db.getCollection("headsOfState");
		final Document doc = new Document(); // the document that will be added.
		doc.append("_id", hos.get_id());
		doc.append("headOfState", hos.getHeadOfState());
		headsOfStateCollection.insertOne(doc);
		return Pages.HEADS_OF_STATE;
	}

}
