package GetTextAreaNumber;

import javafx.scene.control.TextArea;

public class CleanCoderTextArea extends TextArea{
	
	public CleanCoderTextArea(){
		super();
	}

	public CleanCoderTextArea(String text){
		super();
		this.setText(text);
	}
	public int getLineNumber(String str){
		String strs[] = str.split("\n",-1);
		return strs.length;
	}
	
}
