package com.ranni.tubeplayer.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class GoogleDriveApiEntity {
	
	//for search result items
	String items;//may be an json string(object/array)
	
	
	List<GoogleDriveApiEntity> childs;
	String title;
	String id;
	String mimeType;
	
	
	public void setItems(String s){
		items=s;
	}
	public String getItems(){
		return items;
	}
	public boolean isSearchResultItemsEmpty(){
		return items.equals("[]");
	}
	
	
	public void setAsChildsBySearchResultItem() throws JSONException{
		if(!isSearchResultItemsEmpty()){
			JSONArray jsonArrayItems  = new JSONArray(getItems());
			for(int i=0; i<jsonArrayItems.length()  ;i++){
				JSONObject item = jsonArrayItems.getJSONObject(i);
				GoogleDriveApiEntity child = new GoogleDriveApiEntity();
				child.setId(item.getString("id"));
				child.setTitle(item.getString("title"));
				child.setMimeType(item.getString("mimeType"));
				addChild(child);
			}
		}
	}
	
	
	public void setChilds(List<GoogleDriveApiEntity> c){
		childs= c;
	}
	public List<GoogleDriveApiEntity> getChilds(){
		return childs;
	}
	public void addChild(GoogleDriveApiEntity c){
		if(getChilds()==null){
			setChilds(new ArrayList<GoogleDriveApiEntity>());
		}
		getChilds().add(c);
	}
	
	
	public void setTitle(String s){
		title=s;
	}
	public String getTitle(){
		return title;
	}
	public void setId(String s){
		id=s;
	}
	public String getId(){
		return id;
	}
	public void setMimeType(String s){
		mimeType=s;
	}
	public String getMimeType(){
		return mimeType;
	}
}
