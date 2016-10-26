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

        assertEquals(true, dictionaly.isRequiredComment("//aaaaa"));
        assertEquals(false, dictionaly.isRequiredComment("//TODO\n"));
        assertEquals(false, dictionaly.isRequiredComment("//TODO"));
        assertEquals(false, dictionaly.isRequiredComment("//将来"));
        assertEquals(false, dictionaly.isRequiredComment("//予定"));
        assertEquals(false, dictionaly.isRequiredComment("//未来"));
        assertEquals(false, dictionaly.isRequiredComment("/*対応*/"));
        assertEquals(false, dictionaly.isRequiredComment("/*今後*/"));
	}

}
