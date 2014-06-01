package tk.jackyliao123.ssh;

import java.awt.Toolkit;
import java.io.EOFException;
import java.io.IOException;

public class DataReaderThread extends Thread{
	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Terminal terminal;
	public SSH ssh;
	public ControlSequence control;
	public DataReaderThread(Terminal terminal){
		this.terminal = terminal;
		ssh = terminal.ssh;
		control = new ControlSequence(terminal);
	}
	public void run(){
		try{
			int i = 0;
			while(true){
				i = ssh.read();
				if(i == 27){
					control.processControlSequence();
					continue;
				}
				if(i == '\n'){
					++terminal.cursorY;
					terminal.updateScroll();
					terminal.cursorX = 0;
				}
				else if(i == '\r'){
					terminal.cursorX = 0;
				}
				else if(i == 7){
					toolkit.beep();
				}
				else if(i == 8){
					--terminal.cursorX;
				}
				else{
					terminal.checkCursorBounds();
					terminal.updateScroll();
					terminal.buffer.get(terminal.cursorY)[terminal.cursorX] = (byte)i;
					terminal.styles.get(terminal.cursorY)[terminal.cursorX] = Style.encodeFormat(terminal.style, terminal.fg, terminal.bg);
					++terminal.cursorX;
				}
				if(terminal.command != null){
					terminal.command.repaint();
				}
			}
		}
		catch(EOFException e){
			System.exit(0);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}