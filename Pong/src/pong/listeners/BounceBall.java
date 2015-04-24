package pong.listeners;

import jalse.entities.Entity;
import jalse.listeners.AttributeAdapter;
import jalse.listeners.AttributeEvent;

import java.awt.Dimension;
import java.awt.Point;

import pong.entities.Ball;
import pong.entities.Paddle;
import pong.entities.Table;

public class BounceBall extends AttributeAdapter<Point> {

    @Override
    public void attributeAdded(final AttributeEvent<Point> event) {
	final Ball ball = ((Entity) event.getContainer()).asType(Ball.class);
	final Table table = ((Entity) ball.getContainer()).asType(Table.class);

	final Dimension ballSize = ball.getSize();
	final Dimension tableSize = table.getSize();

	final Point ballPos = event.getValue();
	final Point ballMoveDelta = ball.getMoveDelta();

	// Off top
	if (ballPos.y == 0) {
	    ball.setMoveDelta(new Point(ballMoveDelta.x, -ballMoveDelta.y));
	    return;
	}

	// Hit bottom
	if (ballPos.y + ballSize.height == tableSize.height) {
	    ball.setMoveDelta(new Point(ballMoveDelta.x, -ballMoveDelta.y));
	    return;
	}

	// Hit left paddle
	final Paddle left = table.getLeftPaddle();
	final double leftFront = left.getSize().width;
	if (ballPos.x <= leftFront && inPaddleY(left, ballPos, ballSize)) {
	    final int xMoveDelta = Math.min(ball.getMaxSpeed(), -ballMoveDelta.x + ball.getSpeed());
	    ball.setMoveDelta(new Point(xMoveDelta, ballMoveDelta.y));
	    ballPos.setLocation(leftFront, ballPos.y);
	    return;
	}

	// Hit right paddle
	final Paddle right = table.getRightPaddle();
	final double rightFront = tableSize.width - right.getSize().width;
	if (ballPos.x + ballSize.width >= rightFront && inPaddleY(right, ballPos, ballSize)) {
	    final int xMoveDelta = Math.max(-ball.getMaxSpeed(), -ballMoveDelta.x - ball.getSpeed());
	    ball.setMoveDelta(new Point(xMoveDelta, ballMoveDelta.y));
	    ballPos.setLocation(rightFront - ballSize.width, ballPos.y);
	}
    }

    private boolean inPaddleY(final Paddle paddle, final Point ballPos, final Dimension ballSize) {
	final Point paddlePos = paddle.getPosition();
	return ballPos.y + ballSize.height > paddlePos.y && ballPos.y < paddlePos.y + paddle.getSize().height;
    }
}
