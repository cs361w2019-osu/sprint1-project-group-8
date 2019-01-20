package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Board {
	@JsonProperty private List<Ship> Ships;
	@JsonProperty private List<Result> Attacks;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// TODO Implement
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// TODO Implement
		return false;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result result = new Result();
		result.setLocation(new Square(x, y));

		if (x > 10 || x < 1 || y > 'J' || y < 'A') {
			result.setResult(AtackStatus.INVALID);
			return result;
		}

		for (Ship ship : this.Ships) {
			for (Square square : ship.getOccupiedSquares()) {
				if (square.getRow() == x && square.getColumn() == y) {
					result.setShip(ship);

					for (Square shipSquare : ship.getOccupiedSquares()) {
						boolean shipSquareHit = false;

						for (Result oldAttack : Attacks) {
							Square oldAttackSquare = oldAttack.getLocation();

							boolean prevHit = oldAttackSquare.getRow() == shipSquare.getRow() &&
									          oldAttackSquare.getColumn() == shipSquare.getColumn();
							boolean currHit = shipSquare.getRow() == x && shipSquare.getColumn() == y;

							if (prevHit || currHit) {
								shipSquareHit = true;
								break;
							}
						}

						if (!shipSquareHit) {
							result.setResult(AtackStatus.HIT);
							break;
						}
					}

					if (result.getResult() != AtackStatus.HIT)
						result.setResult(AtackStatus.SUNK);
				}
			}
		}

		if (result.getResult() != AtackStatus.HIT && result.getResult() != AtackStatus.SUNK)
			result.setResult(AtackStatus.MISS);

		Attacks.add(result);
		return result;
	}

	public List<Ship> getShips() {
		return this.Ships;
	}

	public void setShips(List<Ship> ships) {
		this.Ships = ships;
	}

	public List<Result> getAttacks() {
		return this.Attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.Attacks = attacks;
	}
}
