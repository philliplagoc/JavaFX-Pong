package com.lagocp.sprites;

import com.lagocp.gameEngine.sprite.Sprite;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Paddle extends Sprite {
	private int points;
	private String name;

	private boolean moveUp;
	private boolean moveDown;

	private static final double PADDLE_SPEED = 2.5;

	public Paddle(double x, double y, double width, double height, String name, GraphicsContext gc) {
		super(x, y, width, height, gc);
		this.points = 0;
		this.name = name;
	}

	@Override
	public boolean didCollideWith(Sprite other) {
		Ball ball = (Ball) other;
		double ballCenterX = ball.getCenterX();
		double ballRadius = ball.getRadius();
		double ballCenterY = ball.getCenterY();

		double halfWidth = this.getHalfWidth();
		double halfHeight = this.getHalfHeight();
		double centerX = this.getCenterX();
		double centerY = this.getCenterY();

		if (getName().toLowerCase().equals("left")) {
			boolean hitXBounds = ballCenterX - ballRadius <= centerX + halfWidth
					&& ballCenterX - ballRadius >= centerX - halfWidth;
			boolean hitTopPartOfBall = ballCenterY - ballRadius <= centerY + halfHeight
					&& ballCenterY - ballRadius >= centerY - halfHeight;
			boolean hitBotPartOfBall = ballCenterY + ballRadius <= centerY + halfHeight
					&& ballCenterY + ballRadius >= centerY - halfHeight;

			return hitXBounds && (hitTopPartOfBall || hitBotPartOfBall);
		}

		if (getName().toLowerCase().equals("right")) {
			boolean hitXBounds = ballCenterX + ballRadius >= centerX - halfWidth
					&& ballCenterX + ballRadius <= centerX + halfWidth;

			boolean hitTopPartOfBall = ballCenterY - ballRadius <= centerY + halfHeight
					&& ballCenterY - ballRadius >= centerY - halfHeight;
			boolean hitBotPartOfBall = ballCenterY + ballRadius <= centerY + halfHeight
					&& ballCenterY + ballRadius >= centerY - halfHeight;

			return hitXBounds && (hitTopPartOfBall || hitBotPartOfBall);
		}
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.fillRect(getX(), getY(), getWidth(), getHeight());
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMoveUp() {
		return moveUp;
	}

	public void setMoveUp(boolean moveUp) {
		this.moveUp = moveUp;
	}

	public boolean isMoveDown() {
		return moveDown;
	}

	public void setMoveDown(boolean moveDown) {
		this.moveDown = moveDown;
	}

	public void moveUp(boolean movingUp) {
		this.setMoveUp(movingUp);
		setvY((isMoveUp() ? -PADDLE_SPEED : 0) + (isMoveDown() ? PADDLE_SPEED : 0));
	}

	public void moveDown(boolean movingDown) {
		this.setMoveDown(movingDown);
		setvY((isMoveUp() ? -PADDLE_SPEED : 0) + (isMoveDown() ? PADDLE_SPEED : 0));
	}

	@Override
	public boolean didCollideWithTopWall(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();
		return this.getY() <= bounds.getMinY() + 5;
	}

	@Override
	public boolean didCollideWithBotWall(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();

		return this.getY() >= bounds.getMaxY() - 105;
	}

	@Override
	public boolean didCollideWithLeftWall(Canvas canvas) {
		return false;
	}

	@Override
	public boolean didCollideWithRightWall(Canvas canvas) {
		return false;
	}

}
