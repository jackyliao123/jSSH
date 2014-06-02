package tk.jackyliao123.ssh;

import java.util.ArrayList;

public class Terminal {
	public final SSH ssh;
	public int cursorX;
	public int cursorY;
	
	public int consoleWidth = 80;
	public int consoleHeight = 24;
	
	public boolean cursor = true;
	public int scroll = 0;
	
	public byte fg = Style.DEFAULT_FG;
	public byte bg = Style.DEFAULT_BG;
	public byte style = Style.PLAIN;
	
	ArrayList<byte[]> buffer = new ArrayList<byte[]>();
	ArrayList<int[]> styles = new ArrayList<int[]>();
	
	public CommandListener command;
	
	public Terminal(SSH ssh){
		this.ssh = ssh;
	}
	public void clearlAfter(){
		checkCursorBounds();
		byte[] buf = buffer.get(cursorY);
		for(int i = cursorX; i < consoleWidth; i ++){
			buf[i] = 0;
		}
	}
	public void clearlBefore(){
		checkCursorBounds();
		if(cursorX >= consoleWidth)
			cursorX = consoleWidth - 1;
		byte[] buf = buffer.get(cursorY);
		for(int i = 0; i <= cursorX; i ++){
			buf[i] = 0;
		}
	}
	public void clearlAll(){
		checkCursorBounds();
		byte[] buf = buffer.get(cursorY);
		for(int i = 0; i < consoleWidth; i ++){
			buf[i] = 0;
		}
	}
	public void clearAfter(){
		clearlAfter();
		while(buffer.size() > cursorY + 1){
			buffer.remove(buffer.size() - 1);
			styles.remove(styles.size() - 1);
		}
	}
	public void clearBefore(){
		clearlBefore();
		for(int i = 0; i < cursorY; i ++){
			byte[] b = buffer.get(i);
			for(int j = 0; j < consoleWidth; j ++){
				b[j] = 0;
			}
		}
	}
	public void clearAll(){
		while(buffer.size() > 0){
			buffer.remove(buffer.size() - 1);
		}
		cursorX = 0;
		cursorY = 0;
	}
	public void updateScroll(){
		if(cursorY >= consoleHeight){
			scroll = cursorY - consoleHeight + 1;
		}
		else if(cursorY < scroll){
			scroll = cursorY;
		}
	}
	public void checkCursorBounds(){
		if(cursorX >= consoleWidth){
			cursorX = consoleWidth - 1;
		}
		while(cursorX < 0){
			cursorX = 0;
		}
		if(cursorY < 0){
			cursorY = 0;
			cursorX = 0;
		}
		while(cursorY >= buffer.size()){
			buffer.add(new byte[consoleWidth]);
			styles.add(new int[consoleWidth]);
		}
	}
	public void insertBlank(int chars){
		chars = Math.min(chars, consoleWidth - cursorX);
		checkCursorBounds();
		byte[] b = buffer.get(cursorY);
		System.arraycopy(b, cursorX, b, cursorX + chars, consoleWidth - cursorX - chars);
		for(int i = cursorX; i < cursorX + chars; i ++){
			b[i] = 0;
		}
	}
	public void deleteChars(int chars) {
		checkCursorBounds();
		chars = Math.min(chars, consoleWidth - cursorX);
		byte[] b = buffer.get(cursorY);
		int[] format = styles.get(cursorY);
		System.arraycopy(b, cursorX + chars, b, cursorX, consoleWidth - cursorX - chars);
		System.arraycopy(format, cursorX + chars, format, cursorX, consoleWidth - cursorX - chars);
	}
}
