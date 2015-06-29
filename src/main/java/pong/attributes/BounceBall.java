package pong.attributes;

import java.awt.Dimension;
import java.awt.Point;

import jalse.attributes.AttributeEvent;
import jalse.attributes.AttributeListener;
import jalse.entities.Entity;
import pong.entities.Ball;
import pong.entities.Paddle;
import pong.entities.Table;

public class BounceBall implements AttributeListener<Point> {

    @Override
    public void attributeAdded(final AttributeEvent<Point> event) {
	// Ball data
	final Ball ball = ((Entity) event.getContainer()).asType(Ball.class);
	final Point ballPos = event.getValue();
	final Dimension ballSize = ball.getSize();
	final Point ballMoveDelta = ball.getMoveDelta();

	// Table data
	final Table table = ((Entity) ball.getContainer()).asType(Table.class);
	final Dimension tableSize = table.getSize();

	// Off top or bottom
	if (ballPos.y == 0 || ballPos.y + ballSize.height == tableSize.height) {
	    // Inverse ball Y velocity
	    ball.setMoveDelta(new Point(ballMoveDelta.x, -ballMoveDelta.y));
	    return;
	}

	// Hit left paddle
	final Paddle left = table.getLeftPaddle();
	final double leftFront = left.getSize().width;
	if (ballPos.x <= leftFront && inPaddleY(left, ballPos, ballSize)) {
	    // Inverse and speed up X velocity
	    final int xMoveDelta = Math.min(ball.getMaxSpeed(), -ballMoveDelta.x + ball.getSpeedIncrement());
	    // Set new move delta with new X and Y
	    ball.setMoveDelta(new Point(xMoveDelta, getYCausedByPaddle(left, ball)));
	    // Reset to in front of paddle
	    ballPos.setLocation(leftFront, ballPos.y);
	    return;
	}

	// Hit right paddle
	final Paddle right = table.getRightPaddle();
	final double rightFront = tableSize.width - right.getSize().width;
	if (ballPos.x + ballSize.width >= rightFront && inPaddleY(right, ballPos, ballSize)) {
	    // Inverse and speed up X velocity
	    final int xMoveDelta = Math.max(-ball.getMaxSpeed(), -ballMoveDelta.x - ball.getSpeedIncrement());
	    // Set new move delta with new X and Y
	    ball.setMoveDelta(new Point(xMoveDelta, getYCausedByPaddle(right, ball)));
	    // Reset to in front of paddle
	    ballPos.setLocation(rightFront - ballSize.width, ballPos.y);
	}
    }

    private int getYCausedByPaddle(final Paddle paddle, final Ball ball) {
	// Ball data
	final Point ballPos = ball.getPosition();
	int ballYMoveDelta = ball.getMoveDelta().y;

	// Paddle data
	final Point paddlePos = paddle.getPosition();
	final int paddleYMoveDelta = paddle.getMoveDelta().y;

	// Paddle direction
	boolean movingUp = false;
	boolean movingDown = false;
	if (paddleYMoveDelta < 0) {
	    movingUp = true;
	} else if (paddleYMoveDelta > 0) {
	    movingDown = true;
	}

	// Hit top corner
	if (ballPos.y < paddlePos.y) {
	    if (ballYMoveDelta > 0) {
		// Inverse direction
		ballYMoveDelta = -ballYMoveDelta;
	    }
	    return Math.max(-ball.getMaxSpeed(), ballYMoveDelta - ball.getSpeedIncrement() / 2);
	}
	// Hit bottom corner
	else if (ballPos.y + ball.getSize().height > paddlePos.y + paddle.getSize().height) {
	    if (ballYMoveDelta < 0) {
		// Inverse direction
		ballYMoveDelta = -ballYMoveDelta;
	    }
	    return Math.min(ball.getMaxSpeed(), ballYMoveDelta + ball.getSpeedIncrement() / 2);
	}
	// Hit centre paddle
	else {
	    // Hitting inverse direction moving ball
	    if (ballYMoveDelta < 0 && movingDown || ballYMoveDelta > 0 && movingUp) {
		// Inverse direction
		ballYMoveDelta = -ballYMoveDelta;
	    }
	    // Speed up if moving up
	    if (movingUp) {
		ballYMoveDelta -= ball.getSpeedIncrement() / 4;
	    }
	    // Speed up if moving down
	    else if (movingDown) {
		ballYMoveDelta += ball.getSpeedIncrement() / 4;
	    }
	    return ballYMoveDelta;
	}
    }

    private boolean inPaddleY(final Paddle paddle, final Point ballPos, final Dimension ballSize) {
	final Point paddlePos = paddle.getPosition();
	return ballPos.y + ballSize.height >= paddlePos.y && ballPos.y <= paddlePos.y + paddle.getSize().height;
    }
}
