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
    public void attributeChanged(final AttributeEvent<Point> event) {
	final Ball ball = ((Entity) event.getContainer()).asType(Ball.class);
	final Table table = ((Entity) ball.getContainer()).asType(Table.class);

	final Dimension ballSize = ball.getSize();
	final Dimension tableSize = table.getSize();

	final Point position = event.getValue();
	final Point moveDelta = ball.getMoveDelta();

	// Off top
	if (position.getY() <= 0) {
	    moveDelta.setLocation(moveDelta.getX(), moveDelta.getY() * -1);
	    position.setLocation(position.getX(), 0);
	    return;
	}

	// Hit bottom
	if (position.getY() + ballSize.getHeight() >= tableSize.getHeight()) {
	    moveDelta.setLocation(moveDelta.getX(), moveDelta.getY() * -1);
	    position.setLocation(position.getX(), tableSize.getHeight() - ballSize.getHeight());
	    return;
	}

	// Hit left paddle
	final Paddle left = table.getLeftPaddle();
	final double leftFront = left.getSize().getWidth();
	if (position.getX() <= leftFront && inYBounds(position, ballSize, left)) {
	    moveDelta.setLocation(moveDelta.getX() * -1 + ball.getSpeed() / 2, moveDelta.getY());
	    position.setLocation(leftFront, position.getY());
	    return;
	}

	// Hit right paddle
	final Paddle right = table.getRightPaddle();
	final double rightFront = tableSize.getWidth() - right.getSize().getWidth();
	if (position.getX() + ballSize.getWidth() >= rightFront && inYBounds(position, ballSize, right)) {
	    moveDelta.setLocation(moveDelta.getX() * -1 - ball.getSpeed() / 2, moveDelta.getY());
	    position.setLocation(rightFront - ballSize.getWidth(), position.getY());
	}
    }

    private boolean inYBounds(final Point position, final Dimension size, final Paddle paddle) {
	final Point paddlePos = paddle.getPosition();
	return position.getY() + size.getHeight() > paddlePos.getY()
		&& position.getY() < paddlePos.getY() + paddle.getSize().getHeight();
    }
}
