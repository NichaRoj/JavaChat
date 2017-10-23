import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServerThread extends Thread {
	private String name;
	private Socket clientSocket;
	private ArrayList<ChatServerThread> clients;
	private PrintWriter out;
	private BufferedReader in;

	public ChatServerThread(Socket socket, ArrayList<ChatServerThread> clients) throws IOException {
		super(socket.getRemoteSocketAddress().toString());
		this.clientSocket = socket;
		this.clients = clients;
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.start();
	}

	public void run() {
		try {
			out.println("Server: Enter your name.");
			name = in.readLine();
			out.println("Server: Welcome to the chat!\n!! There is currently " + clients.size() + " people in this chat.\n!! Type \"/quit\" to exit the chat.");
			sendToAll("Server: "+name + " has joined the chat.");
			while (true) {
				String input = in.readLine();
				if (input.equals("/quit")) {
					sendToAll("Server: "+name+" has disconnected.");
					clients.set(clients.indexOf(this), null);
					break;
				} else {
					sendToAll("<"+name+"> "+input);
				}

			}
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToAll(String s) {
		if (s != null) {
			System.out.println(s);
			for (ChatServerThread client : clients) {
				client.out.println(s);
			}
		}
	}
}