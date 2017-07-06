package com.codecool.maze;

import java.util.ArrayList;
import java.util.Arrays;

public class PathFinder {

    private int[][] maze;
    private Cell startTile;
    private Cell goalTile;
    private ArrayList<Cell> openList;
    private ArrayList<Cell> closedList;

    public PathFinder(int[][] maze, int[] startCoordinates, int[] goalCoordinates) {
        this.maze = maze;
        startTile = new Cell(startCoordinates);
        goalTile = new Cell(goalCoordinates);
        openList = new ArrayList<>();
        closedList = new ArrayList<>();
        startTile.setScore(0);
        openList.add(startTile);
    }

    public int[][] getMaze() {
        return maze;
    }

    private void removeTileFromList(ArrayList<Cell> list, int[] tileCoordinatesToRemove){
        for(int x = 0; x < list.size(); x++){
            if(list.get(x).getCoordinates() == tileCoordinatesToRemove){
                list.remove(x);
            }
        }
    }

    private boolean isTileInList(ArrayList<Cell> list, int[] tileCoordinatesToSearch){
        for(Cell tile : list){
            if(Arrays.equals(tile.getCoordinates(), tileCoordinatesToSearch)){
                return true;
            }
        }
        return false;
    }

    private Cell getTileFromList(ArrayList<Cell> list, int[] tileCoordinatesToSearch){
        for(Cell tile : list){
            if(Arrays.equals(tile.getCoordinates(), tileCoordinatesToSearch)){
                return tile;
            }
        }
        return null;
    }

    private int getEstimatedCost(int[] fromCoordinates, int[] toCoordinates){
        return Math.abs(toCoordinates[0] - fromCoordinates[0]) + Math.abs(toCoordinates[1] - fromCoordinates[1]);
    }

    private Cell getTileFromOpenListWithLowestScore(){

        Cell returnTile = new Cell(new int[]{});
        int score = openList.get(0).getScore();
        for(Cell tile : openList){
            if(tile.getScore() <= score){
                score = tile.getScore();
                returnTile = tile;
            }
        }
        return returnTile;
    }

    private ArrayList<Cell> getListOfValidNeighbors(Cell tile){
        ArrayList<Cell> returnList = new ArrayList<>();
        int row = tile.getCoordinates()[0];
        int col = tile.getCoordinates()[1];

        try{
            if(maze[row-1][col] == 0){
                int [] coordinates = new int[]{row-1, col};
                Cell tileToAdd = new Cell(coordinates, tile.getCost()+1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                returnList.add(tileToAdd);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        try{
            if(maze[row+1][col] == 0){
                int [] coordinates = new int[]{row+1, col};
                Cell tileToAdd = new Cell(coordinates, tile.getCost()+1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                returnList.add(tileToAdd);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        try {
            if (maze[row][col - 1] == 0) {
                int[] coordinates = new int[]{row, col-1};
                Cell tileToAdd = new Cell(coordinates, tile.getCost() + 1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                returnList.add(tileToAdd);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        try {
            if (maze[row][col + 1] == 0) {
                int[] coordinates = new int[]{row, col+1};
                Cell tileToAdd = new Cell(coordinates, tile.getCost() + 1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                returnList.add(tileToAdd);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}

        return returnList;
    }

    private void generateShortestRoute(){
        while(!openList.isEmpty()){
            Cell currentTile = getTileFromOpenListWithLowestScore();
            closedList.add(currentTile);
            removeTileFromList(openList, currentTile.getCoordinates());

            if(isTileInList(closedList, goalTile.getCoordinates())){
                break;
            }

            ArrayList<Cell> validNeighbors = getListOfValidNeighbors(currentTile);

            for(Cell neighbor : validNeighbors){
                if(isTileInList(closedList, neighbor.getCoordinates())){
                    continue;
                }
                else if(!isTileInList(openList, neighbor.getCoordinates())){
                    openList.add(neighbor);
                }
                else{
                    if(currentTile.getCost() + 1 +
                            getEstimatedCost(neighbor.getCoordinates(),
                                    goalTile.getCoordinates())
                            < neighbor.getScore()){
                        neighbor.setParent(currentTile);
                        neighbor.setScore(currentTile.getCost() + 1 +
                                getEstimatedCost(neighbor.getCoordinates(),
                                        goalTile.getCoordinates()));
                    }
                }
            }
        }
    }

    public int[][] getShortestPathCoordinates(){
        generateShortestRoute();
        int[][] returnList = new int[closedList.size()][2];
        int[] goalCoordinates = closedList.get(closedList.size()-1).getCoordinates();
        int counter = 0;
        returnList[0][0] = goalCoordinates[0];
        returnList[0][1] = goalCoordinates[1];
        Cell lastListMember = getTileFromList(closedList, returnList[counter]);
        counter++;
        while(lastListMember.getParent() != null){
            returnList[counter] = (lastListMember.getParent().getCoordinates());
            lastListMember = getTileFromList(closedList, returnList[counter]);
            counter++;
        }
        return returnList;
    }
}
