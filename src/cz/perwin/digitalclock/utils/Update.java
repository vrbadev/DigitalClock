package cz.perwin.digitalclock.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Update {
	private final int projectID;
	private final String apiKey;
	private String versionName;
	private String versionLink;
	private String versionType;
	private String versionFileName;
	private String versionGameVersion;

	private static final String API_NAME_VALUE = "name";
	private static final String API_LINK_VALUE = "downloadUrl";
	private static final String API_RELEASE_TYPE_VALUE = "releaseType";
	private static final String API_FILE_NAME_VALUE = "fileName";
	private static final String API_GAME_VERSION_VALUE = "gameVersion";
	private static final String API_QUERY = "/servermods/files?projectIds=";
	private static final String API_HOST = "https://api.curseforge.com";

	public Update(int projectID) {
		this(projectID, null);
	}

	public Update(int projectID, String apiKey) {
		this.projectID = projectID;
		this.apiKey = apiKey;

		this.actualize();
	}

	public void actualize() {
		URL url = null;

		try {
			url = new URL(API_HOST + API_QUERY + projectID);
		} catch(MalformedURLException e) {
			e.printStackTrace();
			return;
		}

		try {
			URLConnection conn = url.openConnection();

			if(apiKey != null) {
				conn.addRequestProperty("X-API-Key", apiKey);
			}

			conn.addRequestProperty("User-Agent", "DigitalClock plugin");

			final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = reader.readLine();

			JSONArray array = (JSONArray) JSONValue.parse(response);

			if(array.size() > 0) {
				JSONObject latest = (JSONObject) array.get(array.size() - 1);
				versionName = (String) latest.get(API_NAME_VALUE);
				versionLink = (String) latest.get(API_LINK_VALUE);
				versionType = (String) latest.get(API_RELEASE_TYPE_VALUE);
				versionFileName = (String) latest.get(API_FILE_NAME_VALUE);
				versionGameVersion = (String) latest.get(API_GAME_VERSION_VALUE);
			}
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public String getPluginName() {
		return this.versionName.substring(0, this.versionName.lastIndexOf(' '));
	}
	
	public String getVersion() {
		return this.versionName.substring(this.versionName.lastIndexOf(" v")+2, this.versionName.length());
	}
	
	public String getDownloadLink() {
		return this.versionLink;
	}
	
	public String getType() { // napr: 'RELEASE'
		return this.versionType;
	}
	
	public String getFileName() { // napr: 'soubor.jar'
		return this.versionFileName;
	}
	
	public String getBukkitVersion() {
		return this.versionGameVersion;
	}
}