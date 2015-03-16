package com.ranni.tubeplayer.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.gson.Gson;
import com.ranni.tubeplayer.service.GoogleDriveService;

@Path("/AuthRest")
public class AuthRest {
	
	GoogleDriveService mGoogleDriveService = null;
	
	
	/*@GET
    @Path("MacA")
    @Produces(MediaType.APPLICATION_JSON)
    public String test() {
        return "{\"MacAddr\":\"abc\"}";
    }*/
	
	@GET
    @Path("GoogleDriveAuth")
    @Produces(MediaType.APPLICATION_JSON)
    public String GoogleDriveAuth(@QueryParam("code") String _googleDriveAccessCode, @QueryParam("state") String _stateAction) throws JSONException {
		System.out.println("_stateAction="+_stateAction);
		if(mGoogleDriveService==null){
			 mGoogleDriveService = new GoogleDriveService();
			 mGoogleDriveService.setGoogleAccessCode(_googleDriveAccessCode);
		}
		/*
		 * Action Dispatcher
		 */
		
		//return _googleDriveAccessCode;
        return new Gson().toJson(mGoogleDriveService.doGoogleActionDispatcher(_stateAction));
//		return new Gson().toJson(mGoogleDriveService.doGoogleDriveAuth(mGoogleDriveService.MODE_INIT));
    }
	
//	@GET
//    @Path("FindFolder")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String FindFolder(@QueryParam("qFolder") String _qFolder) {
//		System.out.println("_qFolder="+_qFolder);
////		if(_qFolder==null || _qFolder.length()==0)
////			return new Gson().toJson(mGoogleDriveService.isGoogleDriveHasRootFolder());
//		if(mGoogleDriveService==null){
//			 mGoogleDriveService = new GoogleDriveService();
//			 mGoogleDriveService.setGoogleAccessCode(_googleDriveAccessCode);
//		}
//		return new Gson().toJson(mGoogleDriveService.isGoogleDriveHasFolder(_qFolder));
//    }
	
	
	/*@GET
    @Path("TubePlayerFolder")
    @Produces(MediaType.APPLICATION_JSON)
    public String GoogleDriveCreateFolder() {
		
		if(mGoogleDriveService==null){
			String _googleDriveAccessCode = "4/Hobs9kqo4plK4JIrRir0YlezjlygLT6SxyyCQJKrdP4.spvvGqarIaAXgrKXntQAax3gmB_-lwI";
			System.out.println("test="+_googleDriveAccessCode);
			 mGoogleDriveService = new GoogleDriveService();
			 mGoogleDriveService.setGoogleAccessCode(_googleDriveAccessCode);
		}
		//return _googleDriveAccessCode;
//        return new Gson().toJson(mGoogleDriveService.doGoogleAction(_googleAction));
		return new Gson().toJson(mGoogleDriveService.doGoogleDriveCreateRootFolder());
    }
	*/
	
}
