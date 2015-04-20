package pong.entities;

import jalse.entities.Entity;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

import java.util.UUID;

public interface PongTable extends Entity {

    UUID TABLE_ID = UUID.randomUUID();

    UUID LEFT_PADDLE_ID = UUID.randomUUID();

    UUID RIGHT_PADDLE_ID = UUID.randomUUID();

    UUID BALL_ID = UUID.randomUUID();

    default Ball getBall() {
	return getEntityAsType(BALL_ID, Ball.class);
    }

    @GetAttribute("height")
    Integer getHeight();

    @SetAttribute("height")
    Integer getHeight(Integer height);

    default Paddle getLeftPaddle() {
	return getEntityAsType(LEFT_PADDLE_ID, Paddle.class);
    }

    default Paddle getRightPaddle() {
	return getEntityAsType(RIGHT_PADDLE_ID, Paddle.class);
    }

    @GetAttribute("width")
    Integer getWidth();

    @SetAttribute("width")
    void getWidth(Integer width);

    default Ball newBall() {
	return newEntity(BALL_ID, Ball.class);
    }

    default Paddle newLeftPaddle() {
	return newEntity(LEFT_PADDLE_ID, Paddle.class);
    }

    default Paddle newRightPaddle() {
	return newEntity(RIGHT_PADDLE_ID, Paddle.class);
    }
}
