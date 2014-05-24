package tk.jackyliao123.ssh;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SSHUI{
	public SSHUI(){
		Frame frame = new Frame("jSSH");
		SSHCanvas canvas = new SSHCanvas();
		
		canvas.setPreferredSize(new Dimension(640, 300));
		canvas.setBackground(Color.black);
		canvas.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowListener());
		frame.setVisible(true);
	}
	public static void main(String[] args){
		new SSHUI();
	}
}
class SSHCanvas extends Canvas{
	public int cursorX;
	public int cursorY;
	public boolean cursor = true;
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.white);
		g.setFont(new Font("Courier New", Font.PLAIN, 14));
		g.drawString("root@vps.jackyliao123.tkTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", 0, 9);
		if(cursor)
			g.fillRect(cursorX, cursorY, 8, 12);
		else
			g.drawRect(cursorX, cursorY, 8, 12);
	}
}
class WindowListener extends WindowAdapter{
	public void windowClosing(WindowEvent e){
		System.exit(0);
	}
}