package tk.jackyliao123.ssh;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;

public class KeyDecoder {
	public static void sendKeyStroke(OutputStream output, KeyEvent e) throws IOException{
		switch(e.getKeyCode()){
		case KeyEvent.VK_CONTROL:
			break;
		case KeyEvent.VK_SHIFT:
			break;
		case KeyEvent.VK_ALT:
			break;
		case KeyEvent.VK_CAPS_LOCK:
			break;
		case KeyEvent.VK_ENTER:
			output.write(0x0d);
			output.flush();
			break;
		case KeyEvent.VK_BACK_SPACE:
			output.write(0x7f);
			output.flush();
			break;
		case KeyEvent.VK_ESCAPE:
			output.write(0x1b);
			output.flush();
			break;
		case KeyEvent.VK_DELETE:
			sendEscapeString(output, "[3~");
			break;
		case KeyEvent.VK_UP:
			sendEscapeString(output, "OA");
			break;
		case KeyEvent.VK_DOWN:
			sendEscapeString(output, "OB");
			break;
		case KeyEvent.VK_RIGHT:
			sendEscapeString(output, "OC");
			break;
		case KeyEvent.VK_LEFT:
			sendEscapeString(output, "OD");
			break;
		case KeyEvent.VK_F1:
			sendEscapeString(output, "OP");
			break;
		case KeyEvent.VK_F2:
			sendEscapeString(output, "OQ");
			break;
		case KeyEvent.VK_F3:
			sendEscapeString(output, "OR");
			break;
		case KeyEvent.VK_F4:
			sendEscapeString(output, "OS");
			break;
		case KeyEvent.VK_F5:
			sendEscapeString(output, "[15~");
			break;
		case KeyEvent.VK_F6:
			sendEscapeString(output, "[17~");
			break;
		case KeyEvent.VK_F7:
			sendEscapeString(output, "[18~");
			break;
		case KeyEvent.VK_F8:
			sendEscapeString(output, "[19~");
			break;
		case KeyEvent.VK_F9:
			sendEscapeString(output, "[20~");
			break;
		case KeyEvent.VK_F10:
			sendEscapeString(output, "[21~");
			break;
		case KeyEvent.VK_F11:
			sendEscapeString(output, "[23~");
			break;
		case KeyEvent.VK_F12:
			sendEscapeString(output, "[24~");
			break;
		default:
			output.write(e.getKeyChar());
			output.flush();
			break;
		}
	}
	private static void sendEscapeString(OutputStream output, String s) throws IOException{
		output.write(27);
		output.write(s.getBytes());
		output.flush();
	}
}
