package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.*;
public class UserStory1 {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', false));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 11, 'C', false));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 11, 'C', false));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', false));

    }
    @Test
    public void testValidPlacement(){

    }
}
