import java.util.ArrayList;
import java.util.Scanner;

public class Control {
	private ArrayList<Node> nodes;
	private NetIn net;
	private Scanner scan;
	
	private Protocol node;
	
	private enum command{
		SELECT, CLOSE, EXIT, ADD, TABLE, MESSAGE, LOAD, SAVE
	}
	
	private Control() {
		scan = new Scanner(System.in);
		nodes = new ArrayList<Node>();
		net = new NetIn(nodes, this);
		getMainUserInput();
		net.close();
	}
	
	public void getMainUserInput() {
		String str;
		for(;;) {
			System.out.print(">>>");
			str = scan.nextLine();
			switch(handleCommand(str)) {
			case SELECT:
				if (select()) {
					//getMsgInput();
					NodeMsgWindow.createNodeMsgWindow(node);
				}
				break;
			case ADD:
				add();
				break;
			case LOAD:
			case SAVE:
			case CLOSE:
			case EXIT:
			default:
				break;
			}
		}
	}
	
	private void getMsgInput() {
		String str;
		for(;;) {
			System.out.print(">");
			str = scan.nextLine();
			switch(handleCommand(str)) {
			case CLOSE:
				return;
			case EXIT:
				System.exit(0);
			case TABLE:
				node.send("cmd", "tab");
				break;
			case MESSAGE:
			default:
				node.send(str);
			}
		}
	}

	private command handleCommand(String str) {
		if(str.equals(":select")) {
			return command.SELECT;
		}
		else if(str.equals(":close")) {
			return command.CLOSE;
		}
		else if(str.equals(":exit")) {
			return command.EXIT;
		}
		else if(str.equals(":add")) {
			return command.ADD;
		}
		else if(str.equals(":table")) {
			return command.TABLE;
		}
		else {
			return command.MESSAGE;
		}
	}
	
	public void printUsers() {
		int i;
		for(i = 0; i< nodes.size(); i++) {
			Node n = nodes.get(i);
			System.out.println("["+ i +"]" + n.getInfo());
		}
		if(i==0) {
			System.out.println("Keine User vorhanden!");
		}
	}
	
	private boolean select() {
		String user;
		int id = 0;
		if(nodes.size() < 1) {
			System.out.println("keine User vorhanden!");
			return false;
		}
		printUsers();
		System.out.println("user>");
		user = scan.nextLine();
		if(user.equals(":")) {
			return false;
		}
		
		node = nodes.get(id).getProtocol();
		return true;
	}
	
	private void add() {
		String str;
		System.out.println("Add user:");
		System.out.print("ip>");
		str = scan.nextLine();
		net.connectNode(str);
	}
	
	public void show(String msg, Node node) {
		System.out.println(msg);
	}
	
	public static void main(String[] args) {
		// -Djava.net.preferIPv6Addresses=true
		System.setProperty("java.net.preferIPv6Addresses", "true");
		
		
		new Control();
	}
}
