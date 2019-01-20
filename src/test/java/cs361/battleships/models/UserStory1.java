package cs361.battleships.models;

import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
public class UserStory1 {

    @Test
    public void testOutsidePlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', false));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 11, 'C', false));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 11, 'C', false));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', false));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("DESTROYER"), -1, 'D', true));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), -1, 'F', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'E', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 10, 'C', false));
        assertFalse(board.placeShip(new Ship("DESTROYER"), -1, 'C', false));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), -1, 'C', false));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 5, 'Z', false));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 9, 'C', true));
    }
    @Test
    public void testValidPlacement(){
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 5, 'D', true));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 6, 'E', false));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 7, 'E', true));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 5, 'E', false));
    }
    @Test
    public void testDiagonal(){
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 5, 'D', true));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 6, 'E', false));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 7, 'E', true));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 5, 'E', false));
    }
    @Test
    public void testMineSweeper(){
        Ship ship = new Ship("MINESWEEPER");
        assertEquals(2, ship.getSize());
    }
    @Test
    public void testDestroyer(){
        Ship ship = new Ship("DESTROYER");
        assertEquals(3, ship.getSize());
    }
    @Test
    public void testBattleship(){
        Ship ship = new Ship("BATTLESHIP");
        assertEquals(4, ship.getSize());
    }
    @Test
    public void testPlaceShip(){
        Board board = new Board();
        Ship ship = new Ship("BATTLESHIP");
        assertTrue(board.placeShip(ship, 5, 'D', true));
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        assertEquals(board.getShips(), ships);
        assertTrue(board.placeShip(ship, 3, 'A', true));
        ships.add(ship);
        assertEquals(board.getShips(), ships);
    }
    @Test
    public void testOverlap(){
        Board board = new Board();
        Ship ship = new Ship("BATTLESHIP");
        assertTrue(board.placeShip(ship, 1, 'A', false));
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        assertEquals(board.getShips().size(), ships.size());
        assertEquals(board.getShips(), ships);
        Ship ship1 = new Ship("BATTLESHIP");
        ships.add(ship1);

        assertFalse(board.placeShip(ship1, 1, 'A', false));
        assertEquals(board.getShips().size(), ships.size()-1);

    }
    @Test
    public void testSize(){
        Board board = new Board();
        Ship ship = new Ship("BATTLESHIP");
        board.placeShip(ship, 5, 'D', true);
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        assertEquals(board.getShips(), ships);
        assertEquals(1, board.getShips().size());
        assertEquals(4,board.getShips().get(0).getOccupiedSquares().size());
        Ship ship1 = new Ship("BATTLESHIP");
        board.placeShip(ship1, 5, 'C', true);
        ships.add(ship1);
        assertEquals(board.getShips(), ships);
        assertEquals(2, board.getShips().size());
        assertEquals(5,board.getShips().get(0).getOccupiedSquares().get(0).getRow());
        assertEquals(5,board.getShips().get(1).getOccupiedSquares().get(0).getRow());
    }
}
