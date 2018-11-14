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
import org.json.JSONObject;



import android.os.AsyncTask;

public class HttpJSON2CommonWebService extends WebServiceBase {
	

	public HttpJSON2CommonWebService() {
		super(null);
	}

	public HttpJSON2CommonWebService(IHttpJSON2Event eventHandler) {
		super(eventHandler,GlobalConfiguration.getInstance().getWebServiceHost());
	}

	public HttpJSON2CommonWebService(IHttpJSON2Event eventHandler, String hostName) {
		super(eventHandler,hostName,6000);
	}

	public HttpJSON2CommonWebService(IHttpJSON2Event eventHandler, String hostName,
			int timeOutInSeconds) {
		super(eventHandler,hostName,timeOutInSeconds);
	}

	@Override
	protected String getWebServcieName() {		
		return "test/Service1.svc";//http://180.76.158.206:8733/Service1.svchttp://180.76.158.206:81/test/Service1.svc
	}
	@Override
	protected String getWebMethodName() {
	
		return "GetLoginUser1";
	}
	@Override
	protected List<NameValuePair> getNameValuePairSendData(
			JSONObject jsonParameters) {
		List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();             
		nameValuePairs.add(new BasicNameValuePair("userId","xiaotao"));
		nameValuePairs.add(new BasicNameValuePair("userId","password"));
		return nameValuePairs;
	}

}