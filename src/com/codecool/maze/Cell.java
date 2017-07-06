package com.codecool.maze;

public class Cell {

    private int[] coordinates;
    private int cost;
    private int estimatedCost;
    private int score;
    private Cell parent;

    public Cell(int[] coordinates){
        this.coordinates = coordinates;
    }

    public Cell(int[] coordinates, int cost, int estimatedCost, Cell parent) {
        this.coordinates = coordinates;
        this.cost = cost;
        this.estimatedCost = estimatedCost;
        this.parent = parent;
        score = cost + estimatedCost;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(int estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

}
