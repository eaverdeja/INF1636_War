/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Collections;
import java.util.Random;
import model.Continent;
import model.Player;
import model.Territory;

/**
 *
 * @author lorenzosaraiva
 */
public class PlayerController {
    
    private GameManager gameManager = GameManager.getInstance();
    private Player[] playerArray;
    private Player currentPlayer;
    private int playerQuantity;

    public PlayerController(int players){
        playerQuantity = players;
        createAndRandomizePlayers(players);
    }
    
    private void createAndRandomizePlayers(int players) {
        
        setPlayerArray(new Player[players]);
        playerQuantity = players;
       
        for (int i = 0; i < players; i++) {
            Player newPlayer = new Player();
            getPlayerArray()[i] = newPlayer;
        }
        setCurrentPlayer(this.getPlayerArray()[0]);
    }
    
     public void randomizeTerritories() {
        int counter = 0;
        long seed = System.nanoTime();
        Collections.shuffle(gameManager.getTerritoryList(), new Random(seed));
        for (Territory t : gameManager.getTerritoryList()) {
            t.setOwnerPlayer(this.getPlayerArray()[counter]);
            t.setQtdExercitos(1);
            this.getPlayerArray()[counter].setCurrentTerritories(this.getPlayerArray()[counter].getCurrentTerritories() + 1);
            counter++;
            if (counter == playerQuantity) {
                counter = 0;
            }
        }
    }


    public Player[] getPlayerArray() {
        return playerArray;
    }

    public void setPlayerArray(Player[] playerArray) {
        this.playerArray = playerArray;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    public boolean checkPlayerHasContinent(String s,Player p){
        
        for (Continent c: gameManager.getObjController().getContinentList()){
            if (c.getName() == s){
                return c.playerHasContinent(p);
            }
        }
        return false;
    }
    
}