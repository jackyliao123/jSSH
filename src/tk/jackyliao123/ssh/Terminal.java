package tk.jackyliao123.ssh;

public class Terminal {
	public final SSH ssh;
	public int cursorX;
	public int cursorY;
	public boolean customScrollRegion;
	public int scrollRegionMin;
	public int scrollRegionMax;
	public boolean endOfLine;
	public boolean wrapAround = true;
	public boolean rWrapAround = false;
	public boolean reverseVideo;
	
	public int consoleWidth = 80;
	public int consoleHeight = 24;
	
	public long[][] buffer = new long[consoleHeight][consoleWidth];
	
	public boolean cursor = true;
	
	public byte fg = Style.DEFAULT_FG;
	public byte bg = Style.DEFAULT_BG;
	public short style = Style.PLAIN;
	
	public CommandListener command;
	
	public Terminal(SSH ssh){
		this.ssh = ssh;
	}
	public void checkCursorBounds(){
		if(cursorX >= consoleWidth){
			cursorX = consoleWidth - 1;
		}
		else if(cursorX < 0){
			cursorX = 0;
		}
		if(cursorY >= consoleHeight){
			cursorY = consoleHeight - 1;
		}
		else if(cursorY < 0){
			cursorY = 0;
		}
		checkSize(cursorX, cursorY);
	}
	
	public void checkSize(int posX, int posY){
		if(posY >= buffer.length){
			long[][] temp = buffer;
			buffer = new long[posY + 1][consoleWidth];
			System.arraycopy(temp, 0, buffer, 0, temp.length);
		}
		if(posX >= buffer[posY].length){
			long[] temp = buffer[posY];
			buffer[posY] = new long[posX + 1];
			System.arraycopy(temp, 0, buffer[posY], 0, temp.length);
		}
	}
	public void curUp(int n){
		if(customScrollRegion && cursorY >= scrollRegionMin){
			cursorY -= n;
			if(cursorY < scrollRegionMin){
				cursorY = scrollRegionMin;
			}
		}
		else{
			cursorY -= n;
		}
	}
	public void curDown(int n){
		if(customScrollRegion && cursorY < scrollRegionMax){
			cursorY += n;
			if(cursorY >= scrollRegionMax){
				cursorY = scrollRegionMax - 1;
			}
		}
		else{
			cursorY += n;
		}
	}
	public void curUpScroll(int n){
		if(customScrollRegion && cursorY >= scrollRegionMin){
			cursorY -= n;
			if(cursorY < scrollRegionMin){
				scrollUp(scrollRegionMin - cursorY);
				cursorY = scrollRegionMin;
			}
		}
		else{
			cursorY -= n;
			if(cursorY < 0){
				scrollUp(-cursorY);
			}
		}
	}
	public void curDownScroll(int n){
		if(customScrollRegion && cursorY < scrollRegionMax){
			cursorY += n;
			if(cursorY >= scrollRegionMax){
				scrollDown(cursorY - scrollRegionMax + 1);
				cursorY = scrollRegionMax - 1;
			}
		}
		else{
			cursorY += n;
			if(cursorY >= consoleHeight){
				scrollDown(cursorY - consoleHeight + 1);
			}
		}
	}
	public void clearlAfter(){
		checkCursorBounds();
		long[] b = buffer[cursorY];
		for(int i = cursorX; i < b.length; i ++){
			b[i] = 0;
		}
	}
	public void clearlBefore(){
		checkCursorBounds();
		long[] b = buffer[cursorY];
		for(int i = 0; i <= cursorX; i ++){
			b[i] = 0;
		}
	}
	public void clearlAll(){
		checkCursorBounds();
		buffer[cursorY] = new long[consoleWidth];
	}
	public void clearAfter(){
		clearlAfter();
		for(int i = cursorY + 1; i < buffer.length; i ++){
			buffer[i] = new long[consoleWidth];
		}
	}
	public void clearBefore(){
		clearlBefore();
		for(int i = 0; i < cursorY; i ++){
			buffer[i] = new long[consoleWidth];
		}
	}
	public void clearAll(){
		buffer = new long[consoleHeight][consoleWidth];
	}
	
	public void scrollUp(int n){
		if(!customScrollRegion){
			scrollRegionMin = 0;
			scrollRegionMax = consoleHeight;
		}
		if(scrollRegionMax - scrollRegionMin > n){
			System.arraycopy(buffer, scrollRegionMin, buffer, scrollRegionMin + n, scrollRegionMax - scrollRegionMin - n);
		}
		for(int i = scrollRegionMin; i < scrollRegionMin + n; i ++){
			buffer[i] = new long[consoleWidth];
		}
	}
	public void scrollDown(int n){
		if(!customScrollRegion){
			scrollRegionMin = 0;
			scrollRegionMax = consoleHeight;
		}
		if(scrollRegionMax - scrollRegionMin > n){
			System.arraycopy(buffer, scrollRegionMin + n, buffer, scrollRegionMin, scrollRegionMax - scrollRegionMin - n);
		}
		for(int i = scrollRegionMax - n; i < scrollRegionMax; i ++){
			buffer[i] = new long[consoleWidth];
		}
	}
	
	public void insertBlank(int chars){
		checkCursorBounds();
		chars = Math.min(chars, consoleWidth - cursorX);
		long[] b = buffer[cursorY];
		System.arraycopy(b, cursorX, b, cursorX + chars, consoleWidth - cursorX - chars);
		for(int i = cursorX; i < cursorX + chars; i ++){
			b[i] = 0;
		}
	}
	public void deleteChars(int chars) {
		checkCursorBounds();
		chars = Math.min(chars, consoleWidth - cursorX);
		long[] b = buffer[cursorY];
		System.arraycopy(b, cursorX + chars, b, cursorX, consoleWidth - cursorX - chars);
		for(int i = consoleWidth - chars; i < consoleWidth; i ++){
			b[i] = 0;
		}
	}
	public void insertRow(int n){
		checkCursorBounds();
		if(!customScrollRegion){
			scrollRegionMin = 0;
			scrollRegionMax = consoleHeight;
		}
		if(cursorY >= scrollRegionMin && cursorY < scrollRegionMax){
			if(scrollRegionMax - scrollRegionMin - n > 0){
				System.arraycopy(buffer, cursorY, buffer, cursorY + n, scrollRegionMax - scrollRegionMin - n);
			}
			for(int i = cursorY; i < cursorY + n; i ++){
				buffer[i] = new long[consoleWidth];
			}
		}
	}
	public void deleteRow(int n){
		checkCursorBounds();
		if(!customScrollRegion){
			scrollRegionMin = 0;
			scrollRegionMax = consoleHeight;
		}
		if(cursorY >= scrollRegionMin && cursorY < scrollRegionMax){
			System.arraycopy(buffer, cursorY + n, buffer, cursorY, scrollRegionMax - scrollRegionMin - n);
			for(int i = scrollRegionMax - n - 1; i < scrollRegionMax; i ++){
				buffer[i] = new long[consoleWidth];
			}
		}
	}
}
