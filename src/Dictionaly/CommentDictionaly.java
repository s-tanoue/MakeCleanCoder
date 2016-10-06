package Dictionaly;

public class CommentDictionaly {

	public boolean searchDicitonaly(String text){
		return true;
	}
	
	public boolean isRequiredComment(String text){
			
			if(text.equals("修正")){
				return false;
			}else if(text.equals("TODO")){
				return false;
			}else if(text.equals("作成")){
				return false;
			}else if(text.equals("サポート")){
				return false;
			}else if(text.equals("変更")){
				return false;
			}else if(text.equals("よく分からないけど動く")){
				return false;
			}else if(text.equals("何故か動く")){
				return false;
			}else if(text.equals("なぜか動く")){
				return false;
			}else if(text.equals("なぜ動くのかわからない")){
				return false;
			}else{
				return true;
			}
	}
	
}
