package cs361.battleships.models;

public class UserStory1 {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    public void testValidPlacement(){

    }
}
