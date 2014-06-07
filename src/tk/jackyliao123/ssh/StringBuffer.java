package tk.jackyliao123.ssh;

public class StringBuffer{
	private byte[] chars;
	private int length;
	public StringBuffer(int charSize){
		chars = new byte[charSize];
	}
	public StringBuffer(){ 
		chars = new byte[16];
	}
	public static byte toLowercase(byte c){
		if('A' <= c && 'Z' >= c){
			return (byte)(c + 32);
		}
		return c;
	}
	private void checkSize(){
		if(length + 1 > chars.length){
			byte[] temp = chars;
			chars = new byte[chars.length * 2 + 1];
			System.arraycopy(temp, 0, chars, 0, temp.length);
		}
	}
	public void append(byte b){
		checkSize();
		chars[length] = b;
		length ++;
	}
	public int length(){
		return length;
	}
	public StringBuffer[] split(){
		int count = 0;
		for(int i = 0; i < length; i ++){
			if(chars[i] == ';'){
				count ++;
			}
		}
		StringBuffer[] result = new StringBuffer[count + 1];
		int lastIndex = -1;
		int currIndex = 0;
		for(int i = 0; i < count; i ++){
			currIndex = indexOf(lastIndex + 1);
			result[i] = subString(lastIndex + 1, currIndex);
			lastIndex = currIndex;
		}
		result[count] = subString(lastIndex + 1, length);
		return result;
	}
	private int indexOf(int start){
		if(start < 0){
			start = 0;
		}
		for(int i = start; i < length; i ++){
			if(chars[i] == ';'){
				return i;
			}
		}
		return -1;
	}
	public StringBuffer subString(int begin, int end){
		StringBuffer sb = new StringBuffer(end - begin);
		sb.length = end - begin;
		System.arraycopy(chars, begin, sb.chars, 0, end - begin);
		return sb;
	}
	public StringBuffer subString(int begin){
		StringBuffer sb = new StringBuffer(length - begin);
		sb.length = length - begin;
		System.arraycopy(chars, begin, sb.chars, 0, length - begin);
		return sb;
	}
	public String toString(){
		return new String(chars, 0, length);
	}
	public int toInt(int def){
		if(length == 0){
			return def;
		}
		return Integer.parseInt(toString());
	}
	public byte charAt(int i){
		return chars[i];
	}
}
