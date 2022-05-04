package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	private static Gson gson = new Gson();
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			return gson.toJson("IoT Access Control Device");
		});
		
		post("/accessdevice/log/", (req, res) -> {

			AccessMessage accessMessage = gson.fromJson(req.body(), AccessMessage.class);

			int id = accesslog.add(accessMessage.getMessage());

			AccessEntry accessEntry = new AccessEntry(id, accessMessage.getMessage());

			return gson.toJson(accessEntry);
		});

		get("/accessdevice/log/", (req, res) -> {

			return accesslog.toJson();
		});

		get("/accessdevice/log/:id", (req, res) -> {

			int id = Integer.parseInt(req.params(":id"));

			AccessEntry accessEntry = accesslog.get(id);

			if(accessEntry == null) {
				res.status(404);
				res.body("ID not found.");
				return "";
			}

			return gson.toJson(accessEntry);
		});

		put("/accessdevice/code/", (req, res) -> {

			accesscode = gson.fromJson(req.body(), AccessCode.class);

			return req.body();

		});

		get("/accessdevice/code/", (req, res) -> {

			return gson.toJson(accesscode);
		});

		delete("/accessdevice/log/", (req, res) -> {

			accesslog.clear();

			return accesslog.toJson();
		});
    }
}
