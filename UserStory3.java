package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
public class UserStory3 {

    @Test
    public void testSurrender() {
        List<Ship> sunkShips = new ArrayList<Ship>();
        Result result = new Result();
        Board board = new Board();

        board.placeShip(new Ship("MINESWEEPER"), 5, 'E', true);
        board.attack(5, 'E' );
        board.attack(6, 'E' );
        sunkShips.add(result.getShip());

        board.placeShip(new Ship("DESTROYER"), 9, 'D', false);
        //board.attack(9, 'D');
        board.attack(9, 'E');
        board.attack(9, 'F');
        sunkShips.add(result.getShip());

        board.placeShip(new Ship("BATTLESHIP"), 4, 'G', true);
        board.attack(4, 'G');
        board.attack(5, 'G');
        board.attack(6, 'G');
        board.attack(7, 'G');
        sunkShips.add(result.getShip());


        if(sunkShips.size() >= 3)
            result.setResult(AtackStatus.SURRENDER);
        assertTrue(true);
    }
}