package cs361.battleships.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testInvalidPlacement() {
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testPlaceMinesweeper() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
    }

    @Test
    public void testAttackEmptySquare() {
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true);
        Result result = board.attack(2, 'E');
        assertEquals(AtackStatus.MISS, result.getResult());
    }

    @Test
    public void testAttackShip() {
        Ship minesweeper = new Ship("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        minesweeper = board.getShips().get(0);
        Result result = board.attack(1, 'A');
        assertEquals(AtackStatus.SURRENDER, result.getResult());
        assertEquals(minesweeper, result.getShip());
    }

    @Test
    public void testAttackSameSquareMultipleTimes() {
        Ship minesweeper = new Ship("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        board.attack(1, 'A');
        Result result = board.attack(1, 'A');
        assertEquals(AtackStatus.INVALID, result.getResult());
    }

    @Test
    public void testAttackSameEmptySquareMultipleTimes() {
        Result initialResult = board.attack(1, 'A');
        assertEquals(AtackStatus.MISS, initialResult.getResult());
        Result result = board.attack(1, 'A');
        assertEquals(AtackStatus.INVALID, result.getResult());
    }

    @Test
    public void testSurrender() {
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true);
        board.attack(1, 'A');
        var result = board.attack(2, 'A');
        assertEquals(AtackStatus.INVALID, result.getResult());
    }

    @Test
    public void testPlaceMultipleShipsOfSameType() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 5, 'D', true));

    }

    @Test
    public void testCantPlaceMoreThan3Ships() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 5, 'D', true));
        assertTrue(board.placeShip(new Ship("DESTROYER"), 6, 'A', false));
        assertFalse(board.placeShip(new Ship(""), 8, 'A', false));

    }

    @Test
    public void testSonarPulseOccupiedSquare() {
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true);
        var result = board.sonarPulse(1, 'A');
        assertEquals(AtackStatus.OCCUPIED, result.getResult());
    }

    @Test
    public void testSonarPulseEmptySquare() {
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true);
        var result = board.sonarPulse(8, 'E');
        assertEquals(AtackStatus.EMPTY, result.getResult());
    }

    @Test
    public void testSonarPulseEntirePulse() {
        board.placeShip(new Ship("DESTROYER"), 1, 'A', true);
        var result = board.sonarPulse(1, 'A');
        var ships = board.getShips();
        for (var a : board.getAttacks()) {
            var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(a.getLocation())).collect(Collectors.toList());
            if (shipsAtLocation.size() == 0) {
                assertTrue(a.getResult() == AtackStatus.EMPTY);
            }
            else {
                assertTrue(a.getResult() == AtackStatus.OCCUPIED);
            }
        }
    }

    @Test
    public void testSonarPulseRightAmountOfSquaresMiddle() {
        board.sonarPulse(5, 'E');
        assertEquals(board.getAttacks().size(), 13);
    }

    @Test
    public void testSonarPulseRightAmountOfSquaresCorners() {
        board.sonarPulse(1, 'A');
        assertEquals(board.getAttacks().size(), 6);
        board.sonarPulse(1, 'J');
        assertEquals(board.getAttacks().size(), 12);
        board.sonarPulse(10, 'A');
        assertEquals(board.getAttacks().size(), 18);
        board.sonarPulse(10, 'A');
        assertEquals(board.getAttacks().size(), 24);
    }

    @Test
    public void testSonarPulseRightAmountOfSquaresSides() {
        board.sonarPulse(1, 'E');
        assertEquals(board.getAttacks().size(), 9);
        board.sonarPulse(10, 'E');
        assertEquals(board.getAttacks().size(), 18);
        board.sonarPulse(5, 'A');
        assertEquals(board.getAttacks().size(), 27);
        board.sonarPulse(5, 'J');
        assertEquals(board.getAttacks().size(), 36);
    }

    @Test
    public void testMoveOneSquareCorrectDirection() {
        board.placeShip(new Ship("MINESWEEPER"), 6, 'D', true);
        board.placeShip(new Ship("DESTROYER"), 6, 'E', true);
        board.placeShip(new Ship("BATTLESHIP"), 6, 'F', true);

        board.moveShips('L');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'C');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'D');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'E');

        board.moveShips('R');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'D');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'E');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'F');

        board.moveShips('U');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getRow(), 5);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'D');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getRow(), 5);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'E');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getRow(), 5);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'F');

        board.moveShips('D');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'D');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'E');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getRow(), 6);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'F');
    }

    @Test
    public void testNoMoveOverlap() {
        board.placeShip(new Ship("MINESWEEPER"), 2, 'A', true);
        board.placeShip(new Ship("DESTROYER"), 2, 'B', true);
        board.placeShip(new Ship("BATTLESHIP"), 1, 'B', false);

        board.moveShips('L');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getRow(), 2);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'A');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getRow(), 2);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'B');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getRow(), 1);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'A');

        board.moveShips('U');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getRow(), 2);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'A');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getRow(), 2);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'B');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getRow(), 1);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'A');

        board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 10, 'I', false);
        board.placeShip(new Ship("DESTROYER"), 8, 'H', true);
        board.placeShip(new Ship("BATTLESHIP"), 7, 'G', false);

        board.moveShips('D');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getRow(), 10);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'I');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getRow(), 8);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'H');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getRow(), 7);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "BATTLESHIP").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'G');

        board.moveShips('R');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getRow(), 10);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "MINESWEEPER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'I');
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getRow(), 8);
        assertEquals(board.getShips().stream().filter(s -> s.getKind() == "DESTROYER").findFirst().get().getOccupiedSquares().get(0).getColumn(), 'H');
    }

    @Test
    public void testAttackWithSpaceLaser() {
        board.unlockLaser();
        Ship minesweeper = new Ship("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        minesweeper = board.getShips().get(0);
        Result result = board.attack(1, 'A');
        assertEquals(AtackStatus.SURRENDER, result.getResult());
        assertEquals(minesweeper, result.getShip());
    }
}
