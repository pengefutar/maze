package com.codecool.maze;

public class CaptureThePrincess {

    public static void main(String[] args) {
        Maze mazeGenerator = new Maze(21, 31);
        PathFinder maze = mazeGenerator.generate();
        MazeConsolePrinter.printWithoutSolution(maze);
        MazeConsolePrinter.printWithSolution(maze);
    }
}
