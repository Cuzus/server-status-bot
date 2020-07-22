package org.cuzus.serverstatusbot;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class UdpServer implements Closeable
{
	/**
	 * The type-of-service to send queries under
	 * 
	 * @since  2010-03-15
	 * @see DatagramSocket#setTrafficClass(int)
	 */
	protected static final int IPTOS_RELIABILITY = 0x04;

	public InetSocketAddress baseAddress;

	protected static final Map<InetSocketAddress, UdpServer> servers = Collections.synchronizedMap(new HashMap<InetSocketAddress, UdpServer>());
	
	private static DatagramSocket socket;
	
	private static Reader reader;

	private static class Reader extends Thread
	{
		private Reader() 
		{
			super("UDP Reader");
			setDaemon(true);
			System.out.println("Starting UDP reader thread");
		}

		@Override
		public void run() 
		{
			try 
			{
				while (!servers.isEmpty()) 
				{
					System.out.println("while read data");
					readData(getSocket());
				}
			} 
			catch (SocketException e) {
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			finally {
				System.out.println("Stopping UDP reader thread");

				if (socket != null) {
					socket.close();
				}

				reader = null;
				socket = null;
			}
		}
	}
	
	public UdpServer(InetSocketAddress address)
	{
		System.out.println("Creating " + address);

		this.baseAddress = address;

		servers.put(address, this);

		if (reader == null) {
			startReader();
		}
	}
	
	private static synchronized void startReader() 
	{
		if (reader == null) {
			reader = new Reader();
			reader.start();
		}
	}
	
	private static void readData(DatagramSocket sock) throws IOException {
		byte[] buff = new byte[1400];

		DatagramPacket packet = new DatagramPacket(buff, buff.length);
		sock.receive(packet);
		
		UdpServer server = servers.get(packet.getSocketAddress());

		if (server != null) 
		{
			byte[] data = packet.getData();

			System.out.println("From " + server.baseAddress+ ": " + Arrays.toString(data));
			System.out.println(new String(data, 0, data.length));

			// try
			// {
			// 	FileOutputStream outputStream = new FileOutputStream("data_" + System.currentTimeMillis() + ".txt");
			// 	outputStream.write(data);
			// 	outputStream.close();
			// }
			// catch (Exception e) {}

			server.parseData(data);
		}
	}

	protected abstract void parseData(byte[] data) throws IOException;
	
	private static DatagramSocket getSocket() throws SocketException
	{
		if (socket == null)
		{
			createSocket();
		}

		return socket;
	}
	
	private static synchronized void createSocket() throws SocketException
	{
		if (socket == null)
		{
			socket = new DatagramSocket();

			System.out.println("Opening socket at local port " + socket.getLocalPort());

			try
			{
				socket.setTrafficClass(IPTOS_RELIABILITY);
			} 
			catch (SocketException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void close()
	{
		servers.remove(this.baseAddress);

		if (servers.isEmpty()) 
		{
			System.out.println("No more UDP servers - stopping reader");
			
			if (socket != null) 
			{
				socket.close();
			}
		}
	}
	
	protected void sendData(String data) throws IOException
	{
		System.out.println("To " + baseAddress + " (string): \"" + data + '"');

		sendData(data.getBytes("UTF-8"));
	}

	protected void sendData(byte[] data) throws IOException
	{
		System.out.println("To " + baseAddress + ": " + Arrays.toString(data));

		DatagramPacket req = new DatagramPacket(data, data.length, baseAddress);
		DatagramSocket sock = getSocket();

		sock.send(req);
	}
}