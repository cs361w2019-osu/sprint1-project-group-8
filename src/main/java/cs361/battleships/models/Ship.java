package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.mchange.v1.util.CollectionUtils;

import java.util.*;

public class Ship {

	@JsonProperty private String kind;
	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private int size;
	@JsonProperty private int captainHealth;

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

	public Ship(Ship other) {
		this.kind = other.getKind();
		this.size = other.getSize();
		this.captainHealth = other.getCaptainHealth();
		this.occupiedSquares = new ArrayList<>();
		for (Square s : other.getOccupiedSquares()) {
			this.occupiedSquares.add(new Square(s));
		}
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public int getSize() { return size; }

	public int getCaptainHealth() { return captainHealth; }

	public void place(char col, int row, boolean isVertical) {
		for (int i=0; i<size; i++) {
			if (isVertical) {
				occupiedSquares.add(new Square(row+i, col));
				if(i == size -2){
					occupiedSquares.get(i).setCaptain();
				}
			} else {
				occupiedSquares.add(new Square(row, (char) (col + i)));
				if(i == size -2){
					occupiedSquares.get(i).setCaptain();
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
		if (attackedSquare.isHit() || attackedSquare.getIsCaptain() && captainHealth == 0) {
			var result = new Result(attackedLocation);
			result.setResult(AtackStatus.INVALID);
			return result;
		}
		else if(attackedSquare.getIsCaptain()){
			captainHealth = captainHealth -1;
			var result = new Result(attackedLocation);
			result.setShip(this);
			if(captainHealth == 0){

				for(int i = 0; i < size; i++){

					getOccupiedSquares().get(i).hit();
				}
				 result.setResult(AtackStatus.SUNK);
				return result;
			}else{
				result.setResult(AtackStatus.BLOCKED);
				return result;
			}
		}else{

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

	private boolean validateMove(char moveDir) {
		int tRow = getMaxRow();
		int bRow = getMinRow();
		char rCol = getMaxCol();
		char lCol = getMinCol();

		return (bRow > 1 && moveDir == 'U') || (tRow < 10 && moveDir == 'D') || (rCol < 'J' && moveDir == 'R') || (lCol > 'A' && moveDir == 'L');
	}
	
	public void move(char moveDir) {
		if (validateMove(moveDir)) {
			for (var s : occupiedSquares) {
				s.move(moveDir);
			}
		}
	}

	public int getMaxRow() {
		return occupiedSquares.stream().max(Comparator.comparing(s -> s.getRow())).get().getRow();
	}

	public int getMinRow() {
		return occupiedSquares.stream().min(Comparator.comparing(s -> s.getRow())).get().getRow();
	}

	public char getMaxCol() {
		return occupiedSquares.stream().max(Comparator.comparing(s -> s.getColumn())).get().getColumn();
	}

	public char getMinCol() {
		return occupiedSquares.stream().min(Comparator.comparing(s -> s.getColumn())).get().getColumn();
	}

	public boolean checkMoveOverlap(Ship other, char moveDir) {
		Ship moved = new Ship(this);
		moved.move(moveDir);
		return other.overlaps(moved);
	}

	public boolean checkMoveOverlap(List<Ship> others, char moveDir) {
		for (Ship s : others) {
			if (checkMoveOverlap(s, moveDir)) {
				return true;
			}
		}
		return false;
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
