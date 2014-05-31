package tk.jackyliao123.ssh;

import java.awt.Color;

public class Style {
	public static final byte 
	PLAIN = 0x0,
	BOLD = 0x01,
	ITALIC = 0x02,
	UNDERLINE = 0x04,
	CROSSED = 0x08,
	CONCEAL = 0x10,
	NEGATIVE = 0x20;
	
	public static final byte
	BLACK = 0,
	RED = 1,
	GREEN = 2,
	YELLOW = 3,
	BLUE = 4,
	MAGENTA = 5,
	CYAN = 6,
	WHITE = 7;
	
	public static final Color[] colors = new Color[]{
		new Color(0, 0, 0),
		new Color(187, 0, 0),
		new Color(0, 187, 0),
		new Color(187, 187, 0),
		new Color(0, 0, 187),
		new Color(187, 0, 187),
		new Color(0, 187, 187),
		new Color(187, 187, 187),
	};
	
	public static final byte DEFAULT_COLOR = WHITE;
	
	byte style = PLAIN;
	byte color = DEFAULT_COLOR;
	public void setBgFg(byte color){
		this.color = color;
	}
	public void setForeground(byte color){
		this.color = (byte)(this.color & 0xF0 | color & 0xF);
	}
	public void setBackground(byte color){
		this.color = (byte)(this.color & 0x0F | (color & 0xF) << 4);
	}
	public byte getForeground(){
		return (byte) (color & 0xF);
	}
	public byte getBackground(){
		return (byte) (color >> 4);
	}
	public void setStyle(byte style){
		this.style = style;
	}
	public void setBold(boolean bold){
		style = (byte)(bold ? style | BOLD : style & ~BOLD);
	}
	public void setItalic(boolean italic){
		style = (byte)(italic ? style | ITALIC : style & ~ITALIC);
	}
	public void setUnderline(boolean underline){
		style = (byte)(underline ? style | UNDERLINE : style & ~UNDERLINE);
	}
	public void setCrossed(boolean crossed){
		style = (byte)(crossed ? style | CROSSED : style & ~CROSSED);
	}
	public void setConceal(boolean conceal){
		style = (byte)(conceal ? style | CONCEAL : style & ~CONCEAL);
	}
	public void setNegative(boolean negative){
		style = (byte)(negative ? style | NEGATIVE : style & ~NEGATIVE);
	}
	public static byte getForeground(byte color){
		return (byte) (color & 0xF);
	}
	public static byte getBackground(byte color){
		return (byte) (color >> 4);
	}
	public static boolean getBold(byte style){
		return (style & BOLD) != 0;
	}
	public static boolean getItalic(byte style){
		return (style & ITALIC) != 0;
	}
	public static boolean getUnderline(byte style){
		return (style & UNDERLINE) != 0;
	}
	public static boolean getCrossed(byte style){
		return (style & CROSSED) != 0;
	}
	public static boolean getConceal(byte style){
		return (style & CONCEAL) != 0;
	}
	public static boolean getNegative(byte style){
		return (style & NEGATIVE) != 0;
	}
	public Style clone(){
		Style style = new Style();
		style.color = color;
		style.style = this.style;
		return style;
	}
}
