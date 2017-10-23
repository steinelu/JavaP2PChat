import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * Klasse die Verbindungen akzeptiert und diese in der ArrayList nodes abspeichert.
 * 
 */
public class NetIn {
	
	private Thread thread;
	private ServerSocket socket;
	private Integer port = 9000;
	
	private boolean close = false;
	
	private ArrayList<Node> nodes;
	
	public NetIn(ArrayList<Node> nodes, Control control) {
		this.nodes = nodes;
		
		try {
			this.socket = new ServerSocket(this.port);
		} catch (IOException e) {e.printStackTrace();}
		
		startAcceptThread();
	}
	
	private void startAcceptThread() {
		thread = new Thread() {
			public void run() {
				while(!close) {
					try {
						Socket s = socket.accept();
						//System.out.println(("New Connection: [" + s.getInetAddress() + "]"));
						newNode(s);
					}catch (Exception e) {e.printStackTrace();}
				}
			}
		};
		thread.start();
	}
	
	private void newNode(Socket s) {
		nodes.add(new Node(s, this));
	}
	
	public void connectNode(String ip, int port) {
		try {
			newNode(new Socket(ip, port));
		}catch (IOException e) {e.printStackTrace();}
	}
	
	public void connectNode(String ip) {
		connectNode(ip, port);
	}
	
	public Integer getAcceptPort() {
		return this.port;
	}
	
	public ArrayList<Node> getNodeList() {
		return nodes;
	}
	
	public void disconnect(Node node) {
		//System.out.println("NetIn-Disconnecting: " + node.getInfo());
		node.close();
		nodes.remove(node);
	}
	
	public void disconnect(int index) {
		disconnect(nodes.get(index));
	}
	
	public Node getNode(String ip) {
		for(Node node : nodes) {
			if(node.getIP().equals(ip)) {
				return node;
			}
		}
		return null;
	}
	
	public void close() {
		try {
			close = true;
			thread.join();
			socket.close();
		} catch (Exception e) {e.printStackTrace();}
		
		for(Node node : nodes) {
			node.close();
		}
	}
	
	
}
