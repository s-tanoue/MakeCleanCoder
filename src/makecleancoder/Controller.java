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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    //encodingがtrueのときはutf-8になる
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
        ArrayList<String> improperCommentStringList = commentParse(editAreaText);
        exportResultToFile(improperCommentStringList, openedFileOnEditArea.getName());
        ArrayList<Hyperlink> improperCommentLinkList =toHyperLinkList(improperCommentStringList);
        ArrayList<String> allCommentStringList = getAllComment(editAreaText);
        ArrayList<Hyperlink> allCommentLinkList =toHyperLinkList(allCommentStringList);


        consoleAreaVbox.getChildren().clear();
        TextFlow improperCommentCount= new TextFlow(new Text("検出したコメントの数:"+String.valueOf(improperCommentLinkList.size())));
        consoleAreaVbox.getChildren().add(improperCommentCount);
        for(Hyperlink link : improperCommentLinkList){
            link.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    int lineNumber = getLineNumberFromLink(link);
                    setScrollBar(lineNumber);
                }
            });
            TextFlow textFlow = new TextFlow(link);
            consoleAreaVbox.getChildren().add(textFlow);
        }
        allCommentsAreaVbox.getChildren().clear();

        TextFlow commentCount= new TextFlow(new Text("コメントの数:"+String.valueOf(allCommentLinkList.size())));
        allCommentsAreaVbox.getChildren().add(commentCount);
        for(Hyperlink link : allCommentLinkList){
            link.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    int lineNumber = getLineNumberFromLink(link);
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
        String inputString="";
        ArrayList<Hyperlink> inproperCommentLinkList = new ArrayList<>();
        ArrayList<String> inproperCommentStringList = new ArrayList<>();
        ArrayList<String> allCommentStringList = new ArrayList<>();
        ArrayList<Hyperlink> allCommentLinkList = new ArrayList<>();

        consoleAreaVbox.getChildren().clear();
        allCommentsAreaVbox.getChildren().clear();
        try {
            for(File file: selectedFile){
                //選択された一つ目のファイルを開く
                openFile(file);
                BufferedReader br = encoding ? new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8")) : new BufferedReader(new InputStreamReader(new FileInputStream(file),"SJIS"));
                String str;
                inputString = "";
                while ((str = br.readLine()) != null) {
                    inputString += str + crlf;
                }
                String filePath = file.getPath();

                //コメントを解析した結果は，hyperlinkのlistで返ってくる．
                inproperCommentLinkList.clear();
                inproperCommentStringList = commentParse(inputString);
                exportResultToFile(inproperCommentStringList, file.getName());
                inproperCommentLinkList = toHyperLinkList(inproperCommentStringList);

                Text inproperCommentCount = new Text("検出したコメントの数:"+String.valueOf(inproperCommentLinkList.size()));
                TextFlow textFlow = new TextFlow(new Text("ファイル名"+file.getPath()+"  "),inproperCommentCount);
                consoleAreaVbox.getChildren().add(textFlow);
                inproperCommentLinkList.add(new Hyperlink());

                for(Hyperlink link : inproperCommentLinkList){
                    //ファイルを開いてテキストエディタにセットする機能の追加
                    link.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            openFile(file);
                            int lineNumber = getLineNumberFromLink(link);
                            setScrollBar(lineNumber);
                        }
                    });
                    consoleAreaVbox.getChildren().add(link);
                }
                allCommentStringList = getAllComment(inputString);
                allCommentLinkList = toHyperLinkList(allCommentStringList);


                Text commentCount = new Text("コメントの数:"+String.valueOf(allCommentLinkList.size()));
                TextFlow textFlow2 = new TextFlow(new Text("ファイル名"+file.getPath()+"  "),commentCount);
                allCommentsAreaVbox.getChildren().add(textFlow2);
                // //改行を入れるために，
                allCommentLinkList.add(new Hyperlink());

                for(Hyperlink link : allCommentLinkList){
                    //ファイルを開いてテキストエディタにセットする機能の追加
                    link.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            openFile(file);

                            int lineNumber = getLineNumberFromLink(link);

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

    private int getLineNumberFromLink(Hyperlink link) {
        String str[] =link.toString().split(":",0);
        String str2[] = str[0].split("'",0);
        return Integer.parseInt(str2[1]);
    }

    //変数名を解析
    @FXML
    private void executeVariableParser(ActionEvent event) {
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

    //指定されたパスに存在するファイルを開き，テキストエリアに出力する．
    private void openFile(File file){
        openedFileOnEditArea = new File(file.getPath());
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
    private ArrayList<String> commentParse(String inputString)
    {
        ResultData result= new ResultData(inputString);
        ArrayList<String> outPutList = new ArrayList<String>();

        for(int i = 0; i < result.comment.size();  i++) {
            ArrayList<String> comment = result.map.get(result.keyValue.get(i));
            for(int j = 0; j < comment.size(); j++){
                CommentDictionaly dictionaly = new CommentDictionaly();
                //適切なコメントかどうか判断する．
                if (dictionaly.isInappropriateComment(comment.get(j))) {
                    outPutList.add(String.valueOf(result.keyValue.get(i)) + ":" +comment.get(j).replaceAll(crlf, "") + " は不適切な可能性があります");
                }
            }
        }
        return outPutList;
    }
    //StringをHyperLinkのListに変更する．
    private ArrayList<Hyperlink> toHyperLinkList(List<String> stringList)
    {
        ArrayList<Hyperlink> outPutList = new ArrayList<>();
        for(String str:stringList){
            outPutList.add(new Hyperlink(str));
        }
        return outPutList;
    }

    // 引数　ソースコード
    // 戻り値 ソースコードに存在するすべてのコメント
    private ArrayList<String> getAllComment(String inputString)
    {
        ResultData result = new ResultData(inputString);
        ArrayList<String> outPutLink= new ArrayList<String>();

        for(int i = 0; i < result.map.size();  i++) {
            ArrayList<String> comment = result.map.get(result.keyValue.get(i));
            for(int j = 0; j < comment.size(); j++){
                outPutLink.add(String.valueOf(result.keyValue.get(i)) + ":" +comment.get(j).replaceAll(crlf, ""));
            }
        }
        return outPutLink;
    }

    //listは解析結果
    //fileNameはファイル名
    //result_filename.txtに解析結果を出力する．
    private void exportResultToFile(List<String> list,String fileName)
    {

        //TODO:文字コードによって，出力する文字コードを変更する．
        Calendar c = Calendar.getInstance();
        //フォーマットパターンを指定して表示する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String exportString = sdf.format(c.getTime())+crlf;
        exportString += "検出したコメントの数:"+String.valueOf(list.size())+crlf;
        for(String str:list){
            exportString+=str+crlf;
        }
        //出力先ファイルのFileオブジェクトを作成
        File file = new File("src/Result/"+"result_"+fileName+".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bw = encoding? new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),"UTF-8")) : new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),"SJIS"));
            bw.write(exportString);
            bw.newLine();
            bw.close();
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //編集エリアのコメントを解析する
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editArea.scrollTopProperty().bindBidirectional(lineNumberArea.scrollTopProperty());
    }

}

