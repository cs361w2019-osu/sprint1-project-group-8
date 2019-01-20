package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {
	private String kind;
	private int size;
	@JsonProperty private List<Square> occupiedSquares;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}
	
	public Ship(String kind) {

		this();
		if(kind == "MINESWEEPER"){
			this.kind = kind;
			this.size = 2;

		}
		else if(kind == "DESTROYER"){
			this.kind = kind;
			this.size = 3;
		}

	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}
}
