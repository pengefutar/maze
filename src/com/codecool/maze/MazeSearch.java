package com.codecool.maze;

import java.util.ArrayList;
import java.util.Arrays;

public class MazeSearch {

    int[][] maze;
    SearchTile startTile;
    SearchTile goalTile;
    ArrayList<SearchTile> openList;
    ArrayList<SearchTile> closedList;

    public MazeSearch(int[][] maze, int[] startCoordinates, int[] goalCoordinates) {
        this.maze = maze;
        startTile = new SearchTile(startCoordinates);
        goalTile = new SearchTile(goalCoordinates);
        openList = new ArrayList<>();
        closedList = new ArrayList<>();
        startTile.setScore(0);
        openList.add(startTile);
    }

    private void removeTileFromList(ArrayList<SearchTile> list, int[] tileCoordinatesToRemove){
        for(int x = 0; x < list.size(); x++){
            if(list.get(x).getCoordinates() == tileCoordinatesToRemove){
                list.remove(x);
            }
        }
    }

    private boolean isTileInList(ArrayList<SearchTile> list, int[] tileCoordinatesToSearch){
        for(SearchTile tile : list){
            if(Arrays.equals(tile.getCoordinates(), tileCoordinatesToSearch)){
                return true;
            }
        }
        return false;
    }

    private boolean isOpenListEmpty(){
        if(openList.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    private int getEstimatedCost(int[] fromCoordinates, int[] toCoordinates){
        int rowCost;
        int colCost;

        if(fromCoordinates[0] < toCoordinates[0]){
            rowCost = toCoordinates[0] - fromCoordinates[0];
        }else{
            rowCost = fromCoordinates[0] - toCoordinates[0];
        }

        if(fromCoordinates[1] < toCoordinates[1]){
            colCost = toCoordinates[1] - fromCoordinates[1];
        }else{
            colCost = fromCoordinates[1] - toCoordinates[1];
        }

        return rowCost + colCost;
    }

    private SearchTile getTileFromOpenListWithLowestScore(){

        SearchTile returnTile = new SearchTile(new int[]{});
        int score = openList.get(0).getScore();
        for(SearchTile tile : openList){
            if(tile.getScore() <= score){
                score = tile.getScore();
                returnTile = tile;
            }
        }
        return returnTile;
    }

    private ArrayList<SearchTile> getListOfValidNeighbors(SearchTile tile){
        ArrayList<SearchTile> returnList = new ArrayList<>();
        int row = tile.getCoordinates()[0];
        int col = tile.getCoordinates()[1];

        try{
            if(maze[row-1][col] == 0){
                int [] coordinates = new int[]{row-1, col};
                SearchTile tileToAdd = new SearchTile(coordinates, tile.getCost()+1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                returnList.add(tileToAdd);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        try{
            if(maze[row+1][col] == 0){
                int [] coordinates = new int[]{row+1, col};
                SearchTile tileToAdd = new SearchTile(coordinates, tile.getCost()+1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                returnList.add(tileToAdd);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        try {
            if (maze[row][col - 1] == 0) {
                int[] coordinates = new int[]{row, col-1};
                SearchTile tileToAdd = new SearchTile(coordinates, tile.getCost() + 1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                returnList.add(tileToAdd);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        try {
            if (maze[row][col + 1] == 0) {
                int[] coordinates = new int[]{row, col+1};
                SearchTile tileToAdd = new SearchTile(coordinates, tile.getCost() + 1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                returnList.add(tileToAdd);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}

        return returnList;
    }

    private void generateShortestRoute(){
        while(!isOpenListEmpty()){
            SearchTile currentTile = getTileFromOpenListWithLowestScore();
            closedList.add(currentTile);
            removeTileFromList(openList, currentTile.getCoordinates());

            if(isTileInList(closedList, goalTile.getCoordinates())){
                break;
            }

            ArrayList<SearchTile> validNeighbors = getListOfValidNeighbors(currentTile);

            for(SearchTile neighbor : validNeighbors){
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

    public ArrayList<SearchTile> getShortestPath(){
        generateShortestRoute();
        ArrayList<SearchTile> returnList = new ArrayList<>();
        returnList.add(closedList.get(closedList.size()-1));
        SearchTile lastListMember = returnList.get(returnList.size()-1);
        while(lastListMember.getParent() != null){
            returnList.add(lastListMember.getParent());
            lastListMember = returnList.get(returnList.size()-1);
        }
        return returnList;
    }
}
