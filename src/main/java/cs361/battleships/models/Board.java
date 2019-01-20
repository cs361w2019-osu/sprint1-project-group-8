package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {

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
		List<Square> sL = null;
		Square s = null;
		if(x + ship.getSize() < 1 || x + ship.getSize() > 10)
			return false;
		if(y + ship.getSize() < 'A' || y + ship.getSize() > 'J')
			return false;
		//Create the squares the ship will occupy
		for(int i = 0; i < ship.getSize(); i++){

			if(isVertical){
				s.setRow(++x);
				s.setColumn(y);
			}
			else{
				s.setRow(i);
				s.setColumn(++y);
			}
			sL.add(s);
		}

		ship.setOccupiedSquares(sL);
		//Check to make sure the ship was not placed diagonally
		for(int i = 0; i < ship.getSize()-1; i++){
			if(isVertical){
				if(ship.getOccupiedSquares().get(i).getRow() != ship.getOccupiedSquares().get(i+1).getRow()){
					return false;
				}

			}
			else{
				if(ship.getOccupiedSquares().get(i).getColumn() != ship.getOccupiedSquares().get(i+1).getColumn()){
					return false;
				}
			}
		}
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
		return null;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
}
