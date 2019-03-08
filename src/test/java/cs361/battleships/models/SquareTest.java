package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class SquareTest {

    @Test
    public void testIsOutOfBoundTest() {
        Square square = new Square(11, 'A');
        assertTrue(square.isOutOfBounds());

        square = new Square(1, 'Z');
        assertTrue(square.isOutOfBounds());

        square = new Square(1, 'a');
        assertTrue(square.isOutOfBounds());

        square = new Square(0, 'A');
        assertTrue(square.isOutOfBounds());
    }

    @Test
    public void testIsHit() {
        Square square = new Square(1, 'A');
        assertFalse(square.isHit());

        square.hit();
        assertTrue(square.isHit());
    }

    @Test
    public void testEquals() {
        Square square1 = new Square(1, 'A');
        Square square2 = new Square(1, 'A');

        assertTrue(square1.equals(square2));
        assertEquals(square1.hashCode(), square2.hashCode());
    }

    @Test
    public void testNotEquals() {
        Square square1 = new Square(1, 'A');
        Square square2 = new Square(2, 'A');

        assertFalse(square1.equals(square2));
        assertNotEquals(square1.hashCode(), square2.hashCode());
    }
    @Test
    public void testCaptainExists(){

        Square square1 = new Square(1, 'A');
        square1.setCaptain();
        assertTrue(square1.getIsCaptain());
    }

    @Test
    public void testMoveOneSquareCorrectDirection(){
        Square s = new Square(5, 'E');
        s.move('U');
        assertTrue(s.getRow() == 4);
        s.move('D');
        assertTrue(s.getRow() == 5);
        s.move('L');
        assertTrue(s.getColumn() == 'D');
        s.move('R');
        assertTrue(s.getColumn() == 'E');
    }
}
