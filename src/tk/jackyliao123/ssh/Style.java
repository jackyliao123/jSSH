package tk.jackyliao123.ssh;

public class Style {
	private Style(){
	}
	public static final byte 
	PLAIN = 0x0,
	BOLD = 0x01,
	ITALIC = 0x02,
	UNDERLINE = 0x04,
	CROSSED = 0x08,
	CONCEAL = 0x10,
	NEGATIVE = 0x20,
	USE_PALETTE = 0x40;
	
	public static final byte DEFAULT_STYLE = PLAIN;
	public static final byte DEFAULT_FG = 7;
	public static final byte DEFAULT_BG = 0;
	
	public static byte setBold(boolean bold, byte style){
		return (byte)(bold ? style | BOLD : style & ~BOLD);
	}
	public static byte setItalic(boolean italic, byte style){
		return (byte)(italic ? style | ITALIC : style & ~ITALIC);
	}
	public static byte setUnderline(boolean underline, byte style){
		return (byte)(underline ? style | UNDERLINE : style & ~UNDERLINE);
	}
	public static byte setCrossed(boolean crossed, byte style){
		return (byte)(crossed ? style | CROSSED : style & ~CROSSED);
	}
	public static byte setConceal(boolean conceal, byte style){
		return (byte)(conceal ? style | CONCEAL : style & ~CONCEAL);
	}
	public static byte setNegative(boolean negative, byte style){
		return (byte)(negative ? style | NEGATIVE : style & ~NEGATIVE);
	}
	public static byte setUsePalette(boolean usePalette, byte style){
		return (byte)(usePalette ? style | USE_PALETTE : style & ~USE_PALETTE);
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
	public static boolean getUsePalette(byte style){
		return (style & USE_PALETTE) != 0;
	}
	
	public static int encodeFormat(byte style, byte fg, byte bg){
		return style << 16 | fg << 8 | bg;
	}
	public static byte decodeStyle(int format){
		return (byte)(format >> 16 & 0xFF);
	}
	public static int decodeFg(int format){
		return format >> 8 & 0xFF;
	}
	public static int decodeBg(int format){
		return format & 0xFF;
	}
}
