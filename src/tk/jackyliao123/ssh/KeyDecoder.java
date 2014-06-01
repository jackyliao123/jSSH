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
				sendEscapeString(output, "Ot");
				break;
			case KeyEvent.VK_F6:
				sendEscapeString(output, "Ou");
				break;
			case KeyEvent.VK_F7:
				sendEscapeString(output, "Ov");
				break;
			case KeyEvent.VK_F8:
				sendEscapeString(output, "OI");
				break;
			case KeyEvent.VK_F9:
				sendEscapeString(output, "Ow");
				break;
			case KeyEvent.VK_F10:
				sendEscapeString(output, "Ox");
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
