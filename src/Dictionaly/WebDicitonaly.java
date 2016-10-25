package Dictionaly;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.net.*;

public class WebDicitonaly{
	
	public boolean searchDictionaly(String text){
		try{
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			
			//http://public.dejizo.jp/NetDicV09.asmx/SearchDicItemLite?Dic=EJdict&Word= &Scope=HEADWORD&Match=HEADWORD&Merge=AND&Prof=XHTML&PageSize=20&PageIndex=0
			Document doc = builder.parse("http://public.dejizo.jp/NetDicV09.asmx/SearchDicItemLite?Dic=EJdict&Word="+text+"&Scope=HEADWORD&Match="
					+ "EXACT&Merge=AND&Prof=XHTML&PageSize=20&PageIndex=0");

			Element root = doc.getDocumentElement();
				
			Element TotalHitCount = (Element)root.getElementsByTagName("TotalHitCount").item(0);
		
			String TotalHitCountValue=TotalHitCount.getFirstChild().getNodeValue();
			System.out.println(" "+TotalHitCountValue);
                        System.out.println("http://public.dejizo.jp/NetDicV09.asmx/SearchDicItemLite?Dic=EJdict&Word="+text+"&Scope=HEADWORD&Match="
					+ "EXACT&Merge=AND&Prof=XHTML&PageSize=20&PageIndex=0");
             
              //そのままの単語で良い場合はtrueを返す;
			 if(this.MyDicitonaly(text)){
				 return false;
			}
			 else if(Integer.parseInt(TotalHitCountValue)>= 1){
				return true;
			 }
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public boolean MyDicitonaly(String text){
		
		if(text.equals("a")){
			return true;
		}else if(text.equals("b")){
			return true;
		}else if(text.equals("c")){
			return true;
		}else if(text.equals("d")){
			return true;
		}else if(text.equals("e")){
			return true;
		}else if(text.equals("f")){
			return true;
		}else if(text.equals("g")){
			return true;
		}else if(text.equals("h")){
			return true;
		}else if(text.equals("l")){
			return true;
		}else if(text.equals("m")){
			return true;
		}else if(text.equals("n")){
			return true;
		}else if(text.equals("o")){
			return true;
		}else if(text.equals("p")){
			return true;
		}else if(text.equals("q")){
			return true;
		}else if(text.equals("r")){
			return true;
		}else if(text.equals("s")){
			return true;
		}else if(text.equals("t")){
			return true;
		}else if(text.equals("u")){
			return true;
		}else if(text.equals("v")){
			return true;
		}else if(text.equals("w")){
			return true;
		}else if(text.equals("x")){
			return true;
		}else if(text.equals("y")){
			return true;
		}else if(text.equals("z")){
			return true;
		}else{
			return false;
		}
	}
        
}

