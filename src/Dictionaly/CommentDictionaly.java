package Dictionaly;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CommentDictionaly {

  //データ構造の作成
  private ArrayList<String> wordList = new ArrayList<String>();
  private HashMap<String,ArrayList<String>>  wordMap = new HashMap<String,ArrayList<String>>();

  public void setDicitonaly() { 
    //xml の読みこみ
    Document document = null;
    try {
      document = DocumentBuilderFactory.newInstance()
          .newDocumentBuilder()
          .parse("src/Dictionaly/comment_word.xml");
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
   
    Node rootNode = (Node) (document.getDocumentElement());
    Node commentNode = rootNode.getFirstChild();
    while (commentNode != null) {
      if (!commentNode.getNodeName().equals("comment")) {
        commentNode = commentNode.getNextSibling();
        continue;
      }
      Node wordNode = commentNode.getFirstChild();
      while (wordNode != null) {
        if (wordNode.getNodeName().equals("word")) {  
          Node node = wordNode.getFirstChild(); 
          if (node != null) {
            wordList.add(node.getNodeValue());           
          } 
        }
        //nullじゃないとき，ハッシュマップに，コメントの属性値とワードのリストをセットする．
        if(wordList != null && commentNode.getAttributes().getNamedItem("type") != null){
          wordMap.put(commentNode.getAttributes().getNamedItem("type").getNodeValue(),wordList);
        }   
        wordNode = wordNode.getNextSibling();
      }
      commentNode = commentNode.getNextSibling();
    }
  }

  public boolean isRequiredComment(String text) {
    text.replaceAll("\\n", "");
    setDicitonaly();
    for(String w: wordList){
      if(text.matches(".*"+w+".*")){
        return false;
      }
    }
    return true;
  }
}
