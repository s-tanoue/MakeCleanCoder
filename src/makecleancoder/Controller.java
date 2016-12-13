package makecleancoder;

import CommentParser.CleanCoderCommentParser;
import Dictionaly.CommentDictionaly;
import Parser.CleanCoderParser;
import Parser.ParseException;
import ResultData.ResultData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
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
    private TextArea editArea;

    @FXML
    private TextFlow consoleArea;

    @FXML
    private VBox consoleAreaVbox;

    @FXML
    private TextArea lineNumberArea;

    @FXML
    private TextArea parseResultArea;

    @FXML
    private Label fileLabel;

    private String crlf = System.getProperty("line.separator");


    @FXML 
    private void onPreference(ActionEvent event){
    }
    @FXML
    private void onFileOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        fileLabel.setText(selectedFile.getName());
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
            int lineNumber = inputAllString.split(crlf,-1).length; 
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
    private void onFileSave(ActionEvent event) {
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
    private ArrayList<Hyperlink> commentParse(String inputString)
    {
        ResultData result= new ResultData(inputString);
        ArrayList<Hyperlink> outPutLink= new ArrayList<Hyperlink>();

        for(int i = 0; i < result.map.size();  i++) {
            ArrayList<String> comment = result.map.get(result.keyValue.get(i));
            for(int j = 0; j < comment.size(); j++){
                CommentDictionaly dictionaly = new CommentDictionaly();
                //適切なコメントかどうか判断する．
                if (dictionaly.isInappropriateComment(comment.get(j))) {
                    outPutLink.add(new Hyperlink(String.valueOf(result.keyValue.get(i)) + ":" +comment.get(j).replaceAll(crlf, "") + " は不適切なコメントです"+crlf));
                }
            }
        }
        return outPutLink;
    }
    //ファイルのコメントを解析する
    @FXML
    private void executeParseComment(ActionEvent event) {

        String editAreaText = editArea.getText();
        ArrayList<Hyperlink> outPutLink = commentParse(editAreaText);

        VBox vbox = new VBox();
        consoleArea.getChildren().clear();
        for(Hyperlink link : outPutLink){
            vbox.getChildren().add(link);
        }
        consoleArea.getChildren().add(vbox);
    }
    //フォルダー内のコメントを解析
    @FXML
    private void onAnalaysisCommentInFolder(ActionEvent event){
      DirectoryChooser directoryChosser = new DirectoryChooser();
      File selectedFolder = directoryChosser.showDialog(root.getScene().getWindow());
      fileLabel.setText(selectedFolder.getName());
      
    }
    //複数ファイルのコメントを解析
    @FXML
    private void onAnalaysisCommentInMultipleFiles (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
        VBox vbox = new VBox();
        String inputString="";
        ArrayList<Hyperlink> outPutLink = new ArrayList<Hyperlink>();

        consoleArea.getChildren().clear();
        try {
            for(File file: selectedFile){
              //選択された一つ目のファイルを開く
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str;
                inputString = "";
                while ((str = br.readLine()) != null) {
                    inputString += str + crlf;
                }
                //コメントを解析した結果は，hyperlinkのlistで返ってくる．
                outPutLink.clear();
                outPutLink = commentParse(inputString);
                //コメント解析した結果のリストの0番目にファイル名を追加
                outPutLink.add(0, new Hyperlink("ファイル名:"+file.getPath()));
                //改行を入れるために，
                outPutLink.add(new Hyperlink());
                for(Hyperlink link : outPutLink){
                    //ファイルを開いてテキストエディタにセットする機能の追加
                    link.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            BufferedReader br;
                            try {
                                br = new BufferedReader(new FileReader(file));
                                String temp="";
                                String str=new String();
                                fileLabel.setText(file.getName());
                                while ((str = br.readLine()) != null) {
                                    temp+= str + crlf;
                                    editArea.setText(temp);
                                }
                                //行番号を表示．ここからメソッドにする．
                                int lineNumber = temp.split(crlf,-1).length; 
                                String line = "";
                                for (int i = 1; i <= lineNumber; i++) {
                                    line += String.valueOf(i) + crlf;
                                }
                                lineNumberArea.setText(line);
                            } catch (FileNotFoundException e1) {
                                // TODO 自動生成された catch ブロック
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                // TODO 自動生成された catch ブロック
                                e1.printStackTrace();
                            }

                        }
                    });
                    vbox.getChildren().add(link);
                }
            }
            consoleArea.getChildren().add(vbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        parseResultArea.setText(outPutString);

    }

    @FXML
    private void createLineNumber(KeyEvent event) {
        if ("ENTER".equals(event.getCode().toString())) {
            double scrollTop = editArea.getScrollTop();
            int lineNumber = editArea.getText().split(crlf,-1).length; 
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
        consoleArea.getChildren().clear();
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
