package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Square {

	@JsonProperty private int row;
	@JsonProperty private char column;
	@JsonProperty private boolean hit = false;
	@JsonProperty private boolean isCaptain = false;
	@JsonProperty private boolean isSubmerged = false;

	public Square() {
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
	}

	public Square(Square other) {
		this.row = other.getRow();
		this.column = other.getColumn();
		this.hit = other.getHit();
		this.isCaptain = other.getIsCaptain();
	}

	public void move(char moveDir) {
		switch(moveDir) {
			case 'U': this.row--;
					  break;
			case 'D': this.row++;
					  break;
			case 'L': this.column--;
					  break;
			case 'R': this.column++;
					  break;
		}
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public boolean getHit() { return hit; }

	public boolean getIsCaptain(){
		return isCaptain;
	}

	public void setCaptain(){
		this.isCaptain = true;
	}
	public boolean getIsSubmerged(){
		return isSubmerged;
	}
	public void setIsSubmerged(){
		this.isSubmerged = true;
	}
	@Override
	public boolean equals(Object other) {
		if (other instanceof Square) {
			return ((Square) other).row == this.row && ((Square) other).column == this.column;
		}
		return false;
	}

	public boolean equals(Object other, boolean checkSubmerge) {
		if (other instanceof Square) {
			return ((Square) other).row == this.row && ((Square) other).column == this.column && checkSubmerge == this.isSubmerged;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 31 * row + column;
	}

	@JsonIgnore
	public boolean isOutOfBounds() {
		return row > 10 || row < 1 || column > 'J' || column < 'A';
	}

	public boolean isHit() {
		return hit;
	}

	public void hit() {
		hit = true;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ')';
	}
}
