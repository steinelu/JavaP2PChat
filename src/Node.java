import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Klasse Node, die eingehende und ausgehende Nachrichten verwaltet.
 * @author lukas
 *
 */
public class Node {
	
	private Protocol data;
	
	private Socket socket;
	
	private Thread thread;
	
	//private InputStreamReader inputr;
	private DataInputStream input;
	private DataOutputStream output;
	
	private boolean close = false;
	
	//public OutputStream stream;
	
	
	public Node(Socket s, NetIn net) {
		//System.out.println("new Node()");
		socket = s;
		data  = new Protocol(this, net);
		
		setStreams();
		startNodeThread();
	}
	
	private void startNodeThread() {
		//System.out.println("startNodeThread");
		thread = new Thread() {
			public void run() {
				while(!close) {
					try {
						data.process(input.readUTF());
					} catch (Exception e) {e.getStackTrace();}
				}
			}
		};
		thread.start();
	}
	
	public void sendData(String str) {
		data.send("msg", str);
	}
	
	public void send(String str) {
		try {
			output.writeUTF(str);
			//output.write(str.getBytes());
		} catch (Exception e) {e.printStackTrace();}
	}
	
	private void setStreams() {
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public String getIP() {
		return socket.getInetAddress().getHostAddress();
	}
	
	public String getInfo() {
		return socket.toString();
	}
	
	public void close() {
		send(Protocol.getCloseString());
		close = true;
		try {
			thread.join();
			input.close();
			output.close();
			socket.close();
		} catch (Exception e) {e.printStackTrace();}	
	}
	
	public Protocol getProtocol() {
		return data;
	}
	
}