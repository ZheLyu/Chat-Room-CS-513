import static org.junit.Assert.*;

import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import com.zhelyu.chat.client.ClientView;
import com.zhelyu.chat.client.ServerHandler;
import com.zhelyu.chat.server.Server;
import com.zhelyu.chat.server.ClientHandler;
import com.zhelyu.chat.server.ServerConnection;

public class test {
	Server server;
	ClientView client;
	//Socket socket=null;
	@Before
	public void setUp() throws Exception {
		//server.main(null);
		//client.main(null);
		
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
		new ServerConnection();
		ServerHandler s=new ServerHandler();
		new ClientView();
		//new ClientHandler(socket);

		
	}

}
