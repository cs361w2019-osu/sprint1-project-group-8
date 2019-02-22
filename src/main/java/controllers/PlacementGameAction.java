package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class PlacementGameAction {

    @JsonProperty private Game game;
    @JsonProperty private String shipType;
    @JsonProperty private int x;
    @JsonProperty private char y;
    @JsonProperty private boolean isVertical;
    @JsonProperty private int captainPlacement;
    @JsonProperty private int captainHealth;

    public Game getGame() {
        return game;
    }

    public int getActionRow() {
        return x;
    }

    public char getActionColumn() {
        return y;
    }

    public String getShipType() {
        return shipType;
    }
    public int getCaptainPlacement(){

        return  captainPlacement;
    }
    public int getCaptainHealth(){

        return  captainPlacement;
    }
    public void setCaptainHealth(int health){
        this.captainHealth = health;
    }
    public void setCaptainPlacement(int placement){
        this.captainPlacement = placement;
    }
    public boolean isVertical() {
        return isVertical;
    }
}
