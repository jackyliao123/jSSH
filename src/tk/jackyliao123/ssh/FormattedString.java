//package tk.jackyliao123.ssh;
//
//import java.awt.Color;
//
//public class FormattedString {
//	private byte[] c;
//	private int length;
//	private int styleUpdates;
//	
//	public static final byte 
//	PLAIN = 0x0,
//	BOLD = 0x01,
//	ITALIC = 0x02,
//	UNDERLINE = 0x04,
//	CROSSED = 0x08,
//	CONCEAL = 0x10;
//	
//	public static final byte
//	BLACK = 0,
//	RED = 1,
//	GREEN = 2,
//	YELLOW = 3,
//	BLUE = 4,
//	MAGENTA = 5,
//	CYAN = 6,
//	WHITE = 7;
//	
//	public static final byte ESCAPE = 27;
//	
//	public static final Color[] colors = new Color[]{
//		new Color(0, 0, 0),
//		new Color(187, 0, 0),
//		new Color(0, 187, 0),
//		new Color(187, 187, 0),
//		new Color(0, 0, 187),
//		new Color(187, 0, 187),
//		new Color(0, 187, 187),
//		new Color(187, 187, 187),
//	};
//	
//	public static final byte DEFAULT_COLOR = WHITE;
//	
//	private byte style = PLAIN;
//	private byte color = DEFAULT_COLOR;
//	public FormattedString(){
//		c = new byte[16];
//	}
//	private void checkCapacity(int length){
//		while(this.length + length > c.length){
//			byte[] temp = c;
//			c = new byte[temp.length * 2];
//			System.arraycopy(temp, 0, c, 0, this.length);
//		}
//	}
//	private int convertIndex(int index){
//		if(index > length - styleUpdates * 3 || index < 0){
//			throw new IndexOutOfBoundsException("" + index);
//		}
//		int i;
//		for(i = 0; i < length; i++){
//			if(c[i] == ESCAPE){
//				i += 2;
//			}
//			else{
//				index -= 1;
//			}
//			if(index == -1){
//				return i;
//			}
//		}
//		if(index == 0){
//			return length;
//		}
//		throw new RuntimeException();
//	}
//	public void appendChar(int c){
//		checkCapacity(1);
//		this.c[length] = (byte)c;
//		++length;
//	}
//	public void appendString(String s){
//		checkCapacity(s.length());
//		System.arraycopy(s.getBytes(), 0, c, length, s.length());
//		length += s.length();
//	}
//	public void removeCharAt(int index){
//		removeChars(index, index + 1);
//	}
//	private static byte[] getLastStyle(byte[] b){
//		byte[] result = null;
//		for(int i = 0; i < b.length; i ++){
//			if(b[i] == ESCAPE){
//				if(i < b.length - 2){
//					result = new byte[]{b[i + 1], b[i + 2]};
//				}
//			}
//		}
//		return result;
//	}
//	public void removeChars(int begin, int end){
//		begin = convertIndex(begin);
//		end = convertIndex(end);
//		byte[] b = new byte[end - begin];
//		System.arraycopy(c, begin, b, 0, end - begin);
//		byte[] style = getLastStyle(b);
//		int offset = 0;
//		if(style != null){
//			offset = 3;
//		}
//		System.arraycopy(c, end, c, begin + offset, length - end);
//		if(style != null){
//			c[begin] = ESCAPE;
//			c[begin + 1] = style[0];
//			c[begin + 2] = style[1];
//		}
//		length -= end - begin + offset;
//	}
//	public int length(){
//		return length - styleUpdates * 3;
//	}
//	private void updateStyle(){
//		appendChar(ESCAPE);
//		appendChar(style);
//		appendChar(color);
//		++styleUpdates;
//	}
//	public void setBgFg(byte color){
//		this.color = color;
//		updateStyle();
//	}
//	public void setForeground(byte color){
//		this.color = (byte)(this.color & 0xF0 | color & 0xF);
//		updateStyle();
//	}
//	public void setBackground(byte color){
//		this.color = (byte)(this.color & 0x0F | (color & 0xF) << 4);
//		updateStyle();
//	}
//	public byte getForeground(){
//		return (byte) (color & 0xF);
//	}
//	public byte getBackground(){
//		return (byte) (color >> 4);
//	}
//	public void setStyle(byte style){
//		this.style = style;
//		updateStyle();
//	}
//	public void setBold(boolean bold){
//		style = (byte)(bold ? style | BOLD : style & ~BOLD);
//		updateStyle();
//	}
//	public void setItalic(boolean italic){
//		style = (byte)(italic ? style | ITALIC : style & ~ITALIC);
//		updateStyle();
//	}
//	public void setUnderline(boolean underline){
//		style = (byte)(underline ? style | UNDERLINE : style & ~UNDERLINE);
//		updateStyle();
//	}
//	public void setCrossed(boolean crossed){
//		style = (byte)(crossed ? style | CROSSED : style & ~CROSSED);
//		updateStyle();
//	}
//	public void setConceal(boolean conceal){
//		style = (byte)(conceal ? style | CONCEAL : style & ~CONCEAL);
//		updateStyle();
//	}
//	public static byte getForeground(byte color){
//		return (byte) (color & 0xF);
//	}
//	public static byte getBackground(byte color){
//		return (byte) (color >> 4);
//	}
//	public static boolean getBold(byte style){
//		return (style & BOLD) != 0;
//	}
//	public static boolean getItalic(byte style){
//		return (style & ITALIC) != 0;
//	}
//	public static boolean getUnderline(byte style){
//		return (style & UNDERLINE) != 0;
//	}
//	public static boolean getCrossed(byte style){
//		return (style & CROSSED) != 0;
//	}
//	public static boolean getConceal(byte style){
//		return (style & CONCEAL) != 0;
//	}
//	public String getStringWithFormat(){
//		byte[] f = new byte[length];
//		System.arraycopy(c, 0, f, 0, length);
//		return new String(f);
//	}
//	public static void main(String[] args){
//		FormattedString s = new FormattedString();
//		s.appendString("hello ");
//		s.setForeground(RED);
//		s.appendString("abcdefghijklmnopqrstuvwxyz");
//		s.setBackground(GREEN);
//		s.appendString("---");
//		s.setForeground(BLUE);
//		s.appendString("asdf asdf asdf");
//		//s.removeChars(0, 32);
//		s.removeCharAt(s.length() - 1);
//		System.out.println(s.getStringWithFormat());
//	}
//}
