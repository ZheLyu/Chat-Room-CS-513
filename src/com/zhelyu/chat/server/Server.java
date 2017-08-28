/**
Server.java : implementation file 
This is a server class to open the server .
History: 
6 Jun, 2017	--- Zhe Lyu @1 
- Created 
*/

package com.zhelyu.chat.server;

public class Server {
	public static void main(String[] args) {
		ServerConnection connect=new ServerConnection();
		connect.init();

	}

}
