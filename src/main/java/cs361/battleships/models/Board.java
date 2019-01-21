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
		Ships = new ArrayList<>();
		Attacks = new ArrayList<>();
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
		Result result = new Result();
		result.setLocation(new Square(x, y));

		for (Ship ship : this.Ships) {
			for (Square square : ship.getOccupiedSquares()) {
				if (square.getRow() == x && square.getColumn() == y) {
					result.setShip(ship);
					result.setResult(AtackStatus.HIT);
				}
			}
		}

		if (result.getResult() != AtackStatus.HIT)
			result.setResult(AtackStatus.MISS);

		Attacks.add(result);
		return result;
	}

	public List<Ship> getShips() {
		//TODO implement
		return this.Ships;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
		this.Ships = ships;
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
}
