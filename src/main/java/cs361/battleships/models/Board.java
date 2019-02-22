package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		if (ships.size() >= 3) {
			return false;
		}
		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = new Ship(ship.getKind());
		placedShip.place(y, x, isVertical);
		if (ships.stream().anyMatch(s -> s.overlaps(placedShip))) {
			return false;
		}
		if (placedShip.getOccupiedSquares().stream().anyMatch(s -> s.isOutOfBounds())) {
			return false;
		}
		ships.add(placedShip);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result attackResult = attack(new Square(x, y));
		attacks.add(attackResult);
		return attackResult;
	}

	private Result attack(Square s) {
		if (attacks.stream().anyMatch(r -> r.getLocation().equals(s) && !r.getResult().equals(AtackStatus.OCCUPIED) && !r.getResult().equals(AtackStatus.EMPTY))) {
			var attackResult = new Result(s);
			attackResult.setResult(AtackStatus.INVALID);
			return attackResult;
		}
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			return attackResult;
		}
		var hitShip = shipsAtLocation.get(0);
		var attackResult = hitShip.attack(s.getRow(), s.getColumn());
		if (attackResult.getResult() == AtackStatus.SUNK) {
			if (ships.stream().allMatch(ship -> ship.isSunk())) {
				attackResult.setResult(AtackStatus.SURRENDER);
			}
		}
		return attackResult;
	}

	public Result sonarPulse(int x, char y) {
		Result finalResult = new Result(new Square(x, y));

		for (int i = Math.max(1, x - 2); i <= Math.min(10, x + 2); i++) {
			for (int j = Math.max(0, y - 'A' - 2); j <= Math.min(9, y - 'A' + 2); j++) {
				if (Math.abs(i - x) + Math.abs(j - y + 'A') <= 2) {
					var s = new Square(i, (char)(j + 'A'));
					var result = new Result(s);
					var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());

					if (shipsAtLocation.size() == 0) {
						result.setResult(AtackStatus.EMPTY);
					}
					else {
						result.setResult(AtackStatus.OCCUPIED);
					}
					attacks.add(result);

					if (x == i && y == (char)(j + 'A')) {
						finalResult.setResult(result.getResult());
					}
				}
			}
		}

		return finalResult;
	}

	List<Ship> getShips() {
		return ships;
	}

	List<Result> getAttacks() { return attacks; }
}
