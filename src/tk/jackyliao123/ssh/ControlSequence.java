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
	public void processControlSequence() throws IOException{
		int i = ssh.read();
		switch(i){
		case '[':
			processCSI();
			break;
		case ']':
			processOSC();
			break;
		}
		
	}
	public void processOSC() throws IOException{
		StringBuffer escape = new StringBuffer();
		while(true){
			int i = ssh.read();
			if((i == 27 && ssh.read() == '\\') || i == 7){
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
	public void processCSI() throws IOException{
		StringBuffer escape = new StringBuffer();
		while(true){
			int i = ssh.read();
			if((i >= 'A' && i <= 'Z') || (i >= 'a' && i <= 'z')){
				StringBuffer[] s = escape.split();
				switch (i){
				case 'A':
					terminal.cursorY -= escape.toInt(1);
					break;
				case 'B':
					terminal.cursorY += escape.toInt(1);
					break;
				case 'C':
					terminal.cursorX += escape.toInt(1);
					break;
				case 'D':
					terminal.cursorX -= escape.toInt(1);
					break;
				case 'E':
					terminal.cursorY += escape.toInt(1);
					terminal.cursorX = 0;
					break;
				case 'F':
					terminal.cursorY -= escape.toInt(1);
					terminal.cursorY = 0;
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
				case 'S':
					terminal.scroll += escape.toInt(1);
					break;
				case 'T':
					terminal.scroll -= escape.toInt(1);
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
				case 'h':
					
					break;
				case 'l':
					
					break;
				case 'm':
					processSGR(s);
					break;
				}
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
				if(value >= 30 && value <= 37){
					terminal.fg = (byte)(value - 30);
				}
				else if(value >= 40 && value <= 47){
					terminal.bg = (byte)(value - 40);
				}
				break;
			}
		}
	}
}
