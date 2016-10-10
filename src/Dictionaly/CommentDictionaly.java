package Dictionaly;

public class CommentDictionaly {

	public boolean searchDicitonaly(String text){
		return true;
	}
	
	public boolean isRequiredComment(String text){
			
			if(text.matches(".*" + "TODO" + ".*")){
				return false;
			}else if(text.matches(".*" + "修正" + ".*")){
				return false;
			}else if(text.matches(".*" + "サポート" + ".*")){
				return false;
			}else if(text.matches(".*" + "追加" + ".*")){
				return false;
			}else if(text.matches(".*" + "変更" + ".*")){
				return false;
			}else if(text.matches(".*" + "よくわからないけど動く" + ".*")){
				return false;
			}else if(text.matches(".*" + "何故か動く" + ".*")){
				return false;
			}else if(text.matches(".*" + "なぜかうごく" + ".*")){
				return false;
			}else if(text.matches(".*" + "なぜ動くのかわからない" + ".*")){
				return false;
			}else{
				return true;
			}
	}
	
}
