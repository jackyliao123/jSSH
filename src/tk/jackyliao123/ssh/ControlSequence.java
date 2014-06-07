package tk.jackyliao123.ssh;

import java.io.IOException;

public class ControlSequence {
	public final SSH ssh;
	public final Terminal terminal;
	
	public int saveCursorX;
	public int saveCursorY;
	
	public ControlSequence(Terminal terminal){
		this.terminal = terminal;
		ssh = terminal.ssh;
	}
	public void processControlSequence(DataReaderThread reader) throws IOException{
		int i = reader.read();
		switch(i){
		case '[':
			processCSI(reader);
			break;
		case ']':
			processOSC(reader);
			break;
		case ' ':
			reader.read();
			break;
		case '#':
			if(reader.read() == '8'){
				terminal.checkSize(terminal.consoleWidth - 1, terminal.consoleHeight - 1);
				for(int y = 0; y < terminal.consoleHeight; y ++){
					terminal.checkSize(terminal.consoleWidth - 1, y);
					long[] b = terminal.buffer[y];
					for(int x = 0; x < terminal.consoleWidth; x ++){
						b[x] = Style.encodeFormat((byte)'E', Style.DEFAULT_STYLE, Style.DEFAULT_FG, Style.DEFAULT_BG);
					}
				}
			}
			break;
		case '%':
			reader.read();
			break;
		case '(':
			reader.read();
			break;
		case ')':
			reader.read();
			break;
		case '*':
			reader.read();
			break;
		case '+':
			reader.read();
			break;
		case '7':
			saveCursorX = terminal.cursorX;
			saveCursorY = terminal.cursorY;
			break;
		case '8':
			terminal.cursorX = saveCursorX;
			terminal.cursorY = saveCursorY;
			break;
		case 'E':
			terminal.cursorY ++;
			terminal.cursorX = 0;
			break;
		case 'D':
			terminal.curDownScroll(1);
			break;
		case 'M':
			terminal.curUpScroll(1);
			break;
//		case '=':
//		case '>':
//		case 'F':
//		case 'c':
//		case 'l':
//		case 'm':
//		case 'n':
//		case 'o':
//		case '|':
//		case '}':
//		case '~':
//		case 'H':
//		case 'N':
//		case 'O':
//		case 'P':
//		case 'V':
//		case 'W':
//		case 'X':
//		case 'Z':
//		case '\\':
//		case '^':
//		case '_':
		default:
			System.err.println((char)i);
		}
	}
	public void processOSC(DataReaderThread reader) throws IOException{
		StringBuffer escape = new StringBuffer();
		while(true){
			int i = reader.read();
			if((i == 27 && reader.read() == '\\') || i == 7){
				StringBuffer[] s = escape.split();
				if(s.length == 2){
					int v = s[0].toInt(0);
					if(v == 0 || v == 1 || v == 2){
						terminal.command.setWindowTitle(s[1].toString());
					}
				}
				break;
			}
			else{
				escape.append((byte)i);
			}
		}
	}
	public void processCSI(DataReaderThread reader) throws IOException{
		StringBuffer escape = new StringBuffer();
		while(true){
			int i = reader.read();
			if((i >= 'A' && i <= 'Z') || (i >= 'a' && i <= 'z') || i == '@' || i == '{' || i == '|'){
				StringBuffer[] s = escape.split();
				terminal.endOfLine = false;
				switch (i){
				case '@':
					terminal.insertBlank(escape.toInt(1));
					break;
				case 'A':
					terminal.curUp(Math.max(1, escape.toInt(1)));
					break;
				case 'B':
					terminal.curDown(Math.max(1, escape.toInt(1)));
					break;
				case 'C':
					terminal.cursorX += Math.max(1, escape.toInt(1));
					break;
				case 'D':
					terminal.cursorX -= Math.max(1, escape.toInt(1));
					break;
				case 'E':
					terminal.curDown(escape.toInt(1));
					terminal.cursorX = 0;
					break;
				case 'F':
					terminal.curUp(escape.toInt(1));
					terminal.cursorX = 0;
					break;
				case 'G':
					terminal.cursorX = escape.toInt(1) - 1;
					break;
				case 'H':
					if(s.length == 2){
						terminal.cursorY = s[0].toInt(1) - 1;
						terminal.cursorX = s[1].toInt(1) - 1;
					}
					else{
						terminal.cursorY = s[0].toInt(1) - 1;
					}
					break;
				case 'J':
					switch(escape.toInt(0)){
					case 0:
						terminal.clearAfter();
						break;
					case 1:
						terminal.clearBefore();
						break;
					case 2:
						terminal.clearAll();
					}
					break;
				case 'K':
					switch(escape.toInt(0)){
					case 0:
						terminal.clearlAfter();
						break;
					case 1:
						terminal.clearlBefore();
						break;
					case 2:
						terminal.clearlAll();
					}
					break;
				case 'L':
					terminal.insertRow(s[0].toInt(1));
					break;
				case 'M':
					terminal.deleteRow(s[0].toInt(1));
					break;
				case 'S':
					terminal.scrollDown(s[0].toInt(1));
					break;
				case 'T':
					terminal.scrollUp(s[0].toInt(1));
					break;
				case 'f':
					if(s.length >= 2){
						terminal.cursorY = s[0].toInt(1) - 1;
						terminal.cursorX = s[1].toInt(1) - 1;
					}
					else{
						terminal.cursorY = s[0].toInt(1) - 1;
					}
					break;
				case 'r':
					if(s.length >= 2){
						terminal.customScrollRegion = true;
						terminal.scrollRegionMin = s[0].toInt(1) - 1;
						terminal.scrollRegionMax = s[1].toInt(terminal.consoleHeight);
						if(terminal.scrollRegionMin < 0){
							terminal.scrollRegionMin = 0;
						}
						if(terminal.scrollRegionMax > terminal.consoleHeight){
							terminal.scrollRegionMax = terminal.consoleHeight;
						}
						if(terminal.scrollRegionMin == 0 && terminal.scrollRegionMax == terminal.consoleHeight){
							terminal.customScrollRegion = false;
						}
					}
					else{
						terminal.customScrollRegion = false;
					}
					terminal.cursorX = 0;
					terminal.cursorY = 0;
					break;
				case 'd':
					terminal.cursorY = s[0].toInt(1) - 1;
					break;
				case 'P':
					terminal.deleteChars(s[0].toInt(1));
					break;
				case 'n':
					ssh.output.write(27);
					ssh.output.write('[');
					ssh.output.write(("" + (terminal.cursorY + 1)).toString().getBytes());
					ssh.output.write(';');
					ssh.output.write(("" + (terminal.cursorX + 1)).toString().getBytes());
					ssh.output.write('R');
					break;
				case 's':
					saveCursorX = terminal.cursorX;
					saveCursorY = terminal.cursorY;
					break;
				case 'u':
					terminal.cursorX = saveCursorX;
					terminal.cursorY = saveCursorY;
					break;
				case 'l':
					if(s[0].charAt(0) == '?'){
						int n = s[0].subString(1).toInt(0);
						switch (n){
						case 3:
							terminal.command.setWindowWidth(80);
							break;
						case 5:
							terminal.reverseVideo = false;
							break;
						case 25:
							terminal.cursor = false;
							break;
						}
					}
					break;
				case 'h':
					if(s[0].charAt(0) == '?'){
						int n = s[0].subString(1).toInt(0);
						switch (n){
						case 3:
							terminal.command.setWindowWidth(132);
							break;
						case 5:
							terminal.reverseVideo = true;
							break;
						case 25:
							terminal.cursor = true;
							break;
						default:
							System.err.println(n);
							break;
						}
					}
					break;
				case 'm':
					processSGR(s);
					break;
				case 'c':
					ssh.sendEscapeString("[?c");
					break;
				default:
					System.err.println("UNRECONIZED: ESC[" + escape + (char)i);
					break;
				}
				terminal.checkCursorBounds();
				if(terminal.command != null){
					terminal.command.repaint();
				}
				return;
			}
			else{
				escape.append((byte)i);
			}
		}
	}
	public void processSGR(StringBuffer[] s){
		for(StringBuffer code : s){
			int value = code.toInt(0);
			switch(value){
			case 0:
				terminal.style = Style.DEFAULT_STYLE;
				terminal.bg = Style.DEFAULT_BG;
				terminal.fg = Style.DEFAULT_FG;
				break;
			case 1:
				terminal.style = Style.setBold(true, terminal.style);
				break;
			case 3:
				terminal.style = Style.setItalic(true, terminal.style);
				break;
			case 4:
				terminal.style = Style.setUnderline(true, terminal.style);
				break;
			case 7:
				terminal.style = Style.setNegative(true, terminal.style);
				break;
			case 8:
				terminal.style = Style.setConceal(true, terminal.style);
				break;
			case 9:
				terminal.style = Style.setCrossed(true, terminal.style);
				break;
			case 21:
				terminal.style = Style.setBold(false, terminal.style);
				break;
			case 23:
				terminal.style = Style.setItalic(false, terminal.style);
				break;
			case 24:
				terminal.style = Style.setUnderline(false, terminal.style);
				break;
			case 27:
				terminal.style = Style.setNegative(false, terminal.style);
				break;
			case 28:
				terminal.style = Style.setConceal(false, terminal.style);
				break;
			case 29:
				terminal.style = Style.setCrossed(false, terminal.style);
				break;
			case 39:
				terminal.fg = Style.DEFAULT_FG;
				break;
			case 49:
				terminal.bg = Style.DEFAULT_BG;
			default:
				boolean intense = Style.getBold(terminal.style);
				terminal.style = Style.setUsePalette(false, terminal.style);
				if(value >= 30 && value <= 37){
					terminal.fg = (byte)(value - 30 + (intense ? 8 : 0));
				}
				else if(value >= 40 && value <= 47){
					terminal.bg = (byte)(value - 40 + (intense ? 8 : 0));
				}
				break;
			}
		}
	}
}
