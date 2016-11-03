package ResultData;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;


public class ResultDataTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void test() {
    ResultData result = new ResultData("//aaa\na\n//aaa\n");
    
    BidiMap <Integer,String> actual = new BidiMap<Integer,String>();
    BidiMap <Integer,String> expected = new BidiMap<Integer,String>();
    expected.put(1,"//aaa");
    expected.put(3, "//aa");
    actual=result.map;
    System.out.println(expected.getKey("//aaa"));
    
    HashMap <String,ArrayList<Integer>> m =  new HashMap <String,ArrayList<Integer>>();
    ArrayList<Integer> data = new ArrayList<>();
    data.add(1);
    m.put("//aa", data);
    data.clear();
    data.add(2);
    data.clear();
    
    m.put("//bb", data);
    
    data = m.get("//bb");
    data.add(3);
    m.put("//aa", data);
   
    data.clear();
    data = m.get("//aa");
    for(int i=0; i<data.size();i++){
      System.out.println(data.get(i));
    }
  }

}
