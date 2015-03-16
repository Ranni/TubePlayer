package com.ranni.tubeplayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
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
import com.ranni.tubeplayer.util.HTTPUtil;

@SuppressWarnings("serial")
public class TubePlayerServlet extends HttpServlet {
	private final String TUBE_ACTION_LOGIN 			= "aLOGIN"; 
	private final String TUBE_ACTION_FIND_ROOT 		= "aFIND_ROOT"; 
	private final String TUBE_ACTION_CREATE_ROOT	= "aCREATE_ROOT"; 
	
	private final String TUBE_ACTION_FIND_FOLDER 		= "aFIND_FOLDER"; 
	private final String TUBE_ACTION_CREATE_FOLDER		= "aCREATE_FOLDER"; 
	private final String TUBE_ACTION_DELETE_FILE		= "aDELETE_FILE"; 
//	private final String TUBE_ACTION_FIND_FILE 		= "aFIND_FILE"; 
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String tubeAction = req.getParameter("tubeAction");
		String tubeData = req.getParameter("tubeData");
		
		System.out.println(tubeAction + "\t"+tubeData);
		if(tubeAction==null){
			resp.setContentType("text/plain");
			resp.getWriter().println("Loss some action parameters.");
			return;
		}
		
//		if(tubeData!=null){
//			//TODO
//			tubeData=tubeData.replaceAll("'", "");
//		}
		
//		if(tubeAction.equals(TUBE_ACTION_LOGIN)){
		if(tubeAction.equals(TUBE_ACTION_FIND_ROOT)){
//			req.getRequestDispatcher(DriveApi.GOOGLE_AUTH_API_URL).forward(req, resp);;
//			System.out.println(new DriveApi().GOOGLE_AUTH_API_URL);
//			resp.sendRedirect(new DriveApi().GOOGLE_CREATE_ROOT_FOLDER_URL);
			resp.sendRedirect(new DriveApi().GOOGLE_FIND_ROOT_FOLDER_URL);
		}else if(tubeAction.equals(TUBE_ACTION_CREATE_ROOT)){
			resp.sendRedirect(new DriveApi().GOOGLE_CREATE_ROOT_FOLDER_URL);
		}else if(tubeAction.equals(TUBE_ACTION_LOGIN)){
			resp.sendRedirect(new DriveApi().GOOGLE_AUTH_API_URL);
		}
		else if(tubeData==null){
			resp.setContentType("text/plain");
			resp.getWriter().println("Loss some data parameters.");
			return;
		}
		//need data
		else if(tubeAction.equals(TUBE_ACTION_FIND_FOLDER)){
			resp.sendRedirect(new DriveApi().GOOGLE_FIND_FOLDER_URL.replace(DriveApi.FILL_DATA, tubeData));
		}else if(tubeAction.equals(TUBE_ACTION_CREATE_FOLDER)){
			resp.sendRedirect(new DriveApi().GOOGLE_CREATE_FOLDER.replace(DriveApi.FILL_DATA, tubeData));
		}else if(tubeAction.equals(TUBE_ACTION_DELETE_FILE)){
			resp.sendRedirect(new DriveApi().GOOGLE_DELETE_FILE_URL.replace(DriveApi.FILL_DATA, tubeData));
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		//new HTTPUtil().doPost("http://localhost:8888/master", "tubeAction=aLOGIN", null, null, null);
		try {
			doPost(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
		
		
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
