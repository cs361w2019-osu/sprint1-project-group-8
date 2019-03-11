package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;
	@JsonProperty private List<Result> blockedShips;
	@JsonProperty private List <Ship> submergedSquares;
	@JsonProperty private int last;
	@JsonProperty private boolean hasLaser;
	@JsonProperty private boolean moveYes;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
		blockedShips = new ArrayList<>();
		submergedSquares = new ArrayList<>();
		last = 0;
		hasLaser = false;
		moveYes = false;
	}

	public void changeLaserForTest(boolean setLaser){
		hasLaser = setLaser;
	}
	public boolean placeShip1(Ship ship, int x, char y, boolean isVertical, boolean isSubmerged) {
		if (ships.size() >= 5) {
			return false;
		}
		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = new Ship(ship.getKind());

		placedShip.place(y, x, isVertical, isSubmerged);
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
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical, boolean isSubmerged) {
		if (ships.size() >= 5) {
			return false;
		}
		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = new Ship(ship.getKind());

		placedShip.place(y, x, isVertical, isSubmerged);


		if (ships.stream().anyMatch(s -> s.overlaps(placedShip))) {
			return false;
		}
		if (placedShip.getOccupiedSquares().stream().anyMatch(s -> s.isOutOfBounds())) {
			return false;
		}

			ships.add(placedShip);


		return true;
	}

	public boolean playerHasLaser() {
		return hasLaser;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Square square  = new Square(x, y);
		Result attackResult = attack(new Square(x, y));
		if(attackResult.getResult() == AtackStatus.BLOCKED){

			blockedShips.add(attackResult);
			last = 1;
		}
		else{
			attacks.add(attackResult);
			last = 0;
		}
		// If that spot has a MISS and at least a ship has been sunk,
		// repeat attacks only during laser phase or once the fleet is moved
		if(attackResult.getResult() == AtackStatus.MISS && ships.stream().allMatch(ship -> ship.isSunk())){
			if (!hasLaser && duplicateCheck(square) && shipPresent(x, y)) {
				for (Ship ships : this.getShips()) {
					if (!moveYes && duplicateCheck(square)) {
						attackResult.setResult(AtackStatus.INVALID);
						return attackResult;
					}
				}
			} else if (duplicateCheck(square) && !hasLaser) {
				attackResult.setResult(AtackStatus.INVALID);
				return attackResult;
			}
			this.attacks.add(attackResult);
		}

		return attackResult;
	}

	private Result attack(Square s) {

		if (attacks.stream().anyMatch(r -> r.getLocation().equals(s) && !r.getResult().equals(AtackStatus.OCCUPIED) && !r.getResult().equals(AtackStatus.EMPTY) && !r.getResult().equals(AtackStatus.FLEETMOVE))) {

			var attackResult = new Result(s);
			attackResult.setResult(AtackStatus.INVALID);

			return attackResult;
		}
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			return attackResult;
		}

		var canHitSub = false;
		var hitShip = shipsAtLocation.get(0);

		if(hitShip.getOccupiedSquares().get(0).getIsSubmerged() && hasLaser){
			canHitSub = true;
		}
		var attackResult = hitShip.attack(s.getRow(), s.getColumn(), canHitSub);

		if (attackResult.getResult() == AtackStatus.SUNK) {
			if (ships.stream().allMatch(ship -> ship.isSunk())) {
				attackResult.setResult(AtackStatus.SURRENDER);
			}
		}
		if(shipsAtLocation.size() > 1 ){
			canHitSub = false;
			 hitShip = shipsAtLocation.get(1);

			if(hitShip.getOccupiedSquares().get(1).getIsSubmerged() && hasLaser){
				canHitSub = true;
			}
			 attackResult = hitShip.attack(s.getRow(), s.getColumn(), canHitSub);

			if (attackResult.getResult() == AtackStatus.SUNK) {
				if (ships.stream().allMatch(ship -> ship.isSunk())) {
					attackResult.setResult(AtackStatus.SURRENDER);
				}
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

	public boolean duplicateCheck(Square attackLocation) {
		// loops through to check all attacks made on teh board, and...
		// returns true if another attack at that spot is possible, false if not
		for(Result attack : this.getAttacks()) {
			if((attack.getLocation().getRow() == attackLocation.getRow()) &&
					(attack.getLocation().getColumn() == attackLocation.getColumn())) {
				return true;
			}
		}
		return false;
	}
	
	// Check if there are any conflicts in that spot/square
	public static boolean conflictSpot(Square sq1, Square sq2) {
		return (sq1.getRow()==sq2.getRow() && sq1.getColumn()==sq2.getColumn());
	}


	// Checks whether a ship exist at that spot
	private boolean shipPresent(int x, char y){
		Square sq = new Square(x, y);
		for(Ship ships : this.getShips()){
			for(Square squares : ships.getOccupiedSquares()) {
				if (conflictSpot(sq, squares)) {
					return true;
				}
			}
		}
		return false;
	}

	public Result moveShips(char moveDir) {
		switch (moveDir) {
			case 'U': ships.sort(Comparator.comparing(s -> s.minRow()));
					  break;
			case 'D': ships.sort(Comparator.comparing(s -> s.maxRow()));
					  Collections.reverse(ships);
				 	  break;
			case 'L': ships.sort(Comparator.comparing(s -> s.minCol()));
					  break;
			case 'R': ships.sort(Comparator.comparing(s -> s.maxCol()));
					  Collections.reverse(ships);
					  break;
		}

		for (int i = 0; i < ships.size(); i++) {
			List jList = new ArrayList();
			if (i == 0 || !ships.get(i).checkMoveOverlap(ships.subList(0, i), moveDir)) {
				var changeOpponentHit = false;
				for(int j = 0; j < ships.get(i).getOccupiedSquares().size(); j++) {
					if(ships.get(i).getOccupiedSquares().get(j).isHit()) {
						ships.get(i).getOccupiedSquares().get(j).changeHit();
					//	JOptionPane.showMessageDialog(null, "Status of new square " + ships.get(i).getOccupiedSquares().get(j) + " is now " + ships.get(i).getOccupiedSquares().get(j).getHit());
						changeOpponentHit = true;
						jList.add(j);
					}
				}
				ships.get(i).move(moveDir);
				if(changeOpponentHit) {
					for(int j = 0; j < ships.get(i).getOccupiedSquares().size(); j++) {
						if(jList.contains(j)) {
							ships.get(i).getOccupiedSquares().get(j).hit();
						//	JOptionPane.showMessageDialog(null, "New hit at square: " + ships.get(i).getOccupiedSquares().get(j));
						}
					}
				}
			}
		}

		Result result = new Result(new Square(0, 'Z'));
		result.setResult(AtackStatus.FLEETMOVE);
		moveYes = true;
		return result;
	}

	public void unlockLaser() {
		hasLaser = true;
	}

	public void trackOpponentMove(Result result) {
		attacks.add(result);
	}

	List<Ship> getShips() {
		return ships;
	}

	List<Result> getAttacks() { return attacks; }
}
