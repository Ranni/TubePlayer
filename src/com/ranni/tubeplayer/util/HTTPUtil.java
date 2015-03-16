package com.ranni.tubeplayer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtil {
	public boolean doPost(String sURL, String data, String cookie, 
		      String referer, String charset) { 
		 
		    boolean doSuccess = false; 
		    java.io.BufferedWriter wr = null; 
		    try { 
		 
		      URL url = new URL(sURL); 
		      HttpURLConnection URLConn = (HttpURLConnection) url.openConnection(); 
		 
		      URLConn.setDoOutput(true); 
		      URLConn.setDoInput(true); 
		      ((HttpURLConnection) URLConn).setRequestMethod("POST"); 
		      URLConn.setUseCaches(false); 
		      URLConn.setAllowUserInteraction(true); 
		      HttpURLConnection.setFollowRedirects(true); 
		      URLConn.setInstanceFollowRedirects(true); 
		 
		      URLConn.setRequestProperty( 
		              "User-agent", 
		              "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) " 
		                  + "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)"); 
		      URLConn.setRequestProperty("Accept", 
		              "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
		      URLConn.setRequestProperty("Accept-Language",  "zh-tw,en-us;q=0.7,en;q=0.3"); 
		      URLConn.setRequestProperty("Accept-Charse",    "Big5,utf-8;q=0.7,*;q=0.7"); 
		      if (cookie != null) 
		        URLConn.setRequestProperty("Cookie", cookie); 
		      if (referer != null) 
		        URLConn.setRequestProperty("Referer", referer); 
		 
		      URLConn.setRequestProperty("Content-Type",    "application/x-www-form-urlencoded"); 
		      URLConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length)); 
		 
		      java.io.DataOutputStream dos = new java.io.DataOutputStream(URLConn.getOutputStream()); 
		      dos.writeBytes(data); 
		 
		      java.io.BufferedReader rd = new java.io.BufferedReader( 
		          new java.io.InputStreamReader(URLConn.getInputStream(),  charset)); 
		      String line; 
		      while ((line = rd.readLine()) != null) { 
		        System.out.println(line); 
		      } 
		 
		      rd.close(); 
		    } catch (java.io.IOException e) { 
		      doSuccess = false; 
		      e.printStackTrace();
		 
		    } finally { 
		      if (wr != null) { 
		        try { 
		          wr.close(); 
		        } catch (java.io.IOException ex) { 
		         ex.printStackTrace();
		        } 
		        wr = null; 
		      } 
		    } 
		 
		    return doSuccess; 
		  } 
		 
		  public boolean doGet(String sURL, String cookie, String referer,   String charset) { 
		    boolean doSuccess = false; 
		    BufferedReader in = null; 
		    try { 
		      URL url = new URL(sURL); 
		      HttpURLConnection URLConn = (HttpURLConnection) url.openConnection(); 
		      URLConn.setRequestProperty( 
		              "User-agent", 
		              "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) " 
		                  + "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)"); 
		      URLConn.setRequestProperty("Accept", 
		              "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
		      URLConn.setRequestProperty("Accept-Language",   "zh-tw,en-us;q=0.7,en;q=0.3"); 
		      URLConn.setRequestProperty("Accept-Charse",     "Big5,utf-8;q=0.7,*;q=0.7"); 
		 
		      if (cookie != null) 
		        URLConn.setRequestProperty("Cookie", cookie); 
		      if (referer != null) 
		        URLConn.setRequestProperty("Referer", referer); 
		      URLConn.setDoInput(true); 
		      URLConn.setDoOutput(true); 
		      URLConn.connect(); 
		      URLConn.getOutputStream().flush(); 
		      in = new BufferedReader(new InputStreamReader(URLConn.getInputStream(), charset)); 
		 
		      String line; 
		      while ((line = in.readLine()) != null) { 
		        System.out.println(line); 
		      } 
		 
		    } catch (IOException e) { 
		      doSuccess = false; 
		      e.printStackTrace();
		      e.printStackTrace(); 
		    } finally { 
		      if (in != null) { 
		        try { 
		          in.close(); 
		        } catch (java.io.IOException ex) { 
		        	ex.printStackTrace();
		        } 
		        in = null; 
		 
		      } 
		    } 
		    
		    return doSuccess; 
		  }
}
