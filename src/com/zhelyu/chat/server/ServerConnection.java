/**
ServerConnection.java : implementation file 
This is a server class to create a server socket and handle clients when they apply to connect. 
History: 
6 Jun, 2017	--- Zhe Lyu @1 
- Created 
 */


package com.zhelyu.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerConnection {
	public static List<ClientHandler> clients = new ArrayList<ClientHandler>();
	
	void init(){
			//server socket
			Socket socket=null;		
			int count=0;
			try {
				ServerSocket serverSocket=new ServerSocket(8000);
				System.out.println("Wait for connect !");
			
				while(true){
					socket=serverSocket.accept();
					count++;
					//System.out.println(socket);
					ClientHandler client = new ClientHandler(socket);
					if(client.isNameExist("No" + count)){
						count++;
					}
					client.setName("No" + count);
					clients.add(client);
					System.out.println("Client "+client.getName()+" connect to this server!");
					client.sendMessageToClient("UPDATENICKNAME@" + client.getName(), client);
					client.sendMessageToClient("Connect to server " + serverSocket.getLocalSocketAddress().toString() + " successful!",client);
					//client.sendMessageToClient("Weclocme " + client.getName() + " join in chatroom !",client);
					client.sendMessageToAll("Weclocme " + client.getName() + " join in chatroom !");
					client.refreshUserList(client);
					new Thread(client).start();
				}
				
			} catch (IOException e) {
				System.out.println("Cannot use this socket!");
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			
		
	}

}
