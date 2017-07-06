package com.codecool.maze;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MazeGeneratorTest {

    @Test
    public void testConstructorInvalidRow(){
        assertThrows(IllegalArgumentException.class, () -> {
            Maze mazeGenerator = new Maze(8, 7);
        });
    }

    @Test
    public void testConstructorInvalidCol(){
        assertThrows(IllegalArgumentException.class, () -> {
            Maze mazeGenerator = new Maze(7, 8);
        });
    }

    @Test
    public void testConstructorInvalidRowAndInvalidCol(){
        assertThrows(IllegalArgumentException.class, () -> {
            Maze mazeGenerator = new Maze(8, 8);
        });
    }
}