package makecleancoder;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private ScrollPane consoleArea;
    @FXML
    private ScrollPane allCommentsArea;

    @FXML
    private VBox consoleAreaVbox;

    @FXML
    private VBox allCommentsAreaVbox;

    @FXML
    private TextArea lineNumberArea;

    @FXML
    private TextArea parseResultArea;

    @FXML
    private Label fileLabel;

    private String crlf = System.getProperty("line.separator");
    private File initialFile = new File(System.getProperty("user.home"));
    private File openedFileOnEditArea;
    //enum 型にする．
    private boolean encoding = true;


    @FXML
    private void handleOnPreference(ActionEvent event){
    }

    @FXML
    private void handleOnUTF8(ActionEvent event){
        encoding = true;
        openFile(openedFileOnEditArea);
    }
    @FXML
    private void handleOnShiftJIS(ActionEvent event){
        encoding = false;
        openFile(openedFileOnEditArea);
    }

    @FXML
    private void handleOnFileOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(initialFile);
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        initialFile = new File(selectedFile.getParent());
        openedFileOnEditArea = new File(selectedFile.getPath());
        fileLabel.setText(selectedFile.getName());
        openFile(selectedFile);
    }

    @FXML
    private void handleOnFileSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());
        try (FileWriter filewriter = new FileWriter(selectedFile)) {
            filewriter.write(editArea.getText());
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    //editAreaの中身から，コメントを解析する．
    @FXML
    private void handleOnExecuteParseComment(ActionEvent event) 
    {
        String editAreaText = editArea.getText();
        ArrayList<Hyperlink> outPutLink = commentParse(editAreaText);
        ArrayList<Hyperlink> outPutLink2 = getAllComment(editAreaText);

        consoleAreaVbox.getChildren().clear();
        TextFlow inappriprateCommentCount= new TextFlow(new Text("不適切な可能性のあるコメントの数:"+String.valueOf(outPutLink.size())));
        consoleAreaVbox.getChildren().add(inappriprateCommentCount);
        for(Hyperlink link : outPutLink){
            link.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    String str[] =link.toString().split(":",0);
                    String str2[] = str[0].split("'",0);
                    int lineNumber = Integer.parseInt(str2[1]);
                    setScrollBar(lineNumber);
                }
            });
            TextFlow textFlow = new TextFlow(link);
            consoleAreaVbox.getChildren().add(textFlow);
        }
        allCommentsAreaVbox.getChildren().clear();

        TextFlow commentCount= new TextFlow(new Text("コメントの数:"+String.valueOf(outPutLink2.size())));
        allCommentsAreaVbox.getChildren().add(commentCount);
        for(Hyperlink link : outPutLink2){
            link.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    String str[] =link.toString().split(":",0);
                    String str2[] = str[0].split("'",0);
                    int lineNumber = Integer.parseInt(str2[1]);
                    setScrollBar(lineNumber);
                }
            });
            TextFlow textFlow = new TextFlow(link);
            allCommentsAreaVbox.getChildren().add(textFlow);
        }
    }
    //フォルダー内のコメントを解析
    @FXML
    private void handleOnAnalaysisCommentInFolder(ActionEvent event){
        DirectoryChooser directoryChosser = new DirectoryChooser();
        File selectedFolder = directoryChosser.showDialog(root.getScene().getWindow());
        fileLabel.setText(selectedFolder.getName());
    }
    //複数ファイルのコメントを解析
    @FXML
    private void handleOnAnalaysisCommentInMultipleFiles (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(initialFile);
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
        initialFile = new File(selectedFile.get(0).getParent());
        openedFileOnEditArea = new File(selectedFile.get(0).getPath());
        String inputString="";
        ArrayList<Hyperlink> outPutLinkList = new ArrayList<Hyperlink>();
        ArrayList<Hyperlink> outPutLinkList2 = new ArrayList<Hyperlink>();

        consoleAreaVbox.getChildren().clear();
        allCommentsAreaVbox.getChildren().clear();
        try {
            for(File file: selectedFile){
                //選択された一つ目のファイルを開く
                BufferedReader br = encoding ? new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8")) : new BufferedReader(new InputStreamReader(new FileInputStream(file),"SJIS"));
                String str;
                inputString = "";
                while ((str = br.readLine()) != null) {
                    inputString += str + crlf;
                }

                String filePath = file.getPath();

                //コメントを解析した結果は，hyperlinkのlistで返ってくる．
                outPutLinkList.clear();
                outPutLinkList = commentParse(inputString);
                Text inappropriateCommentCount = new Text("不適切な可能性のあるコメントの数:"+String.valueOf(outPutLinkList.size()));
                TextFlow textFlow = new TextFlow(new Text("ファイル名"+file.getPath()+"  "),inappropriateCommentCount);
                consoleAreaVbox.getChildren().add(textFlow);
                outPutLinkList.add(new Hyperlink());

                for(Hyperlink link : outPutLinkList){
                    //ファイルを開いてテキストエディタにセットする機能の追加
                    link.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            openFile(file);
                            String str[] =link.toString().split(":",0);
                            String str2[] = str[0].split("'",0);
                            int lineNumber = Integer.parseInt(str2[1]);
                            setScrollBar(lineNumber);
                        }
                    });
                    consoleAreaVbox.getChildren().add(link);
                }
                outPutLinkList2 = getAllComment(inputString);
                Text commentCount = new Text("コメントの数:"+String.valueOf(outPutLinkList2.size()));
                TextFlow textFlow2 = new TextFlow(new Text("ファイル名"+file.getPath()+"  "),commentCount);
                allCommentsAreaVbox.getChildren().add(textFlow2);
                // //改行を入れるために，
                outPutLinkList2.add(new Hyperlink());

                for(Hyperlink link : outPutLinkList2){
                    //ファイルを開いてテキストエディタにセットする機能の追加
                    link.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            openFile(file);

                            String str[] =link.toString().split(":",0);
                            String str2[] = str[0].split("'",0);
                            int lineNumber = Integer.parseInt(str2[1]);

                            setScrollBar(lineNumber);
                        }
                    });
                    allCommentsAreaVbox.getChildren().add(link);
                }
            }
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
        consoleAreaVbox.getChildren().clear();
    }

    @FXML
    private void clearEditArea(ActionEvent event) {
        editArea.setText("");
        lineNumberArea.setText("1"+crlf);
    }

    private void setScrollBar(int lineNumber){
        double value = 20;
        if(lineNumber == 1){
            editArea.setScrollTop(0);
        }else{
            editArea.setScrollTop((lineNumber-1)*value);
        }

    }

    //指定されたパスに存在するファイルを開く．
    private void openFile(File file){
        try {
            String inputAllString;
            try(BufferedReader br = encoding ? new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8")) : new BufferedReader(new InputStreamReader(new FileInputStream(file),"SJIS"))){
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
                    outPutLink.add(new Hyperlink(String.valueOf(result.keyValue.get(i)) + ":" +comment.get(j).replaceAll(crlf, "") + " は不適切な可能性があります"+crlf));
                }
            }
        }
        return outPutLink;
    }
    // 引数　ソースコード
    // 戻り値 ソースコードに存在するすべてのコメント
    private ArrayList<Hyperlink> getAllComment(String inputString)
    {
        ResultData result = new ResultData(inputString);
        ArrayList<Hyperlink> outPutLink= new ArrayList<Hyperlink>();

        for(int i = 0; i < result.map.size();  i++) {
            ArrayList<String> comment = result.map.get(result.keyValue.get(i));
            for(int j = 0; j < comment.size(); j++){
                outPutLink.add(new Hyperlink(String.valueOf(result.keyValue.get(i)) + ":" +comment.get(j).replaceAll(crlf, "")));
            }
        }
        return outPutLink;
    }
    //編集エリアのコメントを解析する
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editArea.scrollTopProperty().bindBidirectional(lineNumberArea.scrollTopProperty());
    }

}

