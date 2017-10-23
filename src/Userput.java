import java.io.IOException;
import java.io.OutputStream;

public class Userput {
	private OutputStream output;
	
	public Userput(Node node) {
		output = System.out;
	}
	
	public void output(String str) {
		try {
			output.write(str.getBytes());
		} catch (IOException e) {e.printStackTrace();}
	}
	
	//public void status(String str) {
	//	output(str);
	//}
	
}
