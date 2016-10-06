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
        assertEquals(false, dictionaly.isRequiredComment("TODO"));
	}

}
