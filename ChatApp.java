import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatApp extends JApplet {
	private JTextField txtEnterYourText;
	private JTextArea txtrChat;

	Socket socket;
	PrintWriter out;
	BufferedReader in;

	/**
	 * Create the applet.
	 */
	public ChatApp() {
		super();
		getContentPane().setLayout(null);
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 800, 600);
		getContentPane().add(panel);

		txtEnterYourText = new JTextField();
		txtEnterYourText.setBounds(0, 450, 500, 50);
		txtEnterYourText.setMinimumSize(new Dimension(800, 100));
		txtEnterYourText.setForeground(Color.LIGHT_GRAY);
		txtEnterYourText.setText("Enter your text here");
		txtEnterYourText.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (txtEnterYourText.getForeground() == Color.LIGHT_GRAY) {
					txtEnterYourText.setForeground(Color.BLACK);
					txtEnterYourText.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (txtEnterYourText.getText().isEmpty()) {
					txtEnterYourText.setForeground(Color.LIGHT_GRAY);
					txtEnterYourText.setText("Enter your text here");
				}
			}
		});
		txtEnterYourText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!txtEnterYourText.getText().isEmpty()) {
						out.println(txtEnterYourText.getText());
						if (txtEnterYourText.getText().equals("/quit")) {
							txtrChat.append("Disconnected from the server\n");
							txtEnterYourText.setEditable(false);
						}
					}
					txtEnterYourText.setText("");
				}
			}
		});
		panel.setLayout(null);
		panel.add(txtEnterYourText);
		txtEnterYourText.setColumns(10);

		txtrChat = new JTextArea();
		txtrChat.setBounds(0, 0, 500, 450);
		txtrChat.setEditable(false);
		panel.add(txtrChat);

	}

	public void init() {
		String hostName;
		int portNumber = 4444;
		
		try {
			hostName = JOptionPane.showInputDialog(this, "Please enter server IP Address");
			socket = new Socket(hostName, portNumber);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			new ReceiveMsg(in).start();
		} catch (IOException e) {
			txtrChat.append("Connection failed. Please close the program\n");
		}
	}

	class ReceiveMsg extends Thread {
		private BufferedReader in;

		public ReceiveMsg(BufferedReader br) {
			in = br;
		}

		public void run() {
			String fromServer;
			try {
				while ((fromServer = in.readLine()) != null) {
					txtrChat.append(fromServer + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
