package tk.jackyliao123.ssh;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

public class SSHUI{
	public SSH ssh;
	public Terminal terminal;
	public SSHCanvas canvas;
	private Frame frame;
	public DataReaderThread reader;
	public SSHUI(SSH ssh){
		this.ssh = ssh;
		terminal = new Terminal(ssh);
		reader = new DataReaderThread(terminal);
	}
	public void showWindow(){
		frame = new Frame("jSSH");
		canvas = new SSHCanvas(terminal);
		terminal.command = new CommandListener(frame, canvas);

		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowListener(ssh));
		frame.setVisible(true);
	}
	public void startReader(){
		reader.start();
	}
	public static void main(String[] args){
		try{
			SSH ssh = new SSH("192.168.2.17", 22, "pi", "raspberry");
			ssh.connectSession();
			ssh.openChannel();
			SSHUI sshui = new SSHUI(ssh);
			sshui.showWindow();
			sshui.startReader();
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}