package com.ranni.tubeplayer.service;

import java.util.Arrays;
import java.util.List;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.ranni.tubeplayer.entity.GoogleDriveApiEntity;
import com.ranni.tubeplayer.entity.OAuthStateActionEntity;
import com.ranni.tubeplayer.google.DriveApi;
import com.ranni.tubeplayer.google.Google2Api;
import com.ranni.tubeplayer.util.ResponseMsg;

public class GoogleDriveService {
	private String GOOGLE_ROOT_FOLDER						=	"TubePlayerMusic";
	private final int MODE_WITH_NO_AUTH_ACTION	= 0 ;
	private final int MODE_WITH_AUTH_ACTION		= 1 ;
	
	private static final String GOOGLE_API_V2_FILES					=	"https://www.googleapis.com/drive/v2/files";
	private static final String GOOGLE_API_V2_FILES_MEDIA			=	"https://www.googleapis.com/upload/drive/v2/files";
	
	private static final String GOOGLE_MIME_TYPE_FOLDER 	= 	"application/vnd.google-apps.folder";
	private static final String GOOGLE_MIME_TYPE_FILE 		= 	"application/vnd.google-apps.file";
	private static final String GOOGLE_MIME_TYPE_AUDIO		=	"application/vnd.google-apps.audio";
	
	private static final String [] GOOGLE_MIME_TYPES		=	{GOOGLE_MIME_TYPE_FOLDER,  GOOGLE_MIME_TYPE_FILE,  GOOGLE_MIME_TYPE_AUDIO};
	/*
	 * googleService.signRequest(googleAccessToken, reqOAuth);
			Response respOAuth = reqOAuth.send();
			// Got it! Lets see what we found...
			String _sBody = respOAuth.getBody();
			res.setSuccess(true);
			res.setResponseMsg(_sBody);
	 */
	
	Token googleAccessToken = null;
	OAuthService googleService = null;
	String _accessTokenCode = null;
	public void setGoogleAccessCode(String _sCode){
		_accessTokenCode = _sCode;
	}
	public String getGoogleAccessCode(){
		return _accessTokenCode;
	}
	
	
	public ResponseMsg doGoogleActionDispatcher(String _stateAction) throws JSONException{
		ResponseMsg res = new ResponseMsg();
		
		//_stateAction={"action":"ACTION_CREATE_ROOT_FOLDER", "data":"hello" }
		OAuthStateActionEntity mOAuthStateAction = new Gson().fromJson(_stateAction, OAuthStateActionEntity.class);
		if(mOAuthStateAction==null || mOAuthStateAction.getAction()==null || mOAuthStateAction.getAction().length()==0){ 
			res.setResponseMsg("action info is null, You need more information.");
			return res;
		}
		
		String mGoogleAction 	= mOAuthStateAction.getAction();
		String mGoogleData		= mOAuthStateAction.getData();
		System.out.println("services: "+mGoogleAction+"\t"+mGoogleData);
		if(mGoogleAction.equals(OAuthStateActionEntity.ACTION_CREATE_ROOT_FOLDER)){
			//data ignored
			res = doGoogleDriveCreateRootFolder();
		}else if(mGoogleAction.equals(OAuthStateActionEntity.ACTION_QUERY_ROOT_FOLDER)){
			//get root folder id & list
			res = isGoogleDriveHasFile(GOOGLE_ROOT_FOLDER, GOOGLE_MIME_TYPE_FOLDER, 	null, 	MODE_WITH_AUTH_ACTION);
		}
		else if(mGoogleData==null || mGoogleData.length()==0){
			return res;
		}
		//need data
		else if(mGoogleAction.equals(OAuthStateActionEntity.ACTION_CREATE_FOLDER)){
			//create rootFolder/2nd_folder, only 2 level
			res =doGoogleDriveCreateFolder(mGoogleData);
		}else if(mGoogleAction.equals(OAuthStateActionEntity.ACTION_UPLOAD_FILE)){
			//put file to data:{"parent":"parent_folder"}
			//TODO
		}else if(mGoogleAction.equals(OAuthStateActionEntity.ACTION_QUERY_FOLDER)){
			//get folder id & list
			res = isGoogleDriveHasFile(mGoogleData, GOOGLE_MIME_TYPE_FOLDER, 	null, 	MODE_WITH_AUTH_ACTION);
		}else if(mGoogleAction.equals(OAuthStateActionEntity.ACTION_DELETE_FILE)){
			//data: {"dFile":"fileName", "mimeType":"mimeType", "parent":"parentName"}
			JSONObject jsonData2Delete= null;
			String fileName2Delete =null;
			String fileMimeType2Delete =null;
			String parentName =null;
			try{
				jsonData2Delete= new JSONObject(mGoogleData);
				fileName2Delete = jsonData2Delete.getString("dFile");
				fileMimeType2Delete = jsonData2Delete.getString("mimeType");
				parentName = jsonData2Delete.getString("parent");
				
				
			}catch(JSONException e){
				e.printStackTrace();
//				http://localhost:8888/master?tubeAction=aDELETE_FILE&tubeData={\"dFile\":\"file2Delete\", \"mimeType\":\"application/vnd.google-apps.folder\", \"parent\":\"TubePlayerMusic\"}
			}
			
			if(jsonData2Delete==null || fileName2Delete==null || fileMimeType2Delete==null || parentName==null){
				res.setResponseMsg("incorrect data format");
			}else if(Arrays.asList(GOOGLE_MIME_TYPES).contains(fileMimeType2Delete)){
				res = doGoogleDriveDeletePermanentlyFile(fileName2Delete, fileMimeType2Delete,  parentName);
			}
			
		}
		
		
		return res;
	}
	
	public ResponseMsg doGoogleDriveAuth(){
		ResponseMsg res = new ResponseMsg();
		
		DriveApi driveApi = new DriveApi();
		
		googleService = new ServiceBuilder()
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
		Verifier verifier = new Verifier(getGoogleAccessCode());
//		Token accessToken = service.getAccessToken(requestToken, verifier);
//		Token accessToken = service.getAccessToken(null, verifier);
		googleAccessToken = googleService.getAccessToken(null, verifier);
		
		if(googleService!=null && googleAccessToken!=null){
			res.setSuccess(true);
			res.setResponseMsg(getGoogleAccessCode()+","+driveApi.scope_variable);
		}
		
		return res;
	}
	
//	public ResponseMsg isGoogleDriveHasRootFolder() throws JSONException{
//		return isGoogleDriveHasFolder("GOOGLE_ROOT_FOLDER");
//	}
	
	public ResponseMsg doGoogleDriveCreateRootFolder(){
		ResponseMsg res = new ResponseMsg();
		try{
			res = doGoogleDriveCreateFolder(GOOGLE_ROOT_FOLDER);
		}catch(JSONException e){
			e.printStackTrace();
		}
		return res;
	}
	
//	public ResponseMsg  isGoogleDriveHasFolder(String folder) throws JSONException{
//		String folder2Query = (folder.equals("GOOGLE_ROOT_FOLDER")) ? GOOGLE_ROOT_FOLDER: folder;
//		return isGoogleDriveHasFile(folder2Query, GOOGLE_MIME_TYPE_FOLDER, 	null, 	MODE_WITH_AUTH_ACTION);
//	}
	
	public ResponseMsg  isGoogleDriveHasFile(String fileName, String mimeType, String parentId, int MODE) throws JSONException{
		ResponseMsg res = new ResponseMsg();
		
		ResponseMsg resAuth = (MODE==MODE_WITH_NO_AUTH_ACTION)? null : doGoogleDriveAuth();
		if(   MODE==MODE_WITH_NO_AUTH_ACTION   || resAuth.isSuccess()){
			StringBuffer queryQ=new StringBuffer();
			queryQ.append("?q=");
			if(fileName.contains(" ")){
				//FIXME
				//cannot have space in file name...QQ
				fileName = fileName.replaceAll(" ", "_");
			}
			queryQ.append("title+=+'"+fileName+"'");
			if(mimeType!=null && mimeType.length()>0 && Arrays.asList(GOOGLE_MIME_TYPES).contains(mimeType)){
				queryQ.append("+and+mimeType+=+'"+mimeType+"'");
			}
			if(parentId!=null &&parentId.length()>0){
				queryQ.append("+and+'"+parentId+"'+in+parents");
			}
//			if(mimeType.equals(GOOGLE_MIME_TYPE_FILE) && parentName!=null && parentName.length()>0){
//			if( mimeType.equals(GOOGLE_MIME_TYPE_FILE) ){
//				queryQ.append("+and+");
//			}else 
			/*if(mimeType.equals(GOOGLE_MIME_TYPE_FOLDER) && !fileName.equals(GOOGLE_ROOT_FOLDER)){
				//sub-folder(in playlist)
			}else if(mimeType.equals(GOOGLE_MIME_TYPE_FOLDER) && fileName.equals(GOOGLE_ROOT_FOLDER)){
				//root-folder(playlist)
			}*/
//			else if(mimeType.equals(GOOGLE_MIME_TYPE_AUDIO)){
//				
//			}
			//String folder2Query = (folder.equals("GOOGLE_ROOT_FOLDER")) ? GOOGLE_ROOT_FOLDER: folder;
			OAuthRequest request = new OAuthRequest(Verb.GET, GOOGLE_API_V2_FILES+ queryQ.toString().replaceAll(" ","+"));
			System.out.println(queryQ);
//			OAuthRequest request = new OAuthRequest(Verb.GET, GOOGLE_API_V2_FILES+"?q=title+=+'"+folder2Query+"'+and+mimeType+=+'application/vnd.google-apps.folder'");
//			OAuthRequest request = new OAuthRequest(Verb.GET, GOOGLE_API_V2_FILES);
			googleService.signRequest(googleAccessToken, request);
			Response respOAuth = request.send();
			
			String _sBody = respOAuth.getBody();
			System.out.println(_sBody);
			
			GoogleDriveApiEntity resultEntity = new GoogleDriveApiEntity();
			JSONObject resultJson = new JSONObject(_sBody);
			resultEntity.setItems(resultJson.getString("items"));
			
//			System.out.println("-----test----\n"+ resultEntity.isSearchResultItemsEmpty());
			
			
			res.setSuccess( !resultEntity.isSearchResultItemsEmpty());
			if(res.isSuccess()){
				resultEntity.setAsChildsBySearchResultItem();
				List<GoogleDriveApiEntity> mResultItems = resultEntity.getChilds();
//				res.setResponseMsg("got "+mResultItems.size()+" result items.  "+new Gson().toJson(mResultItems));
				res.setResponseMsg(new Gson().toJson(mResultItems));
			}else{
				//empty
				res.setResponseMsg("Is not exist: "+fileName);
			}
			
		}
		
		return res;
	}
	
	public ResponseMsg doGoogleDriveCreateFolder(String folderName) throws JSONException{
		return doGoogleDriveCreateFile(folderName, GOOGLE_MIME_TYPE_FOLDER, null);
//		ResponseMsg res = new ResponseMsg();
//		
//		folderName = (folderName.equals("GOOGLE_ROOT_FOLDER")) ? GOOGLE_ROOT_FOLDER: folderName;
//		
//		boolean createFolderUnderRootFolder = !folderName.equals(GOOGLE_ROOT_FOLDER);
//		//2nd level folder(folder for playlist) must in ROOT_FOLDER
//		
//		//ResponseMsg resAuth = doGoogleDriveAuth();
//		ResponseMsg resIsExist = isGoogleDriveHasFolder(folderName);
//		if(resIsExist.isSuccess()){
//			res.setSuccess(resIsExist.isSuccess());
//			res.setResponseMsg(resIsExist.getResponseMsg());
//		}else{
//			OAuthRequest request = new OAuthRequest(Verb.POST, GOOGLE_API_V2_FILES);
//			
//			JSONObject json = new JSONObject();
//			
//			if(folderName.contains(" ")){
//				//FIXME
//				//cannot have space in file name...QQ
//				folderName = folderName.replaceAll(" ", "_");
//			}
//			json.put("title", (folderName==null || folderName.length()==0)?GOOGLE_ROOT_FOLDER:folderName);
//			json.put("mimeType", "application/vnd.google-apps.folder");
//			if(createFolderUnderRootFolder){
//				/*
//				 * "parents": [{
//				    "kind": "drive#fileLink",
//				    "id": "0Bz0bd074"
//				  }]
//				 */
//				ResponseMsg resRootInfo = isGoogleDriveHasFile(GOOGLE_ROOT_FOLDER, 	GOOGLE_MIME_TYPE_FOLDER,	null, 	 MODE_WITH_NO_AUTH_ACTION);
//				//root folder should only one!
////				System.out.println("i got root : "+resRootInfo.getResponseMsg().replaceAll("\\[", "").replaceAll("\\]", ""));
//				GoogleDriveApiEntity rootEntity = new Gson().fromJson(resRootInfo.getResponseMsg().replaceAll("\\[", "").replaceAll("\\]", ""), GoogleDriveApiEntity.class);
//				//\"kind\":\"drive#parentReference\",   
//				//don't wanna do this....too inconvenient......
////				JSONObject jsonParent = new JSONObject();
////				jsonParent.put("id", rootEntity.getId());
////				JSONArray jsonParents = new JSONArray();
////				jsonParents.put(jsonParent);
//				String jsonObjTemplate = "{\"array\":[{\"id\":\""+rootEntity.getId()+"\"}]}";
//				json.put("parents", new JSONObject(jsonObjTemplate).getJSONArray("array"));
//				System.out.println("we got json: "+json.toString());
//			}
//			
//
//			request.addPayload(json.toString());
//			request.addHeader("Content-Length", ""+json.length());
//			request.addHeader("Content-Type", "application/json");
//			
//			
//			googleService.signRequest(googleAccessToken, request);
//			Response respOAuth = request.send();
//			
//			String _sBody = respOAuth.getBody();
//			System.out.println(_sBody);
//			
//			res.setSuccess(true);
//			res.setResponseMsg(_sBody);
//		}
//		
//		
//		return res;
	}
	public ResponseMsg doGoogleDriveCreateFile(String fileName, String mimeType, String parentName) throws JSONException{
		ResponseMsg res = new ResponseMsg();
		
		fileName = fileName.replaceAll(" ", "_");		//FIXME
//		fileName = (fileName.equals("GOOGLE_ROOT_FOLDER")) ? GOOGLE_ROOT_FOLDER: fileName;
//		if(fileName.equals(GOOGLE_ROOT_FOLDER) && !mimeType.equals(GOOGLE_MIME_TYPE_FOLDER)){
//			
//		}
//		if(fileName.equals(GOOGLE_ROOT_FOLDER)){
//			res.setResponseMsg("Root folder, permission deny");
//			return res;
//		}
		if(!Arrays.asList(GOOGLE_MIME_TYPES).contains(mimeType)){
			res.setResponseMsg("MimeType invalid");
			return res;
		}
		boolean createFolderUnderRootFolder =  !fileName.equals(GOOGLE_ROOT_FOLDER) && mimeType.equals(GOOGLE_MIME_TYPE_FOLDER);
		boolean createAsFile				=  !mimeType.equals(GOOGLE_MIME_TYPE_FOLDER);
		
		//2nd level folder(folder for playlist) must in ROOT_FOLDER
		
		String fileParentFolderId = null;
		if(createAsFile){
			//create file, must have parent folder
			if(parentName!=null && parentName.length()>0){
				ResponseMsg resParentFolderExist =isGoogleDriveHasFile(parentName, GOOGLE_MIME_TYPE_FOLDER, 	null, 	MODE_WITH_AUTH_ACTION);
				if(resParentFolderExist.isSuccess()){
					GoogleDriveApiEntity parentEntity = new Gson().fromJson(resParentFolderExist.getResponseMsg().replaceAll("\\[", "").replaceAll("\\]", ""), GoogleDriveApiEntity.class);
					
					ResponseMsg resFileExist = isGoogleDriveHasFile(fileName, mimeType,  parentEntity.getId(), MODE_WITH_NO_AUTH_ACTION);
					if(resFileExist.isSuccess()){
						res.setResponseMsg("duplicate");
						res.setSuccess(false);
						return res;
					}else{
						fileParentFolderId=parentEntity.getId();
					}
					
				}else{
					res.setResponseMsg("parentFolder is not exist");
					res.setSuccess(false);
					return res;
				}
			}else{
				res.setResponseMsg("Loss parent information");
				res.setSuccess(false);
				return res;
			}
		}else if(createFolderUnderRootFolder){
			//2nd level folder
			ResponseMsg resParentFolderExist =isGoogleDriveHasFile(GOOGLE_ROOT_FOLDER, GOOGLE_MIME_TYPE_FOLDER, 	null, 	MODE_WITH_AUTH_ACTION);
			if(resParentFolderExist.isSuccess()){
				GoogleDriveApiEntity parentEntity = new Gson().fromJson(resParentFolderExist.getResponseMsg().replaceAll("\\[", "").replaceAll("\\]", ""), GoogleDriveApiEntity.class);
				
				ResponseMsg resFileExist = isGoogleDriveHasFile(fileName, mimeType,  parentEntity.getId(), MODE_WITH_NO_AUTH_ACTION);
				if(resFileExist.isSuccess()){
					res.setResponseMsg("duplicate");
					res.setSuccess(false);
					return res;
				}else{
					fileParentFolderId=parentEntity.getId();
				}
				
			}else{
				res.setResponseMsg("roottFolder is not exist");
				res.setSuccess(false);
				return res;
			}
		}else{
			//create root folder
			ResponseMsg resParentFolderExist =isGoogleDriveHasFile(GOOGLE_ROOT_FOLDER, GOOGLE_MIME_TYPE_FOLDER, 	null, 	MODE_WITH_AUTH_ACTION);
			if(resParentFolderExist.isSuccess()){
				res.setResponseMsg("duplicate");
				res.setSuccess(false);
				return res;
			}
		}
		
		OAuthRequest request = new OAuthRequest(Verb.POST, GOOGLE_API_V2_FILES);
		JSONObject json = new JSONObject();
		json.put("title",  		fileName );
		json.put("mimeType",  	mimeType);
		if(fileParentFolderId!=null){
			String jsonObjTemplate = "{\"array\":[{\"id\":\""+fileParentFolderId+"\"}]}";
			json.put("parents", new JSONObject(jsonObjTemplate).getJSONArray("array"));
		}
		
		request.addPayload(json.toString());
		request.addHeader("Content-Length", ""+json.length());
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Content-Transfer-Encoding", "base64");
		
		
		googleService.signRequest(googleAccessToken, request);
		Response respOAuth = request.send();
		
		String _sBody = respOAuth.getBody();
		System.out.println(_sBody);
		
		res.setSuccess(true);
		res.setResponseMsg(_sBody);
		
		return res;
	}
	
	
	//delete action permanently
	public ResponseMsg  doGoogleDriveDeletePermanentlyFile(String fileName2Delete, String mimeType, String parentFolder) throws JSONException{
		
		ResponseMsg res = new ResponseMsg();
		
		if(fileName2Delete.toUpperCase().equals(GOOGLE_ROOT_FOLDER)){
			res.setResponseMsg("You cannot delete root folder");
			return res;
		}
		if( mimeType==null || mimeType.length()==0){
			res.setResponseMsg("Loss mimeType information");
			return res;
		}
		
		String fileId = null;
		//data: {"dFile":"fileName", "mimeType":"mimeType", "parent":"parentName"}
		if(mimeType.equals(GOOGLE_MIME_TYPE_FOLDER)){
			//wanna delete playlist.
			ResponseMsg resFolderExist =isGoogleDriveHasFile(fileName2Delete, GOOGLE_MIME_TYPE_FOLDER, 	null, 	MODE_WITH_AUTH_ACTION);
			
			if(resFolderExist.isSuccess()){
				GoogleDriveApiEntity entity = new Gson().fromJson(resFolderExist.getResponseMsg().replaceAll("\\[", "").replaceAll("\\]", ""), GoogleDriveApiEntity.class);
				fileId = entity.getId();
			}else{
				res.setResponseMsg("This folder is not exist");
				res.setSuccess(false);
				return res;
			}
		}else if(parentFolder!=null && parentFolder.length()>0){
			//wanna delete audio file.
			//1. check parentFolder is exist.(&get parent info)
			//2. if parentIsExist, then check fileIsExist(&get all query result of  fileName='file' info)
			//3. parse info of above, then find out real ID of file.
			ResponseMsg resParentFolderExist =isGoogleDriveHasFile(parentFolder, GOOGLE_MIME_TYPE_FOLDER, 	null, 	MODE_WITH_AUTH_ACTION);
			if(resParentFolderExist.isSuccess()){
				GoogleDriveApiEntity parentEntity = new Gson().fromJson(resParentFolderExist.getResponseMsg().replaceAll("\\[", "").replaceAll("\\]", ""), GoogleDriveApiEntity.class);
				
				ResponseMsg resFileExist = isGoogleDriveHasFile(fileName2Delete, mimeType,  parentEntity.getId(), MODE_WITH_NO_AUTH_ACTION);
				if(resFileExist.isSuccess()){
					GoogleDriveApiEntity entity = new Gson().fromJson(resFileExist.getResponseMsg().replaceAll("\\[", "").replaceAll("\\]", ""), GoogleDriveApiEntity.class);
					fileId = entity.getId();
				}else{
					res.setResponseMsg("file is not exist");
					res.setSuccess(false);
					return res;
				}
			}else{
				res.setResponseMsg("parentFolder is not exist");
				res.setSuccess(false);
				return res;
			}
//			ResponseMsg resFileExist = isGoogleDriveHasFile(fileName, mimeType, MODE);
		}else{
			res.setResponseMsg("Loss parentFolder information");
			res.setSuccess(false);
			return res;
		}
		
		OAuthRequest request = new OAuthRequest(Verb.DELETE, GOOGLE_API_V2_FILES+"/"+fileId);
		googleService.signRequest(googleAccessToken, request);
		Response respOAuth = request.send();
		
		String _sBody = respOAuth.getBody();
		System.out.println(_sBody+"\t"+fileId);
		res.setSuccess(true);
		res.setResponseMsg(fileId);
		
		return res;
		
//		String folder2Query = (folder.equals("GOOGLE_ROOT_FOLDER")) ? GOOGLE_ROOT_FOLDER: folder;
//		return isGoogleDriveHasFile(folder2Query, GOOGLE_MIME_TYPE_FOLDER, 	MODE_WITH_AUTH_ACTION);
	}
	
}
