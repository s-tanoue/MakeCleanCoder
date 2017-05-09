package Dictionary;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class CommentDictionary {

    //例: key:TODOコメント Value:TODO,明日までにこれをやる！，ここは修正すべき．
    private HashMap<String,ArrayList<String>>  regularExpressionMap = new HashMap<String,ArrayList<String>>();

    //データ構造の作成
    public CommentDictionary() {
        //xml の読みこみ
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse("src/Dictionary/comment_word.xml");
        } catch (SAXException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        //commentsを取得
        Node rootNode = (Node) (document.getDocumentElement());
        //commentの最初のnodeを取得
        Node commentNode = rootNode.getFirstChild();
        while (commentNode != null) {
            ArrayList<String> regularExpressionList = new ArrayList<String>();
            if (!commentNode.getNodeName().equals("comment")) {
                commentNode = commentNode.getNextSibling();
                continue;
            }
            Node regularExpressionNode = commentNode.getFirstChild();
            while (regularExpressionNode != null) {
                if(!regularExpressionNode.getNodeName().equals("regularExpression") ){
                    regularExpressionNode = regularExpressionNode.getNextSibling();
                    continue;
                }
                Node node = regularExpressionNode.getFirstChild();
                if (node != null){
                    regularExpressionList.add(node.getNodeValue());
                }
                //nullじゃないとき，ハッシュマップに，コメントの属性値とワードのリストをセットする．
                if(commentNode.getAttributes().getNamedItem("type") != null){
                    regularExpressionMap.put(commentNode.getAttributes().getNamedItem("type").getNodeValue(),regularExpressionList);
                }
                regularExpressionNode = regularExpressionNode.getNextSibling();
            }
            commentNode = commentNode.getNextSibling();
        }
    }
    //TODO 大文字の区別をつけないオプションを付けるの
    //TODO 過去形現在系の区別をつけないオプションを付ける
    //不適切なコメントであったらtrue
    public boolean isInappropriateComment(String target) {
      if(isRegularExpression(target)){
              return true;
        }
        return false;
    }
    //正規表現に一致したらtrue
    public boolean isRegularExpression(String target){
        for (String key : regularExpressionMap.keySet()) {
            for(String r: regularExpressionMap.get(key)){
                Pattern p = Pattern.compile(r);
                Matcher m = p.matcher(target);
                if(m.find()){
                    return true;
                }
            }
        }
        return false;
    }

}
