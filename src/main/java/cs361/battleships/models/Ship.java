package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.mchange.v1.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Ship {

	@JsonProperty private String kind;
	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private int size;
	@JSONProperty private int captainHealth;
	public Ship() {
		occupiedSquares = new ArrayList<>();

	}
	
	public Ship(String kind) {
		this();
		this.kind = kind;
		switch(kind) {
			case "MINESWEEPER":
				size = 2;
				captainHealth = 1;
				break;
			case "DESTROYER":
				size = 3;
				captainHealth = 2;
				break;
			case "BATTLESHIP":
				size = 4;
				captainHealth = 2;
				break;
		}

	}



	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public void place(char col, int row, boolean isVertical) {


		for (int i=0; i<size; i++) {
			if (isVertical) {

				occupiedSquares.add(new Square(row+i, col));
				if (i == size - 2) {
					occupiedSquares.get(occupiedSquares.size() - 1).setCaptain(true);
				}

			} else {
				occupiedSquares.add(new Square(row, (char) (col + i)));
				if (i == size - 2) {
					occupiedSquares.get(occupiedSquares.size() - 1).setCaptain(true);
				}
			}
		}

	}

	public boolean overlaps(Ship other) {
		Set<Square> thisSquares = Set.copyOf(getOccupiedSquares());
		Set<Square> otherSquares = Set.copyOf(other.getOccupiedSquares());
		Sets.SetView<Square> intersection = Sets.intersection(thisSquares, otherSquares);
		return intersection.size() != 0;
	}

	public boolean isAtLocation(Square location) {
		return getOccupiedSquares().stream().anyMatch(s -> s.equals(location));
	}

	public String getKind() {
		return kind;
	}

	public Result attack(int x, char y) {
		var attackedLocation = new Square(x, y);
		var square = getOccupiedSquares().stream().filter(s -> s.equals(attackedLocation)).findFirst();
		if (!square.isPresent()) {
			return new Result(attackedLocation);
		}
		var attackedSquare = square.get();
		if (attackedSquare.isHit() || attackedLocation.getisCaptain() && captainHealth == 0) {
			var result = new Result(attackedLocation);
			result.setResult(AtackStatus.INVALID);
			return result;
		}else if(attackedLocation.getisCaptain()){
			captainHealth -= 1;
			var result = new Result(attackedLocation);
			result.setShip(this);

			if (captainHealth == 0) {

				for (int i = 0; i < size; i++) {
					getOccupiedSquares().get(i).hit();
				}
				return result.setResult(AtackStatus.SUNK);
			}else {
				result.setResult(AtackStatus.MISS);
				return result;
			}


		}else {
			attackedSquare.hit();
			var result = new Result(attackedLocation);
			result.setShip(this);
			if (isSunk()) {
				result.setResult(AtackStatus.SUNK);
			} else {
				result.setResult(AtackStatus.HIT);
			}
			return result;
		}
	}

	@JsonIgnore
	public boolean isSunk() {
		return getOccupiedSquares().stream().allMatch(s -> s.isHit());
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Ship)) {
			return false;
		}
		var otherShip = (Ship) other;

		return this.kind.equals(otherShip.kind)
				&& this.size == otherShip.size
				&& this.occupiedSquares.equals(otherShip.occupiedSquares);
	}

	@Override
	public int hashCode() {
		return 33 * kind.hashCode() + 23 * size + 17 * occupiedSquares.hashCode();
	}

	@Override
	public String toString() {
		return kind + occupiedSquares.toString();
	}
}
