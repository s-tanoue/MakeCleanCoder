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
        assertEquals(false, dictionaly.isRequiredComment("//サポート"));
        assertEquals(false, dictionaly.isRequiredComment("//なぜかうごく"));
        assertEquals(false, dictionaly.isRequiredComment("//よくわからないけど動く"));
        assertEquals(false, dictionaly.isRequiredComment("//TODO"));
        assertEquals(false, dictionaly.isRequiredComment("/*TODO*/"));
        assertEquals(false, dictionaly.isRequiredComment("/TODOnnan/"));
        assertEquals(false, dictionaly.isRequiredComment("/*よくわからないけど動く*/"));
	}

}
