package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserStory2Test {

    @Test
    public void testHitResult() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'E', true);
        assertEquals(board.attack(5, 'E' ).getResult(), AtackStatus.HIT);
    }

    @Test
    public void testMissResult() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'E', true);
        assertEquals(board.attack(1, 'A' ).getResult(), AtackStatus.MISS);
    }

    @Test
    public void testSunkResult() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'E', true);
        board.attack(5, 'E' );
        assertEquals(board.attack(6, 'E' ).getResult(), AtackStatus.SUNK);
    }

    @Test
    public void testInvalidResult() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'E', true);
        assertEquals(board.attack(11, 'E' ).getResult(), AtackStatus.INVALID);
    }
}
