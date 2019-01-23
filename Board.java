package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Board {
	@JsonProperty private List<Ship> Ships;
	@JsonProperty private List<Result> Attacks;
	// New changes
	@JsonProperty private List<Ship> sunkShips;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		Ships = new ArrayList<>();
		Attacks = new ArrayList<>();
		sunkShips = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		List<Square> newSquares = new ArrayList<>();

		int size = ship.getSize();

		//Check if the ship will be placed off of the board
		if(x < 1 || x + size-1 > 10 || y < 'A' || y + size-1 > 'J')
			return false;

		//check if a ship overlaps
		for(int i = 0; i < getShips().size(); i++){
			for(int j = 0; j < (getShips().get(i).getOccupiedSquares()).size(); j++){
				for(int k = 0; k < size; k++){
					if(isVertical){
						if(((((getShips().get(i).getOccupiedSquares()).get(j)).getRow()) == (x+k)) && ((((Ships.get(i).getOccupiedSquares()).get(j)).getColumn()) == y) ){
							return false;
						}
					}else{
						if(((getShips().get(i).getOccupiedSquares()).get(j).getRow() == (x )) && ((Ships.get(i).getOccupiedSquares()).get(j).getColumn() == y+k )){
							return false;
						}
					}
				}

			}
		}


		//Create the squares the ship will occupy
		for(int i = 0; i < size; i++){
			Square s = new Square(x,y);
			if(isVertical){
				s.setRow(x+i);
				s.setColumn(y);
			}
			else{
				s.setRow(x);
				int nextValue = (int)y + i; // find the int value plus 1
				char c = (char)nextValue;
				s.setColumn(c);
			}
			newSquares.add(s);


		}
		ship.setOccupiedSquares(newSquares);

		//Check to make sure the ship was not placed diagonally
		for(int i = 0; i < size-1; i++){
			if(isVertical){
				//checks for a straight line
				if(ship.getOccupiedSquares().get(i).getColumn() == y+1){
					return false;
				}

			}
			else{
				if(ship.getOccupiedSquares().get(i).getRow() == x+1){
					return false;
				}
			}
		}

		Ships.add(ship);

		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//int sunkShips;
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
					// if the ship is sunk, add that to the list of sunk ships
					if (result.getResult() != AtackStatus.HIT) {
						result.setResult(AtackStatus.SUNK);
						sunkShips.add(result.getShip());
					}

					// if the amount of sunk ships equals initial ships, surrender
					if(sunkShips.size() >= 3) {
						result.setResult(AtackStatus.SURRENDER);
					}
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
		//TODO implement
		return  Attacks;
//		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
		this.Attacks = Attacks;
	}
}
