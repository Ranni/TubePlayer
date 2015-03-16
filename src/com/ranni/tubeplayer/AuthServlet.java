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
		
		// Google取得access_token的url
//		  URL urlObtainToken =  new URL(DriveApi.URL_TOKEN);
//		  HttpURLConnection connectionObtainToken =  (HttpURLConnection) urlObtainToken.openConnection();
//		    
//		  // 設定此connection使用POST
//		  connectionObtainToken.setRequestMethod("POST");
//		  connectionObtainToken.setDoOutput(true);
//		   
//		  // 開始傳送參數 
//		  OutputStreamWriter writer  = new OutputStreamWriter(connectionObtainToken.getOutputStream());
//		  writer.write("code="+req.getParameter("code")+"&");   		// 取得Google回傳的參數code
//		  writer.write("client_id="+DriveApi.CLIENT_ID);  			// 這裡請將xxxx替換成自己的client_id
//		  writer.write("client_secret="+DriveApi.CLIENT_SECRET);   	// 這裡請將xxxx替換成自己的client_serect
////		  writer.write("redirect_uri=http://1-dot-utube4ranni.appspot.com/oauth2callback");   		// 這裡請將xxxx替換成自己的redirect_uri
//		  writer.write("redirect_uri="+DriveApi.REDIRECT_URI);
//		  writer.write("grant_type=authorization_code");  
//		  writer.close();
//		  
//		  System.out.println(connectionObtainToken.getResponseCode());
//		  
//		  // 如果認證成功
//		  if (connectionObtainToken.getResponseCode() == HttpURLConnection.HTTP_OK){
//			   StringBuilder sbLines   = new StringBuilder("");
//			   
//			   // 取得Google回傳的資料(JSON格式)
//			   BufferedReader reader = new BufferedReader(new InputStreamReader(connectionObtainToken.getInputStream(),"utf-8"));
//			   String strLine = "";
//			   while((strLine=reader.readLine())!=null){
//			    sbLines.append(strLine);
//			   }
//			  System.out.println(sbLines.toString());
//			  System.out.println();
//			   try {
//			       // 把上面取回來的資料，放進JSONObject中，以方便我們直接存取到想要的參數
//			    JSONObject jo = new JSONObject(sbLines.toString());
//			    
//			    // 印出Google回傳的access token
//			    resp.getWriter().println(jo.getString(DriveApi.ACCESS_TOKEN)); 
//			   } catch (JSONException e) {
//			    e.printStackTrace();
//			   }
//		  }
	}
}
