package tk.jackyliao123.ssh;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SSHCanvas extends Canvas{
	public SSH ssh;
	public Terminal terminal;
	
	public int fontWidth = 8;
	public int fontHeight = 16;
	
	public SSHCanvas(Terminal terminal){
		this.ssh = terminal.ssh;
		this.terminal = terminal;
		KeyListener listener = new KeyListener(ssh);
		addKeyListener(listener);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(listener);
		setPreferredSize(new Dimension(terminal.consoleWidth * fontWidth, terminal.consoleHeight * fontHeight));
		setBackground(Color.black);
		setCursor(new Cursor(Cursor.TEXT_CURSOR));
	}
	public void update(Graphics g){
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g1 = image.getGraphics();
		if(terminal.reverseVideo){
			g1.setColor(new Color(ColorPalette.COLOR_16[7]));
		}
		else{
			g1.setColor(new Color(0, 0, 0));
		}
		g1.fillRect(0, 0, getWidth(), getHeight());
		paint(g1);
		g.drawImage(image, 0, 0, null);
		g1.dispose();
	}
	public void paint(Graphics g){
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.setColor(new Color(187, 187, 187));
		String font = "Courier New";
		int fontSize = 13;
		
		terminal.checkCursorBounds();
		
		FontMetrics fm = g.getFontMetrics();
		for(int i = 0; i < terminal.buffer.length; i ++){
			long[] data = terminal.buffer[i];
			for(int j = 0; j < data.length; j ++){
				long buf = data[j];
				if(buf != 0){
					byte style = Style.decodeStyle(buf);
					int fg = ColorPalette.decodeColor(Style.decodeFg(buf), Style.getUsePalette(style));
					int bg = ColorPalette.decodeColor(Style.decodeBg(buf), Style.getUsePalette(style));
					byte c = Style.decodeChar(buf);
					boolean negative = Style.getNegative(style) ^ terminal.reverseVideo;
					if(negative){
						g.setColor(new Color(fg));
						g.fillRect(j * fontWidth, i * fontHeight, fontWidth, fontHeight);
						g.setColor(new Color(bg));
					}
					else{
						g.setColor(new Color(bg));
						g.fillRect(j * fontWidth, i * fontHeight, fontWidth, fontHeight);
						g.setColor(new Color(fg));
					}
					
					if(Style.getBold(style))
						style |= Font.BOLD;
					if(Style.getItalic(style))
						style |= Font.ITALIC;
					g.setFont(new Font(font, style, fontSize));
					
					g.drawString(((char)c) + "", j * fontWidth, fm.getAscent() - 1 + i * fontHeight);
				}
			}
		}
		if(terminal.cursor){
			g.setColor(new Color(255, 255, 255));
			g.setXORMode(new Color(0, 0, 0));
			g.fillRect(terminal.cursorX * fontWidth, terminal.cursorY * fontHeight, fontWidth, fontHeight);
			g.setPaintMode();
		}
	}
}