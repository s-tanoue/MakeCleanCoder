package makecleancoder;

import CommentParser.CleanCoderCommentParser;
import Dictionaly.CommentDictionaly;
import GetTextAreaNumber.CleanCoderTextArea;
import Parser.CleanCoderParser;
import Parser.ParseException;
import ResultData.ResultData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author SatoshiTanoue
 * @version 1.0
 */
public class Controller implements Initializable {

  @FXML
  private AnchorPane root;
  @FXML
  private CleanCoderTextArea editArea;
  @FXML
  private CleanCoderTextArea consoleArea;
  @FXML
  private CleanCoderTextArea lineNumberArea;
  private String crlf = System.getProperty("line.separator");
  
  @FXML
  private void fileOpen(ActionEvent event) {
    
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
    try {
      String inputAllString;
      try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
        String str;
        inputAllString = "";
        while ((str = br.readLine()) != null) {
          inputAllString += str + crlf;
        }
      }
      editArea.setText(inputAllString);
      int lineNumber = editArea.getLineNumber(editArea.getText());
      String line = "";
      for (int i = 1; i <= lineNumber; i++) {
        line += String.valueOf(i) + crlf;
      }
      lineNumberArea.setText(line);
    } catch (FileNotFoundException e) {
      
    } catch (IOException e) {
    }

  }

  @FXML
  private void fileSave(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save File");
    File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());

    try (FileWriter filewriter = new FileWriter(selectedFile)) {
      filewriter.write(editArea.getText());
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // 戻り値 コンソールエリアに出力する文字列
  // 引数 解析するソースコード
  private String commentParse(String inputString)
  {
	  String outPutString = "";
	  ResultData result= new ResultData(inputString);

	  for(int i = 0; i < result.map.size();  i++) {
		  ArrayList<String> comment = result.map.get(result.keyValue.get(i));
		  for(int j = 0; j < comment.size(); j++){
			  CommentDictionaly dictionaly = new CommentDictionaly();
			  //適切なコメントかどうか判断する．
			  if (!dictionaly.isRequiredComment(comment.get(j))) {
				  outPutString += String.valueOf(result.keyValue.get(i)) + ":"
						  +comment.get(j).replaceAll(crlf, "") + " は不要なコメントです\n";
			  }
		  }
	  }
	  return outPutString;
  }
  @FXML
  //複数ファイルのコメントを解析するコメント
  private void executeParseComment(ActionEvent event) {

	  String editAreaText = editArea.getText();
	  String  outPutString = commentParse(editAreaText);
	  consoleArea.setText(outPutString);

  }

  //複数ファイルのコメントを解析
  @FXML
  private void executeParseMultipleFileOfComment(ActionEvent event) {

	  FileChooser fileChooser = new FileChooser();
	  fileChooser.setTitle("Open Resource File");
	  List<File> selectedFile = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
	  String inputString="";
	  String outputString="";
	  BufferedReader br;
	  try {
		  for(File file: selectedFile){
			  br = new BufferedReader(new FileReader(file));
			  String str;
			  while ((str = br.readLine()) != null) {
				  inputString += str + crlf;
			  }
			 outputString += "ファイル名:"+file.getName()+"\n"+commentParse(inputString)+crlf;
		  }
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  consoleArea.setText(outputString);

  }

  //変数名を解析
  @FXML
  private void executeParser(ActionEvent event) {
	  String str = editArea.getText();
	  String[] strs = str.split(crlf);
	  String outPutString = "";
	  List<String> result = new ArrayList<String>();
	  int count = 1;
	  for (int i = 0; i < strs.length; i++) {
		  try {
			  CleanCoderParser parser = new CleanCoderParser(new StringReader(strs[i]));
			  result = parser.VariableDeclaration();
			  for (int j = 0; j < result.size(); j++) {
				  if (!result.get(j).isEmpty()) {
					  outPutString += String.valueOf(count) + ":" + result.get(j) + crlf;
				  }
			  }
		  } catch (ParseException e) {
			  e.printStackTrace();
		  }
		  count++;
	  }
	  consoleArea.setText(outPutString);

  }

  @FXML
  private void createLineNumber(KeyEvent event) {
	  if ("ENTER".equals(event.getCode().toString())) {
		  double scrollTop = editArea.getScrollTop();
		  String string = editArea.getText();
		  int lineNumber = editArea.getLineNumber(string);
		  String line = "";
		  for (int i = 1; i <= lineNumber + 1; i++) {
			  line += String.valueOf(i) + crlf;
		  }
		  lineNumberArea.setText(line);
		  editArea.setScrollTop(scrollTop);
	  }
  }

  @FXML
  private void clearConsoleArea(ActionEvent event) {
	  consoleArea.setText("");
  }

  @FXML
  private void clearEditArea(ActionEvent event) {
	  editArea.setText("");
	  lineNumberArea.setText("1"+crlf);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
	  editArea.scrollTopProperty().bindBidirectional(lineNumberArea.scrollTopProperty());
  }

}
