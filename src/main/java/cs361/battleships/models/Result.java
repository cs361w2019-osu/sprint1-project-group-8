package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Result {
	@JsonProperty private AtackStatus Result;
	@JsonProperty private Ship Ship;
	@JsonProperty private Square Location;

	public AtackStatus getResult() {
		return this.Result;
	}

	public void setResult(AtackStatus result) {
		this.Result = result;
	}

	public Ship getShip() {
		return this.Ship;
	}

	public void setShip(Ship ship) {
		this.Ship = ship;
	}

	public Square getLocation() {
		return this.Location;
	}

	public void setLocation(Square square) {
		this.Location = square;
	}
}
