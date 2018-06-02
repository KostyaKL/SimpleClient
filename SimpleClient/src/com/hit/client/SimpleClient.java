package com.hit.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class SimpleClient {

	int port;
	
	String serverHostname;
	
	Socket echoSocket;
    ObjectOutputStream outC;
    ObjectInputStream inC;
    
    Object o;
    
	public SimpleClient() {
		port = 12345;
		serverHostname = new String("127.0.0.1");
		echoSocket = null;
        outC = null;
        inC = null;
	}
	
	public static void main(String[] args) {
		SimpleClient run = new SimpleClient();
		run.run();
	}
	
	public void run() {
		
			try {
    	 
	            try {
	                echoSocket = new Socket(serverHostname, port);
	                outC = new ObjectOutputStream(echoSocket.getOutputStream());
	                inC = new ObjectInputStream(echoSocket.getInputStream());
	            } catch (UnknownHostException e) {
	                System.err.println("Unknown host: " + serverHostname);
	                System.exit(1);
	            } catch (IOException e) {
	                System.err.println("Unable to get streams from server");
	                System.exit(1);
	            }
	 
	        	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

	        	o = inC.readObject();
	        	System.out.println(o.toString());
	 
	            while (!echoSocket.isClosed()) {            	
	                System.out.print("client: ");
	                String userInput = stdIn.readLine();
	                if ("quit".equals(userInput)) {
	                	outC.writeObject("stop");
	                    outC.flush();
	                	echoSocket.close();
	                    break;
	                }
	                outC.writeObject(userInput);
	                outC.flush();             
	                
	                
	                o = inC.readObject();
	                
	                if(o == null) {
	                	System.out.println("login error");
	                }
	                else if(o instanceof Boolean) {
	                	if((boolean)o == true) {
	                		System.out.println("good");
	                	} 
	                	else {
	                		System.out.println("bad");
	                	}
	                }
	                
	                else if(o.toString().equals("array")) {
	                	Integer size = (Integer)inC.readObject();
	                	for(int i=0;i<size;i++) {
	                		System.out.println(inC.readObject().toString());
	                	}
	                }
	                
	                else {
	                	System.out.println(o.toString());
	                }
	            }
	 
	            outC.close();
	            inC.close();
	            stdIn.close();  
	            System.err.println("disconected from server");
	        }
			catch(EOFException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	
}
