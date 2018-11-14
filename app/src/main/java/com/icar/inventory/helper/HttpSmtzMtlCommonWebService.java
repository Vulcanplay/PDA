package com.icar.inventory.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;

public class HttpSmtzMtlCommonWebService extends WebServiceBase {

	public HttpSmtzMtlCommonWebService() {
		super(null);
	}

	public HttpSmtzMtlCommonWebService(IHttpJSON2Event eventHandler) {
		super(eventHandler, GlobalConfiguration.getInstance()
				.getWebServiceHost());
	}

	public HttpSmtzMtlCommonWebService(IHttpJSON2Event eventHandler,
			String hostName) {
		super(eventHandler, hostName, 6000);
	}

	public HttpSmtzMtlCommonWebService(IHttpJSON2Event eventHandler,
			String hostName, int timeOutInSeconds) {
		super(eventHandler, hostName, timeOutInSeconds);
	}

	@Override
	protected String getWebServcieName() {
		return "CommonWebService.asmx";
	}

	@Override
	protected String getWebMethodName() {

		return "doInsertTempChartNew1";
	}

	@Override
	protected List<NameValuePair> getNameValuePairSendData(
			JSONObject jsonParameters) {
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", jsonParameters
					.getString("tzData")));
			return nameValuePairs;
		} catch (Exception e) {
			return null;
		}
	}

}