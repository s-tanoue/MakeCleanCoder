package Dictionaly;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommentDictionalyTest {

	@Test
	public void testSearchDicitonaly() {
	}

	@Test
	public void testIsRequiredComment() {
        CommentDictionaly dictionaly = new CommentDictionaly();

        assertEquals(false, dictionaly.isInappropriateComment("//aaaaa"));
        assertEquals(true, dictionaly.isWord("/* 2016/11/12 修正*/"));
        assertEquals(true, dictionaly.isRegularExppression("/* 2016/11/12 */"));
        assertEquals(true, dictionaly.isInappropriateComment("//2016/11/12 TODO"));
	}

}
