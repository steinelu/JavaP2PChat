import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * Klasse Protocol
 * @author lukas
 *
 */
public class Protocol {
	//private JSONObject obj;
	protected JSONParser parser;
	protected Node node;
	protected NetIn net;
	
	private Userput user;
	
	//private static ArrayList<Integer> mem = new ArrayList<Integer>();
	
	public Protocol(Node node, NetIn net) {
		this.node = node;
		this.net = net;
		this.user = new Userput(node);
		this.parser = new JSONParser();
	}
	
	public void process(String str) {
		//System.out.println("process"+str);
		try {
			JSONObject obj = (JSONObject) parser.parse(str);
			handle(obj);
		} catch (ParseException e) {e.printStackTrace();}
	}
	
	private void handle(JSONObject obj) {
		String str, msg;
		JSONObject tab;
		
		if((msg = (String) obj.get("msg"))!=null) {
			//System.out.println(msg);
			user.output(msg);
		}
		
		if((str = (String) obj.get("cmd")) != null) { 
			if(str.equals("del")) {
				net.disconnect(node);
			}
			else if(str.equals("tab")) {
				send("tab", getTable());
			}
		}
		if((tab = (JSONObject) obj.get("tab")) != null) {
			handleTable(tab);
		}
	}
	public void send(String str) {
		send("msg", str);
	}
	@SuppressWarnings("unchecked") 
	public void send(String key, String value) {
		JSONObject msg = new JSONObject();
		msg.put(key, value);
		node.send(msg.toString());
	}
	
	@SuppressWarnings("unchecked")
	private String getTable() {
		ArrayList<Node> list = net.getNodeList();
		JSONObject obj;
		JSONArray arr = new JSONArray();
		
		for(Node node : list) {
			obj = new JSONObject();
			obj.put("ip", node.getIP());
			obj.put("port", net.getAcceptPort());
			arr.add(obj);
		}
		return (new JSONObject().put("lsit", arr.toString())).toString();
	}
	
	private static String closeString = "";
	@SuppressWarnings({ "serial", "unchecked" })
	public static String getCloseString() {
		return (closeString.equals("")) ? (closeString = (new JSONObject(){{ put("cmd", "del");}}).toString()): closeString;
	}
	
	private void handleTable(JSONObject obj) {
		
	}
	
}
