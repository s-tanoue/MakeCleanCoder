package ResultData;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import CommentParser.CleanCoderCommentParser;

public class ResultData {


	public ArrayList<String> comment = new ArrayList<>();
	public HashMap <Integer,ArrayList<String>> map = new HashMap<>();
	public ArrayList<Integer> keyValue = new ArrayList<Integer>();
	
	//HashMapにコメントと行番号をhashmapにセットする．
	public ResultData(String inputString){
		int lineNumber = 0;
		String inputStrings[] = inputString.split("\n", -1);
		CleanCoderCommentParser parser = new CleanCoderCommentParser(new StringReader(inputString));
		try {
			comment = parser.comment();
			for (int i = 0; i < comment.size(); i++) {
				if (!comment.get(i).isEmpty()) {
					String comments[] = comment.get(i).split("\n", -1);
					for (int j = 0; j < inputStrings.length; j++) {
						if(inputStrings[j].matches(".*"+Pattern.quote(comments[0])+".*")){
							lineNumber = j + 1;
							ArrayList<String> resultComment = new ArrayList<>();
							if(map.get(lineNumber) != null){
								resultComment = map.get(lineNumber);
							}
							resultComment.add(comment.get(i));
							keyValue.add(lineNumber);
							map.put(lineNumber, resultComment);
							inputStrings[j] = "";
							break;
						}
					}
				}
			}
		} catch (CommentParser.ParseException e) {
		  System.out.println("コメントがありません");
			e.printStackTrace();
		}

	}
}

