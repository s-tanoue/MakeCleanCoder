/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MakeCleanCoder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author SatoshiTanoue
 */
public class MakeCleanCoder extends Application {

    public static Stage supportingRegexStage;

    @Override
    public void start(Stage mainStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));   
        Scene scene = new Scene(root);
        String mainFxmlFileName = "view.fxml";
        String mainTitle = "";
        String codeFxmlFileName = "SupportingRegexView.fxml";
        String codeTitle = "SupportingRegex";

        mainStage = decorateStage( mainStage, mainFxmlFileName, mainTitle );
        supportingRegexStage = makeAdditionalStage( mainStage, codeFxmlFileName, codeTitle );
        mainStage.show();
    }

    public static void showSupportingRegexStage(){
        supportingRegexStage.show();
    }
     /**
     * 追加ウィンドウのステージを作る。
     *
     * @param ownerStage 土台となるステージ：土台ステージを消すと追加ステージも消える。
     * @param fxmlFileName 入力FXMLファイル名：{@code null}不可
     * @param title ウィンドウタイトル
     * @return 追加するステージ
     * @throws IOException decorateStageメソッド参照
     */
    public Stage makeAdditionalStage( Stage ownerStage, String fxmlFileName, String title ) throws IOException {
        Stage addStage = new Stage();
        addStage.initOwner( ownerStage );
        addStage.setScene( new Scene( new BorderPane() ) );

        addStage = decorateStage( addStage, fxmlFileName, title );

        return addStage;
    }
    /**
     * 表示するステージを装飾する。
     *
     * @param stage 表示するステージ：特にメインウィンドウを表示する際は{@code null}不可
     * @param fxmlFileName 入力FXMLファイル名：{@code null}不可
     * @param title ウィンドウタイトル
     * @return 装飾されたステージ
     * @throws IOException FXMLファイルの入力エラーの場合
     */
    private Stage decorateStage( Stage stage, String fxmlFileName, String title ) throws IOException {
        Parent root = FXMLLoader.load( getClass().getResource( fxmlFileName ) );
        stage.setTitle( title );
        stage.setScene( new Scene( root ) );

        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
