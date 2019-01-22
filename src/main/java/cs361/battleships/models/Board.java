package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Board {
	@JsonProperty private List<Ship> ships;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// TODO Implement
		ships = new ArrayList<>();
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
					if(((((getShips().get(i).getOccupiedSquares()).get(j)).getRow()) == (x+k)) && ((((ships.get(i).getOccupiedSquares()).get(j)).getColumn()) == y) ){
						return false;
					}
				}else{
					if(((getShips().get(i).getOccupiedSquares()).get(j).getRow() == (x )) && ((ships.get(i).getOccupiedSquares()).get(j).getColumn() == y+k )){
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

		ships.add(ship);

		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		return null;
	}

	public List<Ship> getShips() {
		//TODO implement
		return this.ships;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
}
