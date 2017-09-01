package com.lagocp.ui;

import com.lagocp.sprites.Paddle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * This class will have the UI for the main class. It will hold a score, screen
 * boundaries, and instructions.
 * 
 * @author Phillip
 *
 */
public class UIPane extends Pane {
	private StackPane basePane;
	private StackPane bgPane;
	private BorderPane uiPane;
	Label pointGivenToPlayer;

	public UIPane(Group root, Canvas canvas, Label leftPaddleScoreLabel, int leftPaddleScore,
			Label rightPaddleScoreLabel, int rightPaddleScore) {
		basePane = new StackPane();

		bgPane = new StackPane();
		bgPane.setId("bg");

		uiPane = new BorderPane(bgPane);
		
		pointGivenToPlayer = new Label();

		bgPane.getChildren().add(canvas);
		basePane.getChildren().add(uiPane);
		root.getChildren().add(basePane);

		leftPaddleScoreLabel.setText("Player 1 Score: " + leftPaddleScore);
		leftPaddleScore = 0;

		rightPaddleScoreLabel.setText("Player 2 Score: " + rightPaddleScore);
		rightPaddleScore = 0;

		HBox scoreHolder = new HBox(100, leftPaddleScoreLabel, rightPaddleScoreLabel);
		scoreHolder.setAlignment(Pos.CENTER);

		BorderPane.setAlignment(scoreHolder, Pos.CENTER);
		BorderPane.setMargin(scoreHolder, new Insets(30, 30, 0, 30));
		uiPane.setTop(scoreHolder);

		Text instructions = new Text();
		instructions.setText("Player 1 Controls: W to move up, S to move down"
				+ "\nPlayer 2 Controls: Up Arrow key to move up, Down Arrow key to move down"
				+ "\nOnce a Player has made a point, press SPACEBAR to serve ball");

		BorderPane.setAlignment(instructions, Pos.CENTER);
		BorderPane.setMargin(instructions, new Insets(30));
		uiPane.setBottom(instructions);

		/*
		 * ballInfo = new Text(); leftPaddleInfo = new Text(); rightPaddleInfo = new
		 * Text(); stats = new VBox(20, instructions, leftPaddleInfo, ballInfo,
		 * rightPaddleInfo);
		 * 
		 * BorderPane.setAlignment(stats, Pos.CENTER_RIGHT); BorderPane.setMargin(stats,
		 * new Insets(30, 0, 0, 30)); uiPane.setBottom(stats);
		 */
	}

	/**
	 * Gives a UI notification that a point was given to a player.
	 * 
	 * @param paddle
	 *            The Paddle to give points to. Depending on which paddle got the
	 *            point, the label will change.
	 */
	public void givePointToPlayer(Paddle paddle) {
		if(paddle.getName().toLowerCase().equals("left"))
			pointGivenToPlayer.setText("Player One got a point!\nPress SPACE to serve ball");
		else if(paddle.getName().toLowerCase().equals("right"))
			pointGivenToPlayer.setText("Player Two got a point!\nPress SPACE to serve ball");
		
		BorderPane.setAlignment(pointGivenToPlayer, Pos.CENTER);
		BorderPane.setMargin(pointGivenToPlayer, new Insets(30));
		uiPane.setBottom(pointGivenToPlayer);
	}

	/**
	 * Removes the point notification from the UI.
	 */
	public void removePointNotification() {
		uiPane.getChildren().remove(pointGivenToPlayer);
	}
}
