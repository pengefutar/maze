package com.codecool.maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PathFinder {

    //    private int[][] maze;
    private Maze maze;
    private Cell startTile;
    private Cell goalTile;
    private List<Cell> openList = new ArrayList<>();
    private List<Cell> closedList = new ArrayList<>();

    public PathFinder(Maze maze) {
        this.maze = maze;
        startTile = new Cell(maze.getStartCoordinates());
        goalTile = new Cell(maze.getGoalCoordinates());
        startTile.setScore(0);
        openList.add(startTile);
    }

    public int[][] getMazeMap() {
        return maze.getData();
    }

    private void removeTileFromList(List<Cell> list, int[] tileCoordinatesToRemove) {
        Iterator<Cell> iterator = list.iterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            if (Arrays.equals(cell.getCoordinates(), tileCoordinatesToRemove))
                iterator.remove();
        }
    }

    private boolean isTileInList(List<Cell> list, int[] tileCoordinatesToSearch) {
        for (Cell tile : list) {
            if (Arrays.equals(tile.getCoordinates(), tileCoordinatesToSearch)) {
                return true;
            }
        }
        return false;
    }

    private Cell getTileFromList(List<Cell> list, int[] tileCoordinatesToSearch) {
        for (Cell tile : list) {
            if (Arrays.equals(tile.getCoordinates(), tileCoordinatesToSearch)) {
                return tile;
            }
        }
        return null;
    }

    private int getEstimatedCost(int[] fromCoordinates, int[] toCoordinates) {
        return Math.abs(toCoordinates[0] - fromCoordinates[0]) + Math.abs(toCoordinates[1] - fromCoordinates[1]);
    }

    private Cell getTileFromOpenListWithLowestScore() {
        Cell returnTile = new Cell(new int[]{});
        int score = openList.get(0).getScore();
        for (Cell tile : openList) {
            if (tile.getScore() <= score) {
                score = tile.getScore();
                returnTile = tile;
            }
        }
        return returnTile;
    }

    private void addNeighborIfValid(List<Cell> list, int x, int y, Cell tile) {
        try {
            if (maze.isCellEmpty(x, y)) {
                int[] coordinates = new int[]{x, y};
                Cell tileToAdd = new Cell(coordinates, tile.getCost() + 1,
                        getEstimatedCost(coordinates, goalTile.getCoordinates()), tile);
                list.add(tileToAdd);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }

    }

    private List<Cell> getListOfValidNeighbors(Cell tile) {
        List<Cell> returnList = new ArrayList<>();
        int row = tile.getCoordinates()[0];
        int col = tile.getCoordinates()[1];
        addNeighborIfValid(returnList, row - 1, col, tile);
        addNeighborIfValid(returnList, row + 1, col, tile);
        addNeighborIfValid(returnList, row, col - 1, tile);
        addNeighborIfValid(returnList, row, col + 1, tile);
        return returnList;
    }

    private void generateShortestRoute() {
        while (!openList.isEmpty()) {
            Cell currentTile = getTileFromOpenListWithLowestScore();
            closedList.add(currentTile);
            removeTileFromList(openList, currentTile.getCoordinates());

            if (isTileInList(closedList, goalTile.getCoordinates())) {
                break;
            }

            List<Cell> validNeighbors = getListOfValidNeighbors(currentTile);

            for (Cell neighbor : validNeighbors) {
                if (isTileInList(closedList, neighbor.getCoordinates())) {
                    continue;
                } else if (!isTileInList(openList, neighbor.getCoordinates())) {
                    openList.add(neighbor);
                } else {
                    if (currentTile.getCost() + 1 +
                            getEstimatedCost(neighbor.getCoordinates(),
                                    goalTile.getCoordinates())
                            < neighbor.getScore()) {
                        neighbor.setParent(currentTile);
                        neighbor.setScore(currentTile.getCost() + 1 +
                                getEstimatedCost(neighbor.getCoordinates(),
                                        goalTile.getCoordinates()));
                    }
                }
            }
        }
    }

    public int[][] getShortestPathCoordinates() {
        generateShortestRoute();
        int[][] returnList = new int[closedList.size()][2];
        int[] goalCoordinates = closedList.get(closedList.size() - 1).getCoordinates();
        int counter = 0;
        returnList[0][0] = goalCoordinates[0];
        returnList[0][1] = goalCoordinates[1];
        Cell lastListMember = getTileFromList(closedList, returnList[counter]);
        counter++;
        while (lastListMember.getParent() != null) {
            returnList[counter] = (lastListMember.getParent().getCoordinates());
            lastListMember = getTileFromList(closedList, returnList[counter]);
            counter++;
        }
        return returnList;
    }

    public Boolean isCoordinatePartOfShortestPath(int[] coordinates) {
        for (int[] i : getShortestPathCoordinates()) {
            if (i[0] == coordinates[0] && i[1] == coordinates[1]) {
                return true;
            }
        }
        return false;
    }

    public Cell getStartTile() {
        return startTile;
    }

    public Cell getGoalTile() {
        return goalTile;
    }
}
