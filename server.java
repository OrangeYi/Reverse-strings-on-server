import java.io.*;
import java.net.*;

class server {
	static String req_code;
	static String clientSentence;
	static int r_port = 0;
	public static boolean isInteger( String input ){//function to check whether a string is all numbers
	    try{
	      Integer.parseInt(input);
	      return true;
	    }
	    catch(Exception e){
	      return false;
	    }
	}
	public static void creantUDP(DataOutputStream outToClient) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket();//creat the random UDP connect
		r_port = serverSocket.getLocalPort();//get the random port number
		outToClient.writeBytes(Integer.toString(r_port) + '\n');//sent back the r_port to client
		byte[] receiveData = new byte[1024]; 
		byte[] sendData = new byte[1024]; 
		//System.out.println(receiveData.length);			
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);//ready to get the string
		serverSocket.receive(receivePacket); //get the string
		String sentence = new String(receivePacket.getData());//cover the byte to string
		InetAddress IPAddress = receivePacket.getAddress();//address
		int port = receivePacket.getPort();//port
		String reverseSentence = new StringBuffer(sentence).reverse().toString();//reverse the string
		sendData = reverseSentence.getBytes();//cover to byte
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);//sent back
	}

	public static void creantTCP(String code) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(0); //creat the fix port
		System.out.println("SERVER_PORT=" + welcomeSocket.getLocalPort());//print n_port
		while(true) {		
			Socket connectionSocket = welcomeSocket.accept();//get the req_code from client
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();//read client's req_code
			int req_code_client = Integer.valueOf(clientSentence);
			int req_code_server = Integer.valueOf(code);
			if(req_code_client != req_code_server){//if not match disconnect and wait for other client

				r_port = 999999999;
				outToClient.writeBytes(Integer.toString(r_port) + '\n');//sent back the r_port to client
				continue;
			}
			else{//if match
				creantUDP(outToClient);
		    } 
		}
	}
	public static void main(String args[]) throws Exception {
		String req_code;

		if(args.length != 1){//check the argumnet's number
			System.out.println("Please enter the req_code or correct number of req_code");
			return;
		}
		req_code = args[0];
		if(!(isInteger(req_code))){//check the req_code whether all numbers
			System.out.println("Please enter an integer req_code");
			return;
		}
		creantTCP(req_code);
	}
}