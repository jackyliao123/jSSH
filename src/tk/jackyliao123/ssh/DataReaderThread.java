package tk.jackyliao123.ssh;

import java.awt.Toolkit;
import java.io.EOFException;
import java.io.IOException;

import javax.swing.JOptionPane;

public class DataReaderThread extends Thread{
	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Terminal terminal;
	public SSH ssh;
	public ControlSequence control;
	public DataReaderThread(Terminal terminal){
		setName("DataReaderThread");
		this.terminal = terminal;
		ssh = terminal.ssh;
		control = new ControlSequence(terminal);
	}
	public int read() throws IOException{
		int i;
		while(true){
			i = ssh.read();
			if(i == 9){
				++terminal.cursorX;
			}
			else if(i == '\n'){
				terminal.cursorX = 0;
				if(terminal.customScrollRegion && terminal.cursorY == terminal.scrollRegionMax - 1){
					terminal.scrollDown(1);
				}
				else{
					++terminal.cursorY;
				}
			}
			else if(i == 11){
				++terminal.cursorY;
			}
			else if(i == '\r'){
				terminal.cursorX = 0;
			}
			else if(i == 8){
				terminal.checkCursorBounds();
				--terminal.cursorX;
			}
			else if(i < 32 && i != 27 && i != 7){
				System.out.println(i);
			}
			else{
				return i;
			}
			terminal.command.repaint();
		}
	}
	public void run(){
		try{
			int i = 0;
			while(true){
				i = read();
				if(i == 27){
					control.processControlSequence(this);
					terminal.updateScroll();
					continue;
				}
				else if(i == 7){
					toolkit.beep();
				}
				else{
					terminal.checkCursorBounds();
					if(terminal.cursorX >= terminal.consoleWidth - 1 && !terminal.endOfLine){
						terminal.endOfLine = true;
					}
					else if(terminal.endOfLine){
						terminal.cursorX = 0;
						if(terminal.customScrollRegion && terminal.cursorY == terminal.scrollRegionMax - 1){
							terminal.scrollDown(1);
						}
						else{
							++terminal.cursorY;
						}
						terminal.endOfLine = false;
					}
					terminal.buffer[terminal.cursorY][terminal.cursorX] = Style.encodeFormat((byte)i, terminal.style, terminal.fg, terminal.bg);
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
			JOptionPane.showMessageDialog(null, "Error", "Connection Error: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}