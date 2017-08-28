/**
 ClientView.java : implementation file 
This is a view class to create a frame for user to connect to the chat room, send and receive messages, 
change name and whisper with each other. 
 History: 
 1 Jun, 2017	--- Zhe Lyu @1 
 - Created 
 */
package com.zhelyu.chat.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Font;


public class ClientView extends JFrame {
	
	ServerHandler serverHandler=new ServerHandler();
	
	

	private JPanel contentPane;
	private JTextField textFieldContent = new JTextField();
	private JTextArea textAreaContent = new JTextArea();
	private JLabel labelLocalIP = new JLabel("");
	private JLabel labelName = new JLabel("User Name:");
	private JTextField textFieldName = new JTextField(20);
	private JButton btnConfirm = new JButton("Change");
	private JButton btnSend = new JButton("Send");
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(listModel);
	ButtonGroup bg=null;
	private JRadioButton cbP2C=new JRadioButton("P2C",true);
	private JRadioButton cbP2P=new JRadioButton("P2P");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientView frame = new ClientView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientView() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 601, 404);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.setSize(542, 465);
		setLocation(250, 150);
		setVisible(true);
		setTitle("ChatRoom");
		JPanel panel = new JPanel(new GridLayout(1, 6));
		panel.add(new Label("IP:"));
		panel.add(labelLocalIP);
		panel.add(labelName);
		panel.add(textFieldName);
		panel.add(btnConfirm);
		panel.setBorder(new TitledBorder("User Information"));
		getContentPane().add(panel, BorderLayout.NORTH);

		JPanel panelCenter = new JPanel(new BorderLayout());
		textAreaContent.setFont(new Font("Microsoft JhengHei UI Light", Font.BOLD | Font.ITALIC, 15));
		textAreaContent.setEditable(false);
		textAreaContent.setFocusable(false);
		JScrollPane scroll = new JScrollPane(textAreaContent);
		panelCenter.add(scroll);
		panelCenter.setBorder(new TitledBorder("Message"));
		// this.add(panelCenter );

		JPanel panelList = new JPanel(new BorderLayout());
		panelList.setBorder(new TitledBorder("Online User"));
		list.setFont(new Font("Microsoft JhengHei", Font.BOLD | Font.ITALIC, 15));
		JScrollPane scroll1 = new JScrollPane(list);
		panelList.add(scroll1);
		//panelList.add(list);

		JSplitPane jspane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				panelCenter, panelList);
		jspane.setDividerLocation(350);
		getContentPane().add(jspane);
		
		JPanel panelChoise = new JPanel();
		bg=new ButtonGroup();
		bg.add(cbP2P);
		bg.add(cbP2C);
		panelChoise.add(cbP2C);
		panelChoise.add(cbP2P);
		//panelChoise.add(btnFile);
		JPanel panelFoot = new JPanel(new BorderLayout());
		panelFoot.add(textFieldContent);
		panelFoot.add(btnSend, BorderLayout.EAST);
		panelFoot.add(panelChoise, BorderLayout.NORTH);
		panelFoot.setBorder(new TitledBorder("Send Message"));
		getContentPane().add(panelFoot, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Close Window ");
				serverHandler.disconnect();
				System.exit(0);
			}
		});
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickEnter();
			}
		});
		textFieldContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickEnter();
			}
		});
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickUpdateName();
			}

		});
		
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				serverHandler.userName=list.getSelectedValue();
			}
		});

		serverHandler.connect();
		labelLocalIP.setText(serverHandler.saddress);
		new Thread(new MessageHandler()).start();
		
		
	}
	/**
	 * Handle the client.
	 */
	class MessageHandler implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//serverHandler.flag=true;
				if(!serverHandler.flag){
					textAreaContent.setText("Server closed, disconnect!");
				}
				try {
					while (serverHandler.flag) {
						
						String message = serverHandler.dis.readUTF();
						//System.out.println(message.toString());
						String[] arrMessage = message.split("@");
						//System.out.println(message);
						if (arrMessage[0].equals("UPDATENICKNAME")) {
							serverHandler.localName=arrMessage[1];
							//System.out.println(serverHandler.localName);
							textFieldName.setText(serverHandler.localName);
							setTitle(arrMessage[1]);
						} else if (arrMessage[0].equals("LIST")) {
							listModel.removeAllElements();
							for (int i = 1; i < arrMessage.length; i++) {
								listModel.addElement(arrMessage[i]);
							}
						}
						else {
							
							textAreaContent.append(message + "\n");
						}

					}
				} catch (EOFException e) {
					serverHandler.flag = false;
					textAreaContent.setText("Server closed, disconnect!");
					System.out.println("disconnect");
					// e.printStackTrace();
				} catch (SocketException e) {
					serverHandler.flag = false;
					textAreaContent.setText("Server closed, disconnect!");
					System.out.println("disconnect");
					// e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					serverHandler.flag = false;
					System.out.println("Cannot get the message!");
					//e.printStackTrace();
				}
		}

	}
	/**
	 * Check to change a new nickname.
	 */
	private void onClickUpdateName() {
		String name = textFieldName.getText().trim();
		if (name != null && !name.equals("")) {
			sendNameToServer("UPDATENICKNAME@" + name);
		} else {
			showAlert("No Input !");
		}

	}
	/**
	 * Check to send message to server.
	 */
	private void onClickEnter() {
		String message = textFieldContent.getText().trim();
		if (message != null && !message.equals("")) {
			String time = new SimpleDateFormat("h:m:s").format(new Date());
			textAreaContent.append("This Client " + time + "\n" + message + "\n");
			textFieldContent.setText("");
			sendMessageToServer(message);
		}else{
			showAlert("No Input !");
		}
	}
	

	private void showAlert(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Wrong",
				JOptionPane.ERROR_MESSAGE);

	}
	
	/**
	 * Send new name to server.
	 * 
	 */
	private void sendNameToServer(String message) {
		try {
			serverHandler.dos.writeUTF(message);
			serverHandler.dos.flush();
		} catch (IOException e) {
			textAreaContent.setText("Send message fail!\n");
			System.out.println("Send message fail! \n");
			//e.printStackTrace();
		}
	}
	
	/**
	 * Send message to server.
	 * 
	 */
	
	
	private void sendMessageToServer(String message) {
		try {
			
			if(cbP2C.isSelected()){
				serverHandler.dos.writeUTF("P2C@"+message);
				serverHandler.dos.flush();
			}else if(cbP2P.isSelected()){
				//System.out.println("c----->P2P"+cbP2P.isSelected());
				if(serverHandler.userName==null){
					showAlert("Please choose one client!");
					textAreaContent.append("Send message fail!\n");
					return ;
				}
				if(serverHandler.userName.equals(serverHandler.localName)){
					showAlert(" You cannot send to yourself!");
					textAreaContent.append("Send message fail!");
					return;
				}
				
				String msg="P2P@"+serverHandler.userName+"@"+message;
				serverHandler.dos.writeUTF(msg);
				serverHandler.dos.flush();
			}
			
		} catch (IOException e) {
			textAreaContent.append("Send message fail!");
			//textAreaContent.setText("Send message fail!");
			System.out.println("Send message fail!");
			//e.printStackTrace();
		}
	}
	
	

}
