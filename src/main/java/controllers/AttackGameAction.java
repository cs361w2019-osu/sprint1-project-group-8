package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class AttackGameAction {

    @JsonProperty private Game game;
    @JsonProperty private int x;
    @JsonProperty private char y;
    @JsonProperty private boolean sonar;
    @JsonProperty private boolean moveShip;
    @JsonProperty private char moveDir;

    public Game getGame() {
        return game;
    }

    public int getActionRow() {
        return x;
    }

    public char getActionColumn() {
        return y;
    }

    public boolean getActionSonar() { return sonar; }


    public boolean getActionMoveShip() { return moveShip; }

    public char getActionMoveDir() { return moveDir; }
}
