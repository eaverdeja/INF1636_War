/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.MainFrame;
import java.awt.BorderLayout;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import view.Console;
import view.MapPanel;
import model.Click;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import model.Player;
import java.util.Set;
import model.Continent;
import model.Territory;

/**
 *
 * @author lorenzosaraiva
 */
public class GameManager extends Observable implements Controller, Observer{

 
    public enum turnPhase {
        newArmyPhase, attackPhase, newAttackerPhase, moveArmyPhase
    }
    
    private static GameManager turnInstance = null;
    private static int playerQuantity;
    
    private MainFrame mainFrame;
    
    
    private List<Territory> lstTerritorios = new ArrayList<>();
    private Map<Territory,List<Territory>> neighbourMap;

    private Click click = null; 
 
    private GameplayController attackController;
    private CardsController cardsController;
    private ObjectivesController objController;
    private TurnController turnController;
    private MapController mapController;
    private ButtonsController buttonsController;
    private PlayerController playerController;
    
    private Boolean hasConquered = false;
    private Boolean finishedAttacking = false;
    
    private int cardsChangeAmount = 4;
    private int armiesAdded = 0;


    //Implementing Singleton pattern
    protected GameManager() {
        
    }

    public static GameManager getInstance() {
        if (turnInstance == null) {
            turnInstance = new GameManager();
        }
        return turnInstance;
    }
    
     
     
    public void gameSetUp(int players){
        playerController = new PlayerController(players);
        turnController = new TurnController();
        mapController = new MapController();
        attackController = new GameplayController();
        cardsController = new CardsController();
        objController = new ObjectivesController();
        objController.randomizeObjectives();
        playerController.randomizeTerritories();
        getObjController().setContinentList(getMapController().getContinentList());
        buttonsController = new ButtonsController();
    }
    
    
    //START turn and phase control
    
    public void goToNextPhase() {
        turnController.goToNextPhase();
        setChanged();
        notifyObservers();
        clearChanged();
    }
    
    public void goToMovePhase(){
        turnController.setCurrentPhase(turnPhase.moveArmyPhase);
    }

    public void nextTurn() {
        turnController.nextTurn();
    }
      
    @Override
    public void update(Observable o, Object arg) {
        click = (Click)o;
        attackController.actionForClick(click.getValue());
        attackController.setCurrentTerritory(getMapPanel().getCurrentTerritory());
    }
    
    @Override
    public void consoleEvent(){
        String info = null;
        if(turnController.getCurrentPhase() == turnPhase.newArmyPhase){
            info = Console.getInstance().getText().replaceAll("\\d+", Integer.toString(playerController.getCurrentPlayer().newArmyAmount()-armiesAdded));    
            info = info.replaceAll("We are in the .*","We are in the newArmyPhase");
        }
        else if(turnController.getCurrentPhase() == turnPhase.attackPhase){
            info = Console.getInstance().getText().replaceAll("You have \\d+ armies left", "Who do you wish to attack?");
            info = info.replaceAll("We are in the .*","We are in the attackPhase");
        }
        else if(turnController.getCurrentPhase() == turnPhase.moveArmyPhase){
            info = Console.getInstance().getText();
            info = Console.getInstance().getText().replaceAll("Who do you wish to attack\\?", "Make your move");
            info = info.replaceAll("We are in the .*","We are in the moveArmyPhase");
        }
        
        Console.getInstance().setText(info);
        Console.getInstance().repaint();
    }
    
    
    //END turn and phase control
    
    //Getters and Setters

    public List<Player> getPlayers() {
        return Arrays.asList(playerController.getPlayerArray());
    }

    public Player getCurrentPlayer() {
        return playerController.getCurrentPlayer();
    }
    
    public void setCurrentPlayer(Player currentPlayer) {
        playerController.setCurrentPlayer(currentPlayer);
    }

    public turnPhase getTurnPhase() {
        return turnController.getCurrentPhase();
    }

    public void setLstTerritorios(List<Territory> lstTerritorios) {
        this.lstTerritorios = lstTerritorios;
    }

    public int getArmiesAdded() {
        return armiesAdded;
    }

    public void setArmiesAdded(int armiesAdded) {
        this.armiesAdded = armiesAdded;
        
        setChanged();
        notifyObservers();
    }
    
    public MapPanel getMapPanel() {
        return getMapController().getMapPanel();
    }

    
    public Boolean getHasConquered() {
        return hasConquered;
    }

    public void setHasConquered(Boolean hasConquered) {
        this.hasConquered = hasConquered;
    }
    
    public CardsController getCardsController() {
        return cardsController;
    }

    public void setCardsController(CardsController cardsController) {
        this.cardsController = cardsController;
    }

    public int getCardsChangeAmount() {
        return cardsChangeAmount;
    }

    public void setCardsChangeAmount(int cardsChangeAmount) {
        this.cardsChangeAmount = cardsChangeAmount;
    }
    /**
     * @return the finishedAttacking
     */
    public Boolean getFinishedAttacking() {
        return finishedAttacking;
    }

    /**
     * @param finishedAttacking the finishedAttacking to set
     */
    public void setFinishedAttacking(Boolean finishedAttacking) {
        this.finishedAttacking = finishedAttacking;
    }
    
    public ObjectivesController getObjController() {
        return objController;
    }

    public void setObjController(ObjectivesController objController) {
        this.objController = objController;
    }
    
    public void setCurrentTerritory(Territory t){
        getMapPanel().setCurrentTerritory(t);
    }
    
    public Territory getCurrentTerritory(){
        return getMapPanel().getCurrentTerritory();
    }
    
    public void repaint(){
        getMapPanel().repaint();
    }
    

    public Map<Territory,List<Territory>> getNeighbourMap() {
        return getMapController().getNeighbourMap();
    }
    
    public List<Territory> getTerritoryList(){
        return getMapController().getTerritoryList();
    }
    
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public MapController getMapController() {
        return mapController;
    }

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }
    
    public boolean checkPlayerHasContinent(String s,Player p){
        
        return playerController.checkPlayerHasContinent(s, p);
    }
}
