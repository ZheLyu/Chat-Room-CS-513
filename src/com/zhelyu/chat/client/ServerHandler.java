/**
ServerHandler.java : implementation file 
This is a client class to create the socket . 
 History: 
 1 Jun, 2017	--- Zhe Lyu @1 
 - Created 
 */

package com.zhelyu.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerHandler {
	
	Socket socket = null;
	OutputStream os = null;
	DataOutputStream dos = null;
	InputStream is = null;
	DataInputStream dis = null;
	boolean flag = true;
	InetAddress address=null;
	String saddress=null;

	String userName = null;
	String localName = null;
	
	/**
	 * Apply socket connection¡£
	 */
	void connect(){
		try {
			socket = new Socket("localhost", 8000);
			address = socket.getLocalAddress();
			saddress=address.getHostAddress();
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			is = socket.getInputStream();
			dis = new DataInputStream(is);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			flag = false;
			System.out.println("The Server isn't exist now!");
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			flag = false;
			System.out.println("The Server isn't exist now!");
			//e.printStackTrace();
		}
		
	}
	
	/**
	 * Close stream and socket.
	 */
	void disconnect(){
		flag=false;
		try {
			if (dos!=null)
			dos.close();
			if (os!=null)
			os.close();
			if (dis!=null)
			dis.close();
			if (is!=null)
			is.close();
			if(socket!=null)
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	
}
