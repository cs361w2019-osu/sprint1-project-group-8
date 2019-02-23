package cs361.battleships.models;

public enum AtackStatus {

	/**
	 * The result if an attack results in a miss.
	 */
	MISS,

	/**
	 * The result if an attack results in a hit on an enemy ship.
	 */
	HIT,

	/**
	 * THe result if an attack sinks the enemy ship
	 */
	SUNK,

	/**
	 * The results if an attack results in the defeat of the opponent (a
	 * surrender).
	 */
	SURRENDER,
	
	/**
	 * The result if the coordinates given are invalid.
	 */
	INVALID,

	/**
	 * The result if a square in a sonar pulse is empty
	 */
	EMPTY,

	/**
	 * The result if a square in a sonar pulse is occupied
	 */
	OCCUPIED,
	BLOCKED,
}
