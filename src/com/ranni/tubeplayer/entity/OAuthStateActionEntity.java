package com.ranni.tubeplayer.entity;

public class OAuthStateActionEntity {
	//_stateAction={"action":"ACTION_CREATE_ROOT_FOLDER", "data":"hello" }
	
	public static final String ACTION_CREATE_ROOT_FOLDER		=	"ACTION_CREATE_ROOT_FOLDER";
	public static final String ACTION_CREATE_FOLDER				=	"ACTION_CREATE_FOLDER";
	public static final String ACTION_UPLOAD_FILE				=	"ACTION_UPLOAD_FILE";
	
	
	public static final String ACTION_QUERY_ROOT_FOLDER			=	"ACTION_QUERY_ROOT_FOLDER";
	public static final String ACTION_QUERY_FOLDER				=	"ACTION_QUERY_FOLDER";
	public static final String ACTION_QUERY_FILE				=	"ACTION_QUERY_FILE";
	
	public static final String ACTION_DELETE_FILE				=	"ACTION_DELETE_FILE";
	
	
	private String action;
	private String data;
	
	public void setAction(String s){
		action=s;
	}
	public String getAction(){
		return action;
	}

	public void setData(String s){
		data=s;
	}
	public String getData(){
		return data;
	}
}
