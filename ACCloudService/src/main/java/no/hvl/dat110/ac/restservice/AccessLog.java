package no.hvl.dat110.ac.restservice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

public class AccessLog {

	private Gson gson;

	// atomic integer used to obtain identifiers for each access entry
	private AtomicInteger cid;
	protected ConcurrentHashMap<Integer, AccessEntry> log;
	
	public AccessLog () {
		this.log = new ConcurrentHashMap<Integer,AccessEntry>();
		cid = new AtomicInteger(0);
		gson = new Gson();
	}

	public int add(String message) {
		
		int id = cid.getAndIncrement();
		AccessEntry newEntry = new AccessEntry(id, message);
		log.put(id, newEntry);

		return id;
	}

	public AccessEntry get(int id) {
		return log.get(id);
	}
	
	public void clear() {
		log.clear();
	}
	
	public String toJson () {
    	return gson.toJson(log.values());
    }
}
