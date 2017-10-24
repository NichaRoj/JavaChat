import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ChatClient {
	public static void main(String[] args) {
		String hostName = "127.0.0.1";
		int portNumber = 4444;

		if (args.length < 2 || args[0] == null || args[1] == null) {
			System.out.println("Usage: java ChatClient <hostName> <portNumber>\nProgram Terminated");
			System.exit(0);
		} else {
			hostName = args[0];
			portNumber = Integer.parseInt(args[1]);
		}
		try (Socket socket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));) {
			String fromUser;
			while (true) {
				new ReceiveMsg(in).start();
				fromUser = stdin.readLine();
				if (fromUser != null) {
					out.println(fromUser);
					if (fromUser.equals("/quit")) {
						System.out.println("Disconnected from the server.");
						System.exit(0);
					}
				}
			}
		} catch (IOException e) {
		}
	}

	static class ReceiveMsg extends Thread {
		private BufferedReader in;

		public ReceiveMsg(BufferedReader br) {
			in = br;
		}

		public void run() {
			String fromServer;
			try {
				while ((fromServer = in.readLine()) != null) {
					System.out.println(fromServer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
