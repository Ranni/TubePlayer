package com.ranni.tubeplayer.util;

public class ResponseMsg {
	private boolean success=false;
	private String responoseMsg="init.";
	
	
	public void setSuccess(boolean s){
		success = s;
	}
	public boolean isSuccess(){
		return success;
	}
	
	
	public void setResponseMsg(String s){
		responoseMsg=s;
	}
	public String getResponseMsg(){
		return responoseMsg;
	}
}
