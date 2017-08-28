/**
CilentHnadler.java : implementation file 
This is a server class to update client list, receive message from clients 
and send messages as required. 
History: 
6 Jun, 2017	--- Zhe Lyu @1 
- Created 
 */


package com.zhelyu.chat.server;

//import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class ClientHandler implements Runnable {
	List<ClientHandler> clients = ServerConnection.clients;
	private String name=null;
	private Socket socket = null;
	InputStream is = null;
	DataInputStream dis = null;
	OutputStream os = null;
	DataOutputStream dos = null;
	boolean flag = true;
	
	public ClientHandler(Socket socket){
		this.socket=socket;
		InputStream is;
		try {
			is = socket.getInputStream();
			dis= new DataInputStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * Refresh client list.
	 * @param cilent
	 * @throws IOException
	 */
	
	 void refreshUserList(ClientHandler client) throws IOException {
		 //System.out.println(clients.toString());
		StringBuffer sb = new StringBuffer("LIST");
		for (ClientHandler  c : clients) {
			sb.append("@" + c.getName());
		}
		client.sendListToClient(sb.toString());

	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String message;
		try {
			while(flag){
				message=dis.readUTF();
				//System.out.println(message.toString());
				String[] arrMessage = message.split("@");
				if(arrMessage[0].equals("UPDATENICKNAME")){
					String oldName=this.getName();
					String newName=arrMessage[1];
					if(!isNameExist(newName)){
						this.setName(newName);
						this.sendMessageToAll(oldName+" has changed the nickname to "+newName+"!");
						System.out.println("Client "+oldName+" has changed the nickname to "+newName+"!");
						this.sendMessageToClient("Renamed successfully !", this);
						this.sendMessageToClient("UPDATENICKNAME@" + getName(), this);
						refreshUserList(this);					
					}else{
						this.sendMessageToClient("The nickname has already existed, please choose another one!", this);
						this.sendMessageToClient("UPDATENICKNAME@" + getName(), this);
					}	
				}else if(arrMessage[0].equals("P2P")){
					
					this.sendMessageToOne(arrMessage[1], arrMessage[2]);
				}else{
					//System.out.println(arrMessage[0].toString());
					this.sendMessageToAll(arrMessage[1]);
				}
				
				}
			}catch (SocketException e) {
				flag = false;
				System.out.println(this.getName()+" disconnect to this server!");
				clients.remove(this);
				try {
					refreshUserList(this);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (EOFException e) {
				flag = false;
				clients.remove(this);
				System.out.println(this.getName()+" disconnect to this server!");
				try {
					refreshUserList(this);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				flag = false;
				System.out.println("Cannot get this message!");
				clients.remove(this);
				System.out.println("Client "+this.getName()+" disconnect to this server!");
				//e.printStackTrace();
			}
		try {
			if (dis != null){
			dis.close();}
			if (is != null){
			is.close();}
			if (socket != null)
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		
	}
	
	/**
	 * Send message by client address.
	 * @param message
	 * @param c
	 * @throws IOException
	 */
	
	public void sendMessageToClient(String message, ClientHandler c)
			throws IOException {
		OutputStream os=c.socket.getOutputStream();
		DataOutputStream dos=new DataOutputStream(os);
		//System.out.println(message);
		dos.writeUTF(message);
		dos.flush();
	}
	void setName(String name){
		this.name=name;		
	}	
	String getName(){
		return name;
	}
	
	/**
	 * Judge if name exist in clients list.
	 * @param name
	 * @return
	 */
	 boolean isNameExist(String name){
		for(ClientHandler c:clients){
			if(c.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Send clients list to all clients.
	 * @param message
	 */
	 
	public void sendListToClient(String message){
		for(ClientHandler c: clients){
			try {
				sendMessageToClient(message, c);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}	
	}
	
	/**
	 * Send message to all clients.
	 * @param message
	 */
	
	public void sendMessageToAll(String message){
		String time=new SimpleDateFormat("h:m:s").format(new Date());
		String msg="[" + getName() + "]" +" "+time+"\n"+message;
		for(ClientHandler c:clients){
			//System.out.println(c);
				try {
					if(c!=this){
					sendMessageToClient(msg, c);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}
	
	/**
	 * Send message to one client.
	 * @param name
	 * @param message
	 */
	
	public void sendMessageToOne(String name, String message){
		String time=new SimpleDateFormat("h:m:s").format(new Date());
		String msg="[" + getName() + "] whisper to you "+" "+time+"\n"+message;
		for(ClientHandler c:clients){
			//System.out.println(name);
			//System.out.println(c.getName());
			if(c.getName().equals(name)){
				try {
					sendMessageToClient(msg, c);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}

}
