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
    ResultData result = new ResultData("//aaa\na\n/*aaa*//*aaaa*/\n");
   
    ArrayList<String> commentString = new ArrayList<String>();
    commentString=result.map.get(1);
    System.out.println(commentString.get(0));
  }

}
