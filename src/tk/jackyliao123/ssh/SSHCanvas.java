package tk.jackyliao123.ssh;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SSHCanvas extends Canvas{
	public SSH ssh;
	public Terminal terminal;
	private final ArrayList<byte[]> buffer;
	private final ArrayList<int[]> styles;
	
	public int fontWidth = 8;
	public int fontHeight = 16;
	
	public SSHCanvas(Terminal terminal){
		this.ssh = terminal.ssh;
		this.terminal = terminal;
		buffer = terminal.buffer;
		styles = terminal.styles;
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
		g1.setColor(new Color(0, 0, 0));
		g1.clearRect(0, 0, getWidth(), getHeight());
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
		for(int i = 0; i < terminal.buffer.size(); i ++){
			byte[] b = buffer.get(i);
			int[] s = styles.get(i);
			for(int j = 0; j < b.length; j ++){
				if(b[j] != 0){
					byte style = Style.decodeStyle(s[j]);
					int fg = ColorPalette.decodeColor(Style.decodeFg(s[j]), Style.getUsePalette(style));
					int bg = ColorPalette.decodeColor(Style.decodeBg(s[j]), Style.getUsePalette(style));
					boolean negative = Style.getNegative(style);
					if(negative){
						g.setColor(new Color(fg));
						g.fillRect(j * fontWidth, (i - terminal.scroll) * fontHeight, fontWidth, fontHeight);
						g.setColor(new Color(bg));
					}
					else{
						g.setColor(new Color(bg));
						g.fillRect(j * fontWidth, (i - terminal.scroll) * fontHeight, fontWidth, fontHeight);
						g.setColor(new Color(fg));
					}
					
					if(Style.getBold(style))
						style |= Font.BOLD;
					if(Style.getItalic(style))
						style |= Font.ITALIC;
					g.setFont(new Font(font, style, fontSize));
					
					g.drawString(((char)b[j]) + "", j * fontWidth, fm.getAscent() - 1 + (i - terminal.scroll) * fontHeight);
				}
			}
		}
		if(terminal.cursor){
			g.setColor(new Color(255, 255, 255));
			g.setXORMode(new Color(0, 0, 0));
			g.fillRect(terminal.cursorX * fontWidth, (terminal.cursorY - terminal.scroll) * fontHeight, fontWidth, fontHeight);
			g.setPaintMode();
		}
	}
}