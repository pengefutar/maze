package com.codecool.maze;

import java.util.*;

public class Maze {

    private int totalRows;
    private int totalCols;
    private int[][] data;

    private int[] startCoordinates;
    private int[] goalCoordinates;

    private int rootRow;
    private int rootCol;
    private int numOfWallsToBreak;
    private Random randomizer = new Random();


    public Maze(int totalRows, int totalCols) {
        if (totalRows < 5 || totalCols < 5) {
            throw new IllegalArgumentException("num of rows and cols must be at least 5");
        }
        if (totalRows % 2 == 0 || totalCols % 2 == 0) {
            throw new IllegalArgumentException("num of rows and cols must be odd");
        }
        this.totalRows = totalRows;
        this.totalCols = totalCols;
        data = new int[totalRows][totalCols];
        startCoordinates = new int[2];
        goalCoordinates = new int[2];
        numOfWallsToBreak = Math.max(totalRows, totalCols) / 5;
    }

    private void fillTableWithWalls() {
        for (int x = 0; x < totalRows; x++) {
            for (int y = 0; y < totalCols; y++) {
                data[x][y] = 1;
            }
        }
    }

    private void chooseRoot() {
        rootRow = randomizer.nextInt(totalRows / 2) * 2 + 1;
        rootCol = randomizer.nextInt(totalCols / 2) * 2 + 1;
    }

    private int chooseGateLocation() {
        return randomizer.nextInt(Math.min(totalRows, totalCols) - 2) + 1;
    }

    private void openGates() {
//      boolean areSidesEastAndWest = totalCols < totalRows;
        boolean areSidesEastAndWest = true;
        int location = chooseGateLocation();
        int location2 = chooseGateLocation();

        if (areSidesEastAndWest) {
            while (data[1][location] == 1) {
                location = chooseGateLocation();
            }
            data[0][location] = 0;
            startCoordinates[0] = 0;
            startCoordinates[1] = location;
            while (data[totalRows - 2][location2] == 1) {
                location2 = chooseGateLocation();
            }
            data[totalRows - 1][location2] = 0;
            goalCoordinates[0] = totalRows - 1;
            goalCoordinates[1] = location2;
        } else {
            while (data[location][1] == 1) {
                location = chooseGateLocation();
            }
            data[location][0] = 0;
            startCoordinates[0] = location;
            startCoordinates[1] = 0;
            while (data[location2][totalCols - 2] == 1) {
                location2 = chooseGateLocation();
            }
            data[location2][totalCols - 1] = 0;
            goalCoordinates[0] = location2;
            goalCoordinates[1] = totalCols - 1;
        }
    }

    private int findNumOfNeighborWalls(int row, int col) {
        return data[row + 1][col] + data[row - 1][col] + data[row][col + 1] + data[row][col - 1];
    }

    private boolean isBreakable(int row, int col) {
        if (data[row][col] == 0) {
            return false;
        }
        if (findNumOfNeighborWalls(row, col) != 2) {
            return false;
        }
        if (data[row + 1][col] == 1 && data[row - 1][col] == 1 ||
                data[row][col + 1] == 1 && data[row][col - 1] == 1) {
            return true;
        }
        return false;
    }

    private void breakWalls() {
        List<List<Integer>> validCoordinates = new ArrayList<>();
        int counter = 0;
        for (int x = 1; x < data.length - 1; x++) {
            for (int y = 1; y < data[0].length - 1; y++) {
                if (data[x][y] == 1) {
                    if (isBreakable(x, y)) {
                        List<Integer> list = new ArrayList<>();
                        validCoordinates.add(list);
                        validCoordinates.get(counter).add(x);
                        validCoordinates.get(counter).add(y);
                        counter++;
                    }
                }
            }
        }
        Collections.shuffle(validCoordinates);
        counter = 0;
        while (counter != numOfWallsToBreak) {
            int randomWall = randomizer.nextInt(validCoordinates.size());
            int row = validCoordinates.get(randomWall).get(0);
            int col = validCoordinates.get(randomWall).get(1);
            if (isBreakable(row, col)) {
                data[row][col] = 0;
                counter++;
            }
            validCoordinates.remove(randomWall);
        }
    }

    public PathFinder generate() {
        fillTableWithWalls();
        chooseRoot();
        data[rootRow][rootCol] = 0;
        generateBaseMaze(rootRow, rootCol);
        openGates();
        breakWalls();
        return new PathFinder(this);
    }

    private void generateBaseMaze(int row, int col) {
        List<Integer> directions = new ArrayList<>();
        for (int x = 0; x < 4; x++) {
            directions.add(x);
        }
        Collections.shuffle(directions);
        for (int direction : directions) {
            switch (direction) {
                case 0: //up
                    try {
                        if (data[row - 2][col] != 0) {
                            data[row - 1][col] = 0;
                            data[row - 2][col] = 0;
                            generateBaseMaze(row - 2, col);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    break;
                case 1: //right
                    try {
                        if (data[row][col + 2] != 0) {
                            data[row][col + 1] = 0;
                            data[row][col + 2] = 0;
                            generateBaseMaze(row, col + 2);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    break;
                case 2: //down
                    try {
                        int num = data[row + 2][col];
                        if (data[row + 2][col] != 0) {
                            data[row + 1][col] = 0;
                            data[row + 2][col] = 0;
                            generateBaseMaze(row + 2, col);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    break;
                case 3: //left
                    try {
                        if (data[row][col - 2] != 0) {
                            data[row][col - 1] = 0;
                            data[row][col - 2] = 0;
                            generateBaseMaze(row, col - 2);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    break;
            }
        }
    }

    public boolean isCellEmpty(int x, int y) {
        return data[x][y] == 0;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalCols() {
        return totalCols;
    }

    public int[][] getData() {
        return data;
    }

    public int[] getStartCoordinates() {
        return startCoordinates;
    }

    public int[] getGoalCoordinates() {
        return goalCoordinates;
    }
}
