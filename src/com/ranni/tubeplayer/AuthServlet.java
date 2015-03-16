package com.ranni.tubeplayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.*;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;










//import com.google.appengine.api.oauth.OAuthService;
//import com.google.appengine.labs.repackaged.org.json.JSONException;
//import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.ranni.tubeplayer.google.DriveApi;
import com.ranni.tubeplayer.google.Google2Api;

@SuppressWarnings("serial")
public class AuthServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
//		resp.getWriter().println("Hello, world");
//		resp.getWriter().println(req.getParameter("code"));
		String _sCode	= req.getParameter("code");
		DriveApi driveApi = new DriveApi();
		
		OAuthService service = new ServiceBuilder()
		  .provider(Google2Api.class)
	      .apiKey(driveApi.CLIENT_ID)
	      .apiSecret(driveApi.CLIENT_SECRET)
	      .callback(driveApi.REDIRECT_URI)
	      .scope(driveApi.scope_variable)
	      .build();
		
		System.out.println(driveApi.scope_variable);
//		Token requestToken = service.getRequestToken();
//		String authUrl = service.getAuthorizationUrl(requestToken);
//	    Verifier verifier = new Verifier(authUrl);
		Verifier verifier = new Verifier(_sCode);
//		Token accessToken = service.getAccessToken(requestToken, verifier);
		Token accessToken = service.getAccessToken(null, verifier);
		
		
//		AppUtil mAppUtil = new AppUtil();
//		mAppUtil.setGoogleDriveToken(accessToken);
		if(true) return;
		
		// Now let's go and ask for a protected resource!
		OAuthRequest reqOAuth = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
		service.signRequest(accessToken, reqOAuth);
		Response respOAuth = reqOAuth.send();
		// Got it! Lets see what we found...
		String _sBody = respOAuth.getBody();
		  
		// Got it! Lets see what we found...
		resp.getWriter().println(_sBody);
		
		// Google���oaccess_token��url
//		  URL urlObtainToken =  new URL(DriveApi.URL_TOKEN);
//		  HttpURLConnection connectionObtainToken =  (HttpURLConnection) urlObtainToken.openConnection();
//		    
//		  // �]�w��connection�ϥ�POST
//		  connectionObtainToken.setRequestMethod("POST");
//		  connectionObtainToken.setDoOutput(true);
//		   
//		  // �}�l�ǰe�Ѽ� 
//		  OutputStreamWriter writer  = new OutputStreamWriter(connectionObtainToken.getOutputStream());
//		  writer.write("code="+req.getParameter("code")+"&");   		// ���oGoogle�^�Ǫ��Ѽ�code
//		  writer.write("client_id="+DriveApi.CLIENT_ID);  			// �o�̽бNxxxx�������ۤv��client_id
//		  writer.write("client_secret="+DriveApi.CLIENT_SECRET);   	// �o�̽бNxxxx�������ۤv��client_serect
////		  writer.write("redirect_uri=http://1-dot-utube4ranni.appspot.com/oauth2callback");   		// �o�̽бNxxxx�������ۤv��redirect_uri
//		  writer.write("redirect_uri="+DriveApi.REDIRECT_URI);
//		  writer.write("grant_type=authorization_code");  
//		  writer.close();
//		  
//		  System.out.println(connectionObtainToken.getResponseCode());
//		  
//		  // �p�G�{�Ҧ��\
//		  if (connectionObtainToken.getResponseCode() == HttpURLConnection.HTTP_OK){
//			   StringBuilder sbLines   = new StringBuilder("");
//			   
//			   // ���oGoogle�^�Ǫ����(JSON�榡)
//			   BufferedReader reader = new BufferedReader(new InputStreamReader(connectionObtainToken.getInputStream(),"utf-8"));
//			   String strLine = "";
//			   while((strLine=reader.readLine())!=null){
//			    sbLines.append(strLine);
//			   }
//			  System.out.println(sbLines.toString());
//			  System.out.println();
//			   try {
//			       // ��W�����^�Ӫ���ơA��iJSONObject���A�H��K�ڭ̪����s����Q�n���Ѽ�
//			    JSONObject jo = new JSONObject(sbLines.toString());
//			    
//			    // �L�XGoogle�^�Ǫ�access token
//			    resp.getWriter().println(jo.getString(DriveApi.ACCESS_TOKEN)); 
//			   } catch (JSONException e) {
//			    e.printStackTrace();
//			   }
//		  }
	}
}
