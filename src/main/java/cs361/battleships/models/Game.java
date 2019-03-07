package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cs361.battleships.models.AtackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(ship, randRow(), randCol(), randVertical());
        } while (!opponentPlacedSuccessfully);

        return true;
    }

    public void opponentAttack() {
        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        if (!playersBoard.playerHasLaser() && opponentAttackResult.getResult() == SUNK) {
            playersBoard.unlockLaser();
        }
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y, boolean sonar) {
        if (sonar) {
            opponentsBoard.sonarPulse(x, y);
        }
        else {
            Result playerAttack = opponentsBoard.attack(x, y);
            if (playerAttack.getResult() == INVALID) {
                return false;
            }
            else if (!opponentsBoard.playerHasLaser() && playerAttack.getResult() == SUNK) {
                opponentsBoard.unlockLaser();
            }
        }

        opponentAttack();
        return true;
    }

    public boolean moveShips(char moveDir) {
        if (moveDir != 'U' && moveDir != 'D' && moveDir != 'L' && moveDir != 'R')
            return false;

        playersBoard.moveShips(moveDir);
        opponentAttack();
        return true;
    }

    private char randCol() {
        int random = new Random().nextInt(10);
        return (char) ('A' + random);
    }

    private int randRow() {
        return  new Random().nextInt(10) + 1;
    }

    private boolean randVertical() {
        return new Random().nextBoolean();
    }
}
