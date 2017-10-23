import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer {
	public static void main(String[] args) throws IOException {
		int portNumber = 4444;
		if(args.length < 1) {
			System.out.println("Usage: java ChatServer <portNumber>\nProgram Terminated");
			System.exit(0);
		}
		portNumber = Integer.parseInt(args[0]);
		ArrayList<ChatServerThread> clients = new ArrayList<ChatServerThread>();
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			System.out.println("Server started");
			while (true) {
				clients.add(new ChatServerThread(serverSocket.accept(), clients));
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port " + portNumber);
			System.exit(0);
		}
	}
}
