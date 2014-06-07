package tk.jackyliao123.ssh;

public class Style {
	private Style(){
	}
	public static final short 
	PLAIN = 0x0,
	BOLD = 0x01,
	ITALIC = 0x02,
	UNDERLINE = 0x04,
	CROSSED = 0x08,
	CONCEAL = 0x10,
	NEGATIVE = 0x20,
	USE_PALETTE = 0x40;
	
	public static final short DEFAULT_STYLE = PLAIN;
	public static final byte DEFAULT_FG = 7;
	public static final byte DEFAULT_BG = 0;
	
	public static short setBold(boolean bold, short style){
		return (short)(bold ? style | BOLD : style & ~BOLD);
	}
	public static short setItalic(boolean italic, short style){
		return (short)(italic ? style | ITALIC : style & ~ITALIC);
	}
	public static short setUnderline(boolean underline, short style){
		return (short)(underline ? style | UNDERLINE : style & ~UNDERLINE);
	}
	public static short setCrossed(boolean crossed, short style){
		return (short)(crossed ? style | CROSSED : style & ~CROSSED);
	}
	public static short setConceal(boolean conceal, short style){
		return (short)(conceal ? style | CONCEAL : style & ~CONCEAL);
	}
	public static short setNegative(boolean negative, short style){
		return (short)(negative ? style | NEGATIVE : style & ~NEGATIVE);
	}
	public static short setUsePalette(boolean usePalette, short style){
		return (short)(usePalette ? style | USE_PALETTE : style & ~USE_PALETTE);
	}
	
	public static byte getForeground(byte color){
		return (byte) (color & 0xF);
	}
	public static byte getBackground(byte color){
		return (byte) (color >> 4);
	}
	public static boolean getBold(short style){
		return (style & BOLD) != 0;
	}
	public static boolean getItalic(short style){
		return (style & ITALIC) != 0;
	}
	public static boolean getUnderline(short style){
		return (style & UNDERLINE) != 0;
	}
	public static boolean getCrossed(short style){
		return (style & CROSSED) != 0;
	}
	public static boolean getConceal(short style){
		return (style & CONCEAL) != 0;
	}
	public static boolean getNegative(short style){
		return (style & NEGATIVE) != 0;
	}
	public static boolean getUsePalette(short style){
		return (style & USE_PALETTE) != 0;
	}
	
	public static long encodeFormat(byte c, short style, byte fg, byte bg){
		return (long)c << 32 | style << 16 | fg << 8 | bg;
	}
	public static byte decodeChar(long data){
		return (byte)(data >> 32 & 0xFF);
	}
	public static byte decodeStyle(long data){
		return (byte)(data >> 16 & 0xFF);
	}
	public static short decodeFg(long data){
		return (short)(data >> 8 & 0xFF);
	}
	public static short decodeBg(long data){
		return (short)(data & 0xFF);
	}
}
