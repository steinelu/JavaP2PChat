import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Action;

public class NodeMsgWindow {
	
	public static ArrayList<NodeMsgWindow> windows = new ArrayList<NodeMsgWindow>();
	
	private JFrame frame;
	private final Action action = new SwingAction();
	
	private JTextArea screen;
	private JTextPane msg;
	
	private Protocol node;

	/**
	 * Launch the application.
	 */
	public static void createNodeMsgWindow(Protocol node) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NodeMsgWindow window = new NodeMsgWindow(node);
					window.frame.setVisible(true);
					windows.add(window);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private NodeMsgWindow(Protocol node) {
		this.node = node;
		node.setWindow(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		msg = new JTextPane();
		msg.setText("msg");
		msg.setBounds(15, 202, 311, 26);
		frame.getContentPane().add(msg);
		
		JButton sendbtn = new JButton("send");
		sendbtn.setAction(action);
		sendbtn.setBounds(341, 202, 72, 29);
		frame.getContentPane().add(sendbtn);
		
		screen = new JTextArea();
		screen.setBounds(15, 16, 398, 170);
		frame.getContentPane().add(screen);
	}
	
	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction() { 
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			String str = msg.getText();
			node.send(str);
			addMsgScreen(str);
			msg.setText("");
		}
	}
	
	public void addMsgScreen(String msg) {
		screen.setText(screen.getText() + "\n" + msg);
	}
}
