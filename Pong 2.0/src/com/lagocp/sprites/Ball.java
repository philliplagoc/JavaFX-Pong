package com.lagocp.sprites;

import com.lagocp.sprite.Sprite;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Ball extends Sprite {
	private static final double MAX_SPEED = 3;

	public Ball(double x, double y, double width, double height, GraphicsContext gc) {
		super(x, y, width, height, gc);
	}

	@Override
	public boolean didCollideWith(Sprite other) {
		Paddle paddle = (Paddle) other;
		double paddleCenterX = paddle.getCenterX();
		double paddleHalfWidth = paddle.getHalfWidth();
		double paddleCenterY = paddle.getCenterY();

		double radius = this.getRadius();
		double centerX = this.getCenterX();
		double centerY = this.getCenterY();

		double dx = centerX - paddleCenterX;
		double dy = centerY - paddleCenterY;

		// System.out.println(dx + " " + dy);

		double dist = Math.sqrt((dx * dx) + (dy * dy));
		double minDist = paddleHalfWidth + radius;

		// If true, then collided
		return dist < minDist;
	}

	public double getRadius() {
		return this.getHalfWidth();
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.fillOval(getX(), getY(), getWidth(), getHeight());
	}

	/**
	 * Will only handle top and bottom collisions.
	 */
	@Override
	public boolean didCollideWithWalls(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();
		
		boolean atTopWall = this.getY() <= bounds.getMinY() + getHeight() - 20;
		boolean atBotWall = this.getY() >= bounds.getMaxY() - (2 * this.getRadius());

		if (atTopWall || atBotWall) {
			this.setvY(getvY() * -1);
			return true;
		}

		return false;
	}
	
	public boolean didCollideWithLeftWall(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();

		return this.getCenterX() + this.getRadius() <= bounds.getMinX();
	}
	
	public boolean didCollideWithRightWall(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();

		return this.getX() >= bounds.getMaxX();
	}
	
	@Override
	public void update(double time) {
		this.x += this.getvX() * time;
		this.y += this.getvY() * time;
		
		this.setCenterX(this.getX() + this.getRadius());
		this.setCenterY(this.getY() + this.getRadius());
	}

}
