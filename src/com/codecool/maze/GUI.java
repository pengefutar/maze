package com.codecool.maze;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class GUI extends Application {
    int width, height;
    int rows = 31;
    int cols = 41;
    Maze maze = new Maze(rows, cols);
    PathFinder pathFinder = maze.generate();
    GridPane mazePane = new GridPane();
    String wallColor = "-fx-background-color: #1c1c1c;";
    String pathColor = "-fx-background-color: #cbcbcb;";
    String cheatColor = "-fx-background-color: #d169c5;";
    ToggleGroup difficultyGroup;
    CheckBox cheat;

    public static void main(String[] args) {
        launch(args);
    }

    private static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private void drawGates(Boolean containsCheat) {
        getNodeFromGridPane(mazePane, pathFinder.getStartTile().getCoordinates()[1] - 1, 0).setStyle(wallColor);
        getNodeFromGridPane(mazePane, pathFinder.getStartTile().getCoordinates()[1] + 1, 0).setStyle(wallColor);
        getNodeFromGridPane(mazePane, pathFinder.getStartTile().getCoordinates()[1] - 1, 1).setStyle(wallColor);
        getNodeFromGridPane(mazePane, pathFinder.getStartTile().getCoordinates()[1] + 1, 1).setStyle(wallColor);
        getNodeFromGridPane(mazePane, pathFinder.getGoalTile().getCoordinates()[1] + 1, rows + 2).setStyle(wallColor);
        getNodeFromGridPane(mazePane, pathFinder.getGoalTile().getCoordinates()[1] - 1, rows + 2).setStyle(wallColor);
        getNodeFromGridPane(mazePane, pathFinder.getGoalTile().getCoordinates()[1] + 1, rows + 3).setStyle(wallColor);
        getNodeFromGridPane(mazePane, pathFinder.getGoalTile().getCoordinates()[1] - 1, rows + 3).setStyle(wallColor);
        if (containsCheat) {
            getNodeFromGridPane(mazePane, pathFinder.getStartTile().getCoordinates()[1], 0).setStyle(cheatColor);
            getNodeFromGridPane(mazePane, pathFinder.getStartTile().getCoordinates()[1], 1).setStyle(cheatColor);
            getNodeFromGridPane(mazePane, pathFinder.getGoalTile().getCoordinates()[1], rows + 2).setStyle(cheatColor);
            getNodeFromGridPane(mazePane, pathFinder.getGoalTile().getCoordinates()[1], rows + 3).setStyle(cheatColor);
        } else {
            getNodeFromGridPane(mazePane, pathFinder.getStartTile().getCoordinates()[1], 0).setStyle(pathColor);
            getNodeFromGridPane(mazePane, pathFinder.getStartTile().getCoordinates()[1], 1).setStyle(pathColor);
            getNodeFromGridPane(mazePane, pathFinder.getGoalTile().getCoordinates()[1], rows + 2).setStyle(pathColor);
            getNodeFromGridPane(mazePane, pathFinder.getGoalTile().getCoordinates()[1], rows + 3).setStyle(pathColor);
        }
    }

    private void drawMaze(Boolean containsCheat) {

        mazePane.getChildren().clear();

        double gridSize = ((width / 4 * 3) / 100 * 90) / cols;
        for (int x = 0; x < rows + 4; x++) {
            for (int y = 0; y < cols; y++) {
                Pane pane = new Pane();
                pane.setPrefWidth(gridSize);
                pane.setPrefHeight(gridSize);
                if (1 < x & x < rows + 2) {
                    pane.setStyle(pathFinder.getMazeMap()[x - 2][y] == 1 ? wallColor : pathColor);
                    if (containsCheat && pathFinder.isCoordinatePartOfShortestPath(new int[]{x - 2, y})) {
                        pane.setStyle(cheatColor);
                    }
                }
                mazePane.add(pane, y, x);
            }
        }
        drawGates(containsCheat);
    }

    private void exitPopup(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to exit Maze Sprint?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            alert.close();
            primaryStage.close();
        } else {
            alert.close();
        }
    }

    private void optionsPopup(Stage primaryStage) {
        Stage optionsStage = new Stage();
        VBox vBox = new VBox(20);
        Scene scene = new Scene(vBox);
        Label difficultyLabel = new Label("Difficulty");
        difficultyGroup = new ToggleGroup();
        RadioButton easy = new RadioButton("Easy");
        easy.setToggleGroup(difficultyGroup);
        RadioButton medium = new RadioButton("Medium");
        medium.setToggleGroup(difficultyGroup);
        medium.setSelected(true);
        RadioButton hard = new RadioButton("Hard");
        hard.setToggleGroup(difficultyGroup);
        cheat = new CheckBox("Cheat");
        cheat.setSelected(false);
        vBox.getChildren().addAll(difficultyLabel, easy, medium, hard, cheat);
        optionsStage.setScene(scene);
        hard.setToggleGroup(difficultyGroup);
        optionsStage.initOwner(primaryStage);
        optionsStage.initModality(Modality.APPLICATION_MODAL);
        optionsStage.showAndWait();
//        optionsStage.setOnCloseRequest(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                setRowsAndCols();
//                drawMaze(cheat.isSelected());
//            });
    }

    private void setRowsAndCols() {
        switch (difficultyGroup.getSelectedToggle().toString()) {
            case "Easy":
                rows = 21;
                cols = 31;
                break;
            case "Medium":
                rows = 31;
                cols = 41;
                break;
            case "Hard":
                rows = 41;
                cols = 51;
                break;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Maze Sprint");
        width = (int) Screen.getPrimary().getBounds().getWidth();
        height = (int) Screen.getPrimary().getBounds().getHeight();
        HBox root = new HBox();

        drawMaze(true);
        GridPane game = new GridPane();
        game.setStyle("-fx-background-color: #7e7ec8;");
        game.setAlignment(Pos.CENTER);
        game.setPrefSize(width / 5 * 4, height);
        game.getChildren().add(mazePane);

        VBox buttons = new VBox(25);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefSize(width / 5, height);
        buttons.setStyle("-fx-background-color: #4d4d87;");
        Button generateNew = new Button("New Maze");
        generateNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                maze = new Maze(rows, cols);
                pathFinder = maze.generate();
                drawMaze(true);
            }
        });
        Button options = new Button("Options");
        options.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                optionsPopup(primaryStage);
            }
        });
        Button exit = new Button("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitPopup(primaryStage);
            }
        });
        buttons.getChildren().addAll(generateNew, options, exit);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.gray(0, 0.8039));
        VBox title = new VBox();
        Label titleText = new Label("Maze Sprint");
        titleText.setEffect(ds);
        title.setPrefSize(width / 5, height / 6);
        title.setMaxSize(width / 5, height / 6);
        title.setAlignment(Pos.CENTER);
        title.getChildren().addAll(titleText);

        StackPane menu = new StackPane();
        menu.setPrefSize(width / 5, height);
        menu.getChildren().addAll(buttons, title);
        StackPane.setAlignment(title, Pos.TOP_LEFT);

        root.getChildren().addAll(menu, game);
        root.setStyle("-fx-background-color: #7e7ec8;");

        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add("Style.css");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.setFullScreenExitHint("");
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.show();
    }
}
