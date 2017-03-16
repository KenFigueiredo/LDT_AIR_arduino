import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Communicator implements SerialPortEventListener {

	private Enumeration ports;
	private HashMap<String, CommPortIdentifier> portMap;
	
	private CommPortIdentifier selectedPortId;
	private SerialPort serialPort;
	private ArrayList<String> portNames;
	
	private InputStream in;
	private OutputStream out;
	
	final static int TIMEOUT = 2000;
	private int baudRate;
	
	public Communicator(){
		ports = null;
		portMap = new HashMap<String, CommPortIdentifier>();
		portNames = new ArrayList<String>();
		selectedPortId = null;
		serialPort = null;
		in = null;
		out = null;
		
		baudRate = 9600;
		
		searchForPorts();
	}
	
	public void searchForPorts(){
		ports = CommPortIdentifier.getPortIdentifiers();
		
		while(ports.hasMoreElements()){
			CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();
			
			if(curPort.getPortType() == CommPortIdentifier.PORT_SERIAL){
				portMap.put(curPort.getName(), curPort);
				portNames.add(curPort.getName());
			}
		}
	}
	
	public void resetPortNames(){
		portNames.clear();
	}
	
	public ArrayList<String> getPortNames(){
		return portNames;
	}
	
	public void setBaud(String baud){
		baudRate = Integer.parseInt(baud);
	}
	
	public void connect(String selectedPort){
			
		selectedPortId = (CommPortIdentifier)portMap.get(selectedPort);
		
		CommPort commPort = null;
		
		try{
			commPort = selectedPortId.open("pcToBoard",TIMEOUT);		
			serialPort = (SerialPort)commPort;
			serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			Console.log(selectedPort + " opened successfully. Baud Rate set: " + serialPort.getBaudRate() + " baud");	
		}catch (PortInUseException e){
			Console.log("Port In Use: " + e.currentOwner.toString());		
		}catch (Exception e){
			Console.log("Failed to open " + selectedPort + ":" + e.getMessage());
		}
	}
	
	public void disconnect(){
		try{
			byte[] a = {(byte)0x00,0x00};
			//writeData(a);
			
			serialPort.removeEventListener();
			serialPort.close();
			in.close();
			out.close();
			Console.log("Disconnected");
		}catch(Exception e){
			Console.log("Failed to disconnect: " + e.getMessage());
		}
	}
	
	public boolean initIOStream(){
		
		try{
			in = serialPort.getInputStream();
			out = serialPort.getOutputStream();
			
			//byte[] a = {(byte)0x00,0x00};
			//writeData(a);		
			return true;
			
		}catch (IOException e){
			Console.log("IO failed to open : " + e.getMessage());
			return false;
		}
	}
	
	public void initListener(){
		try{
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		}catch(TooManyListenersException e){
			Console.log("Too many listeners: " + e.toString());
		}
	}
	
	public void writeData(byte[] array, int type){
		
		// 0 for video
		// 1 for message
	
		Console.log(" - Total bytes to be sent: " + array.length);
		
		try{
			
			out.write(array.length); //write out how many bytes to be sent
			
			for(int i = 0; i < array.length; i++){
				out.write(array[i]);				
			}
			out.flush();
	
		}catch (Exception e){
			Console.log("Failed to write data: " + e.toString());
		}
		
		Console.log("Successfully transmistted message.");
	}
	
	@Override
	public void serialEvent(SerialPortEvent spe) {
		if(spe.getEventType() == SerialPortEvent.DATA_AVAILABLE){
			try{
				Parser.clearScreen();
				Thread.sleep(1);
				Console.log2("R: ");			
				while(in.available() > 0){					
					byte next = (byte) in.read();
					Console.log2(Character.toString((char)next));
				}
				Console.log2("> ");
			}catch(Exception e){
				Console.log("Failed to read data: " + e.getMessage());
			}
		}	
	}

}
