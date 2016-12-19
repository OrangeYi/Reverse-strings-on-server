import java.io.*; 
import java.net.*; 
class client {

	static String server_address = null;
	static String n_ports = null;
	static int n_port = 0;
	static String req_code = null;
	static String msg = null;
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
	public static void creatTCP() throws Exception {
		Socket clientSocket = new Socket(server_address, n_port);//connect TCP
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());//ready to send req_code
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//ready to get r_port
		outToServer.writeBytes(req_code + '\n');//sent the req_code to server
		r_port = Integer.valueOf(inFromServer.readLine());//get the r_port and rendy to connect the UDP
		clientSocket.close();//close the socket
	} 

	public static void creatUDP() throws Exception {
		DatagramSocket clientSocketUDP = new DatagramSocket();//creat the new UDP socket
		InetAddress IPAddress = InetAddress.getByName(server_address);
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = msg.getBytes();//ready to send
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, r_port);//connect UDP
		clientSocketUDP.send(sendPacket);//send msg to server
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocketUDP.receive(receivePacket);//get the reverse msg
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println(modifiedSentence);//print the reverse msg
		clientSocketUDP.close();
	}

	public static void main(String args[]) throws Exception {


		if(args.length != 4){//check the number of arguments
			System.out.println("Please enter <server_address> , <n_port>, <req_code>, and <msg>");
			return;
		}

		server_address = args[0];
		n_ports = args[1];
		req_code = args[2];
		msg = args[3];
		
		if(msg.getBytes().length > 1024){
			System.out.println("Msg over 1024 bytes");
			return;
		}

		if(!((isInteger(req_code)) && (isInteger(n_ports)))){//check the req_code whether all numbers
			System.out.println("Please enter an integer req_code/n_port");
			return;
		}
		n_port = Integer.valueOf(n_ports);
		creatTCP();

		if(r_port == 999999999){//if req_code not match than server will send back r_port = 999999999
			return;
		}
		else{
			creatUDP();//once match the req_code creat the UDP connect
		}
	}

}