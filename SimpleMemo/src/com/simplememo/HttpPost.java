package com.simplememo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by USER on 2016-10-27.
 */
public class HttpPost {
	private String pageEnc = "UTF-8";
	private String phpDirAddress = null;
	private String sendQueryPhp = null;
	private String tableName = null;

	public HttpPost(String phpDirAddress, String tableName) {
		this.phpDirAddress = phpDirAddress;
		sendQueryPhp = phpDirAddress + "/sendQuery.php";
		this.tableName = tableName;
		String colNameJson = getColNameList();
		colList = jsonToArrayOfColName(colNameJson);
	}

	public String sendQuery(String query) {
		String sqlQuery = "sqlQuery=" + query;
		Debug.d("sqlQuery : " + sqlQuery);
		String postResult = postURL(sendQueryPhp, sqlQuery);
		postResult = postResult.trim();

		return postResult;
	}

	public void insertMemo(String memo) {
		Debug.d("┌insertMemo : " + memo);
		String query = "INSERT INTO " + tableName + "(memo) VALUES('" + memo + "')";
		String postResult = sendQuery(query);
		Debug.d("└postResult : " + postResult);
	}

	public String selectAllOrigin() {
		String query = "SELECT * FROM " + tableName;
		String postResult = sendQuery(query);
		Debug.d("└postResult : " + postResult);
		return postResult;
	}

	public void deleteById(String id) {
		Debug.d("┌id : " + id);
		String query = "DELETE FROM " + tableName + " WHERE id='" + id + "'";
		String postResult = sendQuery(query);
		Debug.d("└postResult : " + postResult);
	}

	public void truncateTable() {
		String query = "TRUNCATE " + tableName;
		String postResult = sendQuery(query);
		Debug.d("└postResult : " + postResult);
	}

	public String getColNameList() {
		Debug.d("getColNameList");
		String urlAdrs = phpDirAddress + "/getColNameList.php";
		return postURL(urlAdrs);
	}

	public String[][] jsonToArray(String jsonOriginData) {
		Debug.d("jsonToArray_jsonOriginData : " + jsonOriginData);
		String[][] resultDataList = null;
		try {
			JSONObject json = new JSONObject(jsonOriginData);
			JSONArray array = json.getJSONArray("data");

			int colLen = array.getJSONObject(0).toString().replaceAll("[{}\"]", "").split(",").length;
			resultDataList = new String[array.length()][colLen];

			for (int i = 0; i < array.length(); i++) {

				String[] jsonLine = array.getJSONObject(i).toString().replaceAll("[{}\"]", "").split(",");
				Debug.d("strLen : " + jsonLine.length);

				for (int j = 0; j < colLen; j++) {
					resultDataList[i][j] = jsonLine[j].split("\\:")[0];
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultDataList;
	}

	public String[] jsonToArrayOfColName(String colNameJson) {

		String[] colNameList = null;
		try {
			JSONObject jsOriginObj = new JSONObject(colNameJson);
			JSONArray jsArray = jsOriginObj.getJSONArray("data");
			colNameList = new String[jsArray.length()];

			for (int i = 0; i < jsArray.length(); i++) {
				colNameList[i] = jsArray.getJSONObject(i).getString("COLUMN_NAME");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return colNameList;
	}

	public void openURL(final String urlString) {
		new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlString);
					HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
					httpConn.setDefaultUseCaches(false);
					httpConn.getInputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public String postURL(final String urlAdrs) {
		return postURL(urlAdrs, null, null);
	}

	public String postURL(final String urlAdrs, final String varQuery) {
		return postURL(urlAdrs, varQuery, null);
	}

	/**
	 * @param urlAdrs
	 * @param varQuery = id=myId&pw=myPw
	 * @return loadPageData
	 */
	public String postURL(final String urlAdrs, final String varQuery, final String enc) {
		Debug.d("┬urlAdrs : " + urlAdrs + "\n"
				+ "└varQuery : " + varQuery + "\n"
				+ "└enc : " + enc
		);

		final StringBuilder tmpResult = new StringBuilder();
		if (enc != null) pageEnc = enc;
		Thread webConnThread = new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlAdrs);
					HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
					httpConn.setDefaultUseCaches(false);
					httpConn.setDoInput(true);
					httpConn.setDoOutput(true);
					httpConn.setRequestMethod("POST");
					httpConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
					if (varQuery != null) {
						OutputStreamWriter outStream = new OutputStreamWriter(httpConn.getOutputStream(), pageEnc);
						PrintWriter writer = new PrintWriter(outStream);
						writer.write(varQuery);
						writer.flush();
					}
					InputStreamReader tmp = new InputStreamReader(httpConn.getInputStream(), pageEnc);
					BufferedReader reader = new BufferedReader(tmp);
					String str;
					while ((str = reader.readLine()) != null)
						tmpResult.append(str + "\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		webConnThread.start();
		try {
			webConnThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return tmpResult.toString();
	}

	String[] colList;

	public String[][] jsonToArrayOfSelectAll(String originData) {
		String[][] resultDataList = null;
		// StringBuilder resultBuilder = new StringBuilder();
		try {
			JSONObject json = new JSONObject(originData);
			JSONArray array = json.getJSONArray("data");
			resultDataList = new String[array.length()][colList.length];
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				String lineResult = "";
				for (int j = 0; j < colList.length; j++) {
					resultDataList[i][j] = jsonObject.getString(colList[j]);

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultDataList;
	}
}
