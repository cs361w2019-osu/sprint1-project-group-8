package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShipTest {

    @Test
    public void testPlaceMinesweeperHorizontaly() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, false, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceMinesweeperVertically() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceDestroyerHorizontaly() {
        Ship minesweeper = new Ship("DESTROYER");
        minesweeper.place('A', 1, false, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        expected.add(new Square(1, 'C'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceDestroyerVertically() {
        Ship minesweeper = new Ship("DESTROYER");
        minesweeper.place('A', 1, true, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        expected.add(new Square(3, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceBattleshipHorizontaly() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, false, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        expected.add(new Square(1, 'C'));
        expected.add(new Square(1, 'D'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceBattleshipVertically() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        expected.add(new Square(3, 'A'));
        expected.add(new Square(4, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceSubmarineHorizontal() {
        Ship sub = new Ship("SUBMARINE");
        sub.place('A', 3, false, false);
        List<Square> occupiedSquares = sub.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(3, 'A'));
        expected.add(new Square(3, 'B'));
        expected.add(new Square(3, 'C'));
        expected.add(new Square(3, 'D'));
        expected.add(new Square(2, 'C'));
        assertEquals(expected, occupiedSquares);
    }
    @Test
    public void testPlaceSubmarineHorizontalCount() {
        Ship sub = new Ship("SUBMARINE");
        sub.place('A', 3, false, false);
        List<Square> occupiedSquares = sub.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(3, 'A'));
        expected.add(new Square(3, 'B'));
        expected.add(new Square(3, 'C'));
        expected.add(new Square(3, 'D'));
        expected.add(new Square(2, 'C'));
        assertEquals(occupiedSquares.size(), 5);
        assertEquals(expected, occupiedSquares);
    }
    @Test
    public void testPlaceSubmarineCaptainQuartersHorizontal() {
        Ship sub = new Ship("SUBMARINE");
        sub.place('A', 3, false, false);
        List<Square> occupiedSquares = sub.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(3, 'A'));
        expected.add(new Square(3, 'B'));
        expected.add(new Square(3, 'C'));
        expected.add(new Square(3, 'D'));
        expected.add(new Square(2, 'C'));
        assertEquals(expected, occupiedSquares);
        assertTrue(occupiedSquares.get(occupiedSquares.size()-2).getIsCaptain());
        assertEquals(occupiedSquares.get(occupiedSquares.size()-2).getColumn(), 'D');
    }
    @Test
    public void testPlaceSubmarineCaptainQuartersVertical() {
        Ship sub = new Ship("SUBMARINE");
        sub.place('B', 3, true, false);
        List<Square> occupiedSquares = sub.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(3, 'B'));
        expected.add(new Square(4, 'B'));
        expected.add(new Square(5, 'B'));
        expected.add(new Square(6, 'B'));
        expected.add(new Square(4, 'A'));
        assertEquals(expected, occupiedSquares);
        assertTrue(occupiedSquares.get(occupiedSquares.size()-2).getIsCaptain());

    }
    @Test
    public void testPlaceSubmarineSubmergedHorizontal() {
        Ship sub = new Ship("SUBMARINE");
        sub.place('A', 3, false, true);
        List<Square> occupiedSquares = sub.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(3, 'A'));
        expected.add(new Square(3, 'B'));
        expected.add(new Square(3, 'C'));
        expected.add(new Square(3, 'D'));
        expected.add(new Square(2, 'C'));
        assertEquals(expected, occupiedSquares);
        for(int i = 0; i < occupiedSquares.size(); i++){
            assertTrue(occupiedSquares.get(i).getIsSubmerged());

        }


    }
    @Test
    public void testPlaceSubmarineSubmergedVertical() {
        Ship sub = new Ship("SUBMARINE");
        sub.place('B', 3, true, true);
        List<Square> occupiedSquares = sub.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(3, 'B'));
        expected.add(new Square(4, 'B'));
        expected.add(new Square(5, 'B'));
        expected.add(new Square(6, 'B'));
        expected.add(new Square(4, 'A'));
        assertEquals(expected, occupiedSquares);
        for(int i = 0; i < occupiedSquares.size(); i++){
            assertTrue(occupiedSquares.get(i).getIsSubmerged());

        }

    }
    @Test
    public void testPlaceSubmarineVertical() {
        Ship sub = new Ship("SUBMARINE");
        sub.place('B', 3, true, false);
        List<Square> occupiedSquares = sub.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(3, 'B'));
        expected.add(new Square(4, 'B'));
        expected.add(new Square(5, 'B'));
        expected.add(new Square(6, 'B'));
        expected.add(new Square(4, 'A'));
        assertEquals(expected, occupiedSquares);
    }
    @Test
    public void testShipOverlaps() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true, false);

        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('A', 1, true, false);

        assertTrue(minesweeper1.overlaps(minesweeper2));
    }

    @Test
    public void testShipsDontOverlap() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true, false);

        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('C', 2, true, false);

        assertFalse(minesweeper1.overlaps(minesweeper2));
    }

    @Test
    public void testIsAtLocation() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true, false);

        assertTrue(minesweeper.isAtLocation(new Square(1, 'A')));
        assertTrue(minesweeper.isAtLocation(new Square(2, 'A')));
    }

    @Test
    public void testHit() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true, false);

        Result result = minesweeper.attack(1, 'A');
        assertEquals(AtackStatus.HIT, result.getResult());
        assertEquals(minesweeper, result.getShip());
        assertEquals(new Square(1, 'A'), result.getLocation());
    }

    @Test
    public void testSink() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true, false);

        var result = minesweeper.attack(1, 'A');

        assertEquals(AtackStatus.SUNK, result.getResult());

    }

    @Test
    public void testOverlapsBug() {
        Ship minesweeper = new Ship("MINESWEEPER");
        Ship destroyer = new Ship("DESTROYER");
        minesweeper.place('C', 5, false, false);
        destroyer.place('C', 5, false, false);
        assertTrue(minesweeper.overlaps(destroyer));
    }
    @Test
    public void testBlockedHit(){

        Ship destroyer = new Ship("DESTROYER");
        destroyer.place('A', 1, true, false);
        var result = destroyer.attack(2, 'A');
        assertEquals(AtackStatus.BLOCKED, result.getResult());
    }
    @Test
    public void testBlockedHitSunk(){

        Ship destroyer = new Ship("DESTROYER");
        destroyer.place('A', 1, true, false);
        var result = destroyer.attack(2, 'A');
        assertEquals(AtackStatus.BLOCKED, result.getResult());
        result = destroyer.attack(2, 'A');
        assertEquals(AtackStatus.SUNK, result.getResult());
    }
    @Test
    public void testAttackSameSquareTwice() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true, false);
        var result = minesweeper.attack(1, 'A');
        assertEquals(AtackStatus.SUNK, result.getResult());
        result = minesweeper.attack(1, 'A');
        assertEquals(AtackStatus.INVALID, result.getResult());
    }

    @Test
    public void testEquals() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true, false);
        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('A', 1, true, false);
        assertTrue(minesweeper1.equals(minesweeper2));
        assertEquals(minesweeper1.hashCode(), minesweeper2.hashCode());
    }

    @Test
    public void testMoveOneSquareCorrectDirection() {
        Ship s = new Ship("MINESWEEPER");
        s.place('E', 5, true, false);

        s.move('U');
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 4);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'E');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 5);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'E');

        s.move('D');
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 5);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'E');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 6);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'E');

        s.move('L');
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 5);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'D');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 6);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'D');

        s.move('R');
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 5);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'E');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 6);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'E');
    }

    @Test
    public void testDoNotMoveOverEdge() {
        Ship s = new Ship("MINESWEEPER");
        s.place('A', 1, true, false);
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 1);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'A');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 2);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'A');
        s.move('U');
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 1);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'A');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 2);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'A');

        s = new Ship("MINESWEEPER");
        s.place('A', 9, true, false);
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 9);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'A');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 10);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'A');
        s.move('D');
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 9);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'A');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 10);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'A');

        s = new Ship("MINESWEEPER");
        s.place('A', 1, true, false);
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 1);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'A');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 2);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'A');
        s.move('L');
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 1);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'A');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 2);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'A');

        s = new Ship("MINESWEEPER");
        s.place('J', 1, true, false);
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 1);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'J');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 2);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'J');
        s.move('R');
        assertEquals(s.getOccupiedSquares().get(0).getRow(), 1);
        assertEquals(s.getOccupiedSquares().get(0).getColumn(), 'J');
        assertEquals(s.getOccupiedSquares().get(1).getRow(), 2);
        assertEquals(s.getOccupiedSquares().get(1).getColumn(), 'J');
    }

    @Test
    public void testNoMoveOverlap() {
        Ship s1 = new Ship("MINESWEEPER");
        Ship s2 = new Ship("MINESWEEPER");

        s1.place('A', 1, true, false);
        s2.place('B', 1, true, false);
        assertTrue(s2.checkMoveOverlap(s1, 'L'));
        assertFalse(s2.checkMoveOverlap(s1, 'R'));
        assertFalse(s2.checkMoveOverlap(s1, 'U'));
        assertFalse(s2.checkMoveOverlap(s1, 'D'));

        s1 = new Ship("MINESWEEPER");
        s2 = new Ship("MINESWEEPER");

        s1.place('A', 10, false, false);
        s2.place('A', 9, false, false);
        assertFalse(s2.checkMoveOverlap(s1, 'L'));
        assertFalse(s2.checkMoveOverlap(s1, 'R'));
        assertFalse(s2.checkMoveOverlap(s1, 'U'));
        assertTrue(s2.checkMoveOverlap(s1, 'D'));
    }
}
