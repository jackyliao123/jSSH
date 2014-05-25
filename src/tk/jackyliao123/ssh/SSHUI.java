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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SSHUI{
	private SSH ssh;
	public int cursorX;
	public int cursorY;
	public int consoleWidth = 80;
	public int consoleHeight = 25;
	public boolean cursor = true;
	public SSHCanvas canvas;
	public void showWindow(){
		Frame frame = new Frame("jSSH");
		canvas = new SSHCanvas();

		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowListener());
		frame.setVisible(true);
	}
	public static void main(String[] args){
		try{
			SSHUI sshui = new SSHUI();
			sshui.ssh = new SSH("vps.lolzballs.cf", 22, "jackyliao123", "hello123");
			sshui.ssh.connectSession();
			sshui.ssh.openChannel();
			sshui.showWindow();
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	StringBuffer b = new StringBuffer();
	private class InputReader extends Thread{
		public void run(){
			try{
				int i;
				l:
				while((i = ssh.input.read()) != -1){
					if(i == 27){
						i = ssh.input.read();
						if(i == -1){
							break;
						}
						else if(i == '['){
							StringBuffer escape = new StringBuffer();
							while((i = ssh.input.read()) != -1){
								if('A' <= i && i <= 'Z' || 'a' <= i && i <= 'z'){
									i = ssh.input.read();
									if(i == -1){
										break l;
									}
									break;
								}
								else{
									escape.append((char)i);
								}
							}
							if(i == -1){
								break;
							}
							//System.out.println(escape);
						}
					}
					if(true){
						if(!(32 <= i && i <= 126 || i == '\r' || i == '\n')){
							System.out.println(i);
						}
						b.append((char)i);
						String[] lines = b.toString().split("\n");
						cursorX = lines[lines.length - 1].length();
						cursorY = lines.length > consoleHeight ? consoleHeight - 1 : (lines.length - 1);
						canvas.repaint();
					}
				}
				System.exit(0);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private class SSHCanvas extends Canvas{
		public SSHCanvas(){
			addKeyListener(new KeyListener());
			setPreferredSize(new Dimension(640, 300));
			setBackground(Color.black);
			setCursor(new Cursor(Cursor.TEXT_CURSOR));
			new InputReader().start();
		}
		public void paint(Graphics g){
			g.setColor(new Color(0, 255, 0));
			if(cursor)
				g.fillRect(cursorX * 8, cursorY * 12, 8, 13);
			else
				g.drawRect(cursorX * 8, cursorY * 12, 8, 13);
			
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			g.setColor(new Color(187, 187, 187));
			g.setFont(new Font("Courier New", Font.PLAIN, 13));
			FontMetrics fm = g.getFontMetrics();
			String[] strings = b.toString().split("\n");
			for(int i = 0; i < consoleHeight; i ++){
				String s = "";
				if(strings.length > consoleHeight){
					s = strings[strings.length - consoleHeight + i];
				}
				else{
					if(i < strings.length){
						s = strings[i];
					}
				}
				for(int j = 0; j < s.length(); j ++){
					g.drawString(s.charAt(j) + "", j * 8, fm.getAscent() - 1 + i * 12);
				}
			}
		}
	}
	private class KeyListener extends KeyAdapter{
		public void keyTyped(KeyEvent e){
			try {
				ssh.output.write(e.getKeyChar());
				ssh.output.flush();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	private class WindowListener extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			ssh.channel.disconnect();
			ssh.session.disconnect();
		}
	}
}