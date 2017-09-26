package com.codecool.maze;

import java.util.Arrays;

public class MazeConsolePrinter {

    public static final String yellow = "\u001B[33m";
    public static final String blue = "\u001B[36m";
    public static final String reset = "\033[0m";

    public static void main(String[] args) {
        Maze mazeGenerator = new Maze(21, 31);
        PathFinder maze = mazeGenerator.generate();
        MazeConsolePrinter.printWithoutSolution(maze);
        MazeConsolePrinter.printWithSolution(maze);
    }

    private static boolean isListContainingElement(int[][] list, int[] element) {
        for (int i = 0; i < list.length; i++) {
            if (Arrays.equals(list[i], element)) {
                return true;
            }
        }
        return false;
    }

    static void printWithoutSolution(PathFinder maze) {
        System.out.println();
        for (int[] row : maze.getMaze()) {
            for (int cell : row) {
                if (cell == 1) {
                    System.out.print("x ");
                } else if (cell == 0) {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    static void printWithSolution(PathFinder maze) {

        int[][] shortestPathCells = maze.getShortestPathCoordinates();

        System.out.println();
        for (int row = 0; row < maze.getMaze().length; row++) {
            for (int cell = 0; cell < maze.getMaze()[0].length; cell++) {
                if (maze.getMaze()[row][cell] == 1) {
                    System.out.print(blue + "x " + reset);
                } else if (maze.getMaze()[row][cell] == 0) {
                    if (isListContainingElement(shortestPathCells, new int[]{row, cell})) {
                        System.out.print(yellow + "o " + reset);
                    } else {
                        System.out.print("  ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
