package com.lagocp.main;

import java.util.Random;

import com.lagocp.gameEngine.sprites.Ball;
import com.lagocp.gameEngine.sprites.Paddle;
import com.lagocp.ui.UIPane;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Pong2App extends Application {
	private static final double CANVAS_WIDTH = 950;
	private static final double CANVAS_HEIGHT = 500;

	private static final double ELAPSED_TIME_SPEED = 2.5;

	Ball ball;
	private static final double BALL_X = CANVAS_WIDTH / 2;
	private static final double BALL_Y = CANVAS_HEIGHT / 2;
	private static final Color BALL_COLOR = Color.BLACK;
	private static final double BALL_CIRCUMFERENCE = 25;
	private static final double BALL_SPEED = 1.8;

	Paddle leftPaddle;
	private static final double LEFT_PADDLE_X = 70;
	private static final Color LEFT_PADDLE_COLOR = Color.ORANGE;

	Paddle rightPaddle;
	private static final double RIGHT_PADDLE_X = CANVAS_WIDTH - 70;
	private static final Color RIGHT_PADDLE_COLOR = Color.BLUE;

	private static final double PADDLE_Y = CANVAS_HEIGHT / 2.5;
	private static final double PADDLE_WIDTH = 10;
	private static final double PADDLE_HEIGHT = 100;

	private boolean pointWasMade = false;

	Canvas canvas;
	Bounds canvasBounds;
	GraphicsContext gc;

	// GUI responsible for creating UI
	private Label leftPaddleScoreLabel = new Label();
	private int leftPaddleScore;

	private Label rightPaddleScoreLabel = new Label();
	private int rightPaddleScore;

	private UIPane uiPane;

	/*
	 * private VBox stats; private Text ballInfo; private Text leftPaddleInfo;
	 * private Text rightPaddleInfo;
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("styles.css");
		primaryStage.setScene(scene);

		canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		canvasBounds = canvas.getBoundsInLocal();

		gc = canvas.getGraphicsContext2D();

		/* Set-up UI */
		setUpUI(root);

		/* Spawn sprites */
		spawn(gc);

		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(new KeyPressedHandler());
		canvas.setOnKeyReleased(new KeyReleasedHandler());

		new AnimationTimer() {

			@Override
			public void handle(long currentTime) {

				/*
				 * ballInfo.setText("Ball (" + ball.getX() + ", " + ball.getY() + ")");
				 * leftPaddleInfo.setText("Left Paddle (" + leftPaddle.getX() + ", " +
				 * leftPaddle.getY() + ")"); rightPaddleInfo.setText("Right Paddle (" +
				 * rightPaddle.getX() + ", " + rightPaddle.getY() + ")");
				 */

				// Give point and reset ball.
				if (ball.didCollideWithLeftWall(canvas)) {
					givePoint(rightPaddle);
					rightPaddleScore = rightPaddle.getPoints();
					rightPaddleScoreLabel.setText("Player 2 Score: " + rightPaddleScore);
					reset();
					uiPane.givePointToPlayer(rightPaddle);
				}

				if (ball.didCollideWithRightWall(canvas)) {
					givePoint(leftPaddle);
					leftPaddleScore = leftPaddle.getPoints();
					leftPaddleScoreLabel.setText("Player 1 Score: " + leftPaddleScore);
					reset();
					uiPane.givePointToPlayer(leftPaddle);
				}

				// Handle bouncing off of top and bottom walls
				ball.didCollideWithWalls(canvas);

				// Keep paddle in screen bounds
				if (leftPaddle.getY() <= canvasBounds.getMinY() + 5)
					leftPaddle.setY(canvasBounds.getMinY() + 5);
				if (leftPaddle.getY() >= canvasBounds.getMaxY() - 105)
					leftPaddle.setY(canvasBounds.getMaxY() - 105);

				if (rightPaddle.getY() <= canvasBounds.getMinY() + 5)
					rightPaddle.setY(canvasBounds.getMinY() + 5);
				if (rightPaddle.getY() >= canvasBounds.getMaxY() - 105)
					rightPaddle.setY(canvasBounds.getMaxY() - 105);

				if (leftPaddle.didCollideWith(ball)) {
					ball.setvX(-ball.getvX());
					ball.setX(ball.getX() + ball.getvX());
				}

				if (rightPaddle.didCollideWith(ball)) {
					ball.setvX(-ball.getvX());
					ball.setX(ball.getX() + ball.getvX());
				}

				// Update and render
				ball.update(ELAPSED_TIME_SPEED);
				leftPaddle.update(ELAPSED_TIME_SPEED);
				rightPaddle.update(ELAPSED_TIME_SPEED);

				gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

				gc.setFill(BALL_COLOR);
				ball.render(gc);

				gc.setFill(LEFT_PADDLE_COLOR);
				leftPaddle.render(gc);

				gc.setFill(RIGHT_PADDLE_COLOR);
				rightPaddle.render(gc);

			}

		}.start();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Gives a point to the specified paddle. Updates the paddle's point property.
	 * 
	 * @param paddle
	 *            The paddle to give the point to.
	 */
	private void givePoint(Paddle paddle) {
		paddle.setPoints(paddle.getPoints() + 1);
		pointWasMade = true;
	}

	/**
	 * Resets the game. Places the ball back into the center. Gives ball random
	 * positive and negative velocities for x and y.
	 */
	private void reset() {
		ball.setX(BALL_X);
		ball.setY(BALL_Y);
		ball.setvX(0);
		ball.setvY(0);
	}

	/**
	 * Gives the specified ball a random velocity in both the x and y directions.
	 */
	private void giveBallRandomVelocity() {
		Random rand = new Random();
		ball.setvX(BALL_SPEED * (rand.nextBoolean() ? -1 : 1));
		ball.setvY(BALL_SPEED * (rand.nextBoolean() ? -1 : 1));
	}

	/**
	 * Spawns the sprites.
	 * 
	 * @param gc
	 *            The GraphicsContext to draw on.
	 */
	private void spawn(GraphicsContext gc) {
		ball = new Ball(BALL_X, BALL_Y, BALL_CIRCUMFERENCE, BALL_CIRCUMFERENCE, gc);
		pointWasMade = true;

		leftPaddle = new Paddle(LEFT_PADDLE_X, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT, "Left", gc);

		rightPaddle = new Paddle(RIGHT_PADDLE_X, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT, "Right", gc);
	}

	/**
	 * Sets up the UI on the given root. Adds the canvas to the background, adds the
	 * background to the uiPane's center, adds the uiPane to the basePane, and
	 * finally the basePane to the root.
	 * 
	 * @param root
	 *            The root to set up the UI on.
	 */
	private void setUpUI(Group root) {
		uiPane = new UIPane(root, canvas, leftPaddleScoreLabel, leftPaddleScore, rightPaddleScoreLabel,
				rightPaddleScore);
	}

	/**
	 * Handles what happens when a key is pressed. Depending on what key is pressed,
	 * the paddle will move up or down.
	 * 
	 * @author Phillip
	 *
	 */
	private class KeyPressedHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			switch (event.getCode()) {
			case W:
				leftPaddle.moveUp(true);
				break;
			case S:
				leftPaddle.moveDown(true);
				break;
			case UP:
				rightPaddle.moveUp(true);
				break;
			case DOWN:
				rightPaddle.moveDown(true);
				break;
			case SPACE:
				if (pointWasMade) {
					uiPane.removePointNotification();
					giveBallRandomVelocity();
					pointWasMade = false;
				}
				break;
			default:
				break;
			}

		}

	}

	/**
	 * Handles what happens when a key is released. Depending on what key is
	 * released, the paddle will stop moving.
	 * 
	 * @author Phillip
	 *
	 */
	private class KeyReleasedHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			switch (event.getCode()) {
			case W:
				leftPaddle.moveUp(false);
				break;
			case S:
				leftPaddle.moveDown(false);
				break;
			case UP:
				rightPaddle.moveUp(false);
				break;
			case DOWN:
				rightPaddle.moveDown(false);
				break;
			default:
				break;
			}

		}

	}

}
