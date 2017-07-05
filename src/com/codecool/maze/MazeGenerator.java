package com.codecool.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeGenerator {

    int row;
    int col;
    int rootRow;
    int rootCol;
    int numOfWallsToBreak;
    int[] startCoordinates;
    int[] goalCoordinates;
    int[][] maze;

    public MazeGenerator(int row, int col) {
        if(row < 5 || col < 5){
            throw new IllegalArgumentException("num of rows and cols must be at least 5");
        }
        else if(row % 2 == 0 || col % 2 == 0){
            throw new IllegalArgumentException("num of rows and cols must be odd");
        }
        this.row = row;
        this.col = col;
        this.maze = new int[row][col];
        if(row<col){
            numOfWallsToBreak = col/5;
        }
        else{
            numOfWallsToBreak = row/5;
        }
        startCoordinates = new int[2];
        goalCoordinates = new int[2];
    }

    private void empty(){
        for(int x = 0; x < row; x++){
            for(int y = 0; y < col; y++){
                maze[x][y] = 1;
            }
        }
    }

    private void chooseRandomRoot(){
        Random randomizer = new Random();
        while(rootRow % 2 == 0){
            rootRow = randomizer.nextInt(row);
        }
        while(rootCol % 2 == 0){
            rootCol = randomizer.nextInt(col);
        }
    }

    private int chooseDirection(){
        int direction = 4;
        if(col != row){
            if(col<row){
                direction = 0;
            }
            if(row<col){
                direction = 1;
            }
        }
        else{
            Random randomizer = new Random();
            direction = randomizer.nextInt(4);
        }
        return direction;
    }

    private int choosePlace(int direction){
        Random randomizer = new Random();
        int place;
        if (direction == 1 || direction == 3){
            place = randomizer.nextInt(row-2)+1;
        }
        else {
            place = randomizer.nextInt(col-2)+1;
        }
        return place;
    }

    private void openGates(){
        int direction = chooseDirection();
        int place = choosePlace(direction);
        int place2 = choosePlace(direction);
        switch (direction){
            case 0: //up
            case 2: //down
                while(maze[1][place] == 1){
                    place = choosePlace(direction);
                }
                maze[0][place] = 0;
                startCoordinates[0] = 0;
                startCoordinates[1] = place;
                while(maze[row-2][place2] == 1){
                    place2 = choosePlace(direction);
                }
                maze[row-1][place2] = 0;
                goalCoordinates[0] = row-1;
                goalCoordinates[1] = place2;
                break;
            case 1: //right
            case 3: //left
                while(maze[place][1] == 1){
                    place = choosePlace(direction);
                }
                maze[place][0] = 0;
                startCoordinates[0] = place;
                startCoordinates[1] = 0;
                while(maze[place2][col-2] == 1){
                    place2 = choosePlace(direction);
                }
                maze[place2][col-1] = 0;
                goalCoordinates[0] = place2;
                goalCoordinates[1] = col-1;
                break;
        }
    }

    private int findNumOfNeighborWalls(int row, int col){
        int numOfNeighborWalls = 0;
        if(maze[row+1][col] == 1){
            numOfNeighborWalls++;
        }

        if(maze[row-1][col] == 1){
            numOfNeighborWalls++;
        }

        if(maze[row][col+1] == 1){
            numOfNeighborWalls++;
        }

        if(maze[row][col-1] == 1){
            numOfNeighborWalls++;
        }

        return numOfNeighborWalls;
    }

    private boolean areNeighborWallsValid(int row, int col){
        if(findNumOfNeighborWalls(row, col) != 2){
            return false;
        }
        if(maze[row+1][col] == 1 && maze[row-1][col] == 1){
            return true;
        }
        else if(maze[row][col+1] == 1 && maze[row][col-1] == 1){
            return true;
        }
        return false;
    }

    private void breakWalls(){
        ArrayList<ArrayList<Integer>> validCoordinates = new ArrayList<ArrayList<Integer>>();
        int counter = 0;
        for(int x = 1; x < maze.length-1; x++){
            for(int y = 1; y < maze[0].length-1; y++){
                if(maze[x][y] == 1){
                    if(areNeighborWallsValid(x, y)){
                        ArrayList<Integer> list = new ArrayList<>();
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
        while(counter != numOfWallsToBreak){
            for(int x = 0; x < validCoordinates.size(); x++){
                if(areNeighborWallsValid(validCoordinates.get(x).get(0),
                        validCoordinates.get(x).get(1))){
                    maze[validCoordinates.get(x).get(0)][validCoordinates.get(x).get(1)] = 0;
                    validCoordinates.remove(x);
                    counter++;
                    break;
                }
            }
        }
    }


    public void generate(){
        empty();
        chooseRandomRoot();
        maze[rootRow][rootCol] = 0;
        generateBaseMaze(rootRow, rootCol);
        openGates();
        breakWalls();
    }

    private void generateBaseMaze(int row, int col){
        List<Integer> directions = new ArrayList<>();
        for (int x = 0; x < 4; x++){
            directions.add(x);
        }
        Collections.shuffle(directions);
        for (int direction : directions){
            switch (direction){
                case 0: //up
                    try{
                        int num = maze[row-2][col];
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        continue;
                    }
                    if (maze[row-2][col] != 0){
                        maze[row-1][col] = 0;
                        maze[row-2][col] = 0;
                        generateBaseMaze(row-2, col);
                    }
                    break;
                case 1: //right
                    try{
                        int num = maze[row][col+2];
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        continue;
                    }
                    if (maze[row][col+2] != 0){
                        maze[row][col+1] = 0;
                        maze[row][col+2] = 0;
                        generateBaseMaze(row, col+2);
                    }
                    break;
                case 2: //down
                    try{
                        int num = maze[row+2][col];
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        continue;
                    }
                    if (maze[row+2][col] != 0){
                        maze[row+1][col] = 0;
                        maze[row+2][col] = 0;
                        generateBaseMaze(row+2, col);
                    }
                    break;
                case 3: //left
                    try{
                        int num = maze[row][col-2];
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        continue;
                    }
                    if (maze[row][col-2] != 0){
                        maze[row][col-1] = 0;
                        maze[row][col-2] = 0;
                        generateBaseMaze(row, col-2);
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        MazeGenerator mazeGenerator = new MazeGenerator(21, 31);
        mazeGenerator.generate();
        for (int[] row : mazeGenerator.maze){
            for(int cell : row){
                if (cell == 1){
                    System.out.print("x ");
                }
                else if (cell == 0){
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

        MazeSearch mazeSearch = new MazeSearch(mazeGenerator.maze,
                mazeGenerator.startCoordinates, mazeGenerator.goalCoordinates);
        for(SearchTile tile : mazeSearch.getShortestPath()){
            mazeGenerator.maze[tile.getCoordinates()[0]][tile.getCoordinates()[1]] = 2;
        }

        System.out.println();
        System.out.println();
        for (int[] row : mazeGenerator.maze){
            for(int cell : row){
                if (cell == 1){
                    System.out.print("x ");
                }
                else if (cell == 0){
                    System.out.print("  ");
                }
                else if (cell == 2){
                    System.out.print("o ");
                }
            }
            System.out.println();
        }

    }
}
