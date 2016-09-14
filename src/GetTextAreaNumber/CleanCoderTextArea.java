package GetTextAreaNumber;

import javafx.scene.control.TextArea;

public class CleanCoderTextArea extends TextArea{

	private boolean pausedScroll = false;
	private double scrollPosition = 0;
	
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
