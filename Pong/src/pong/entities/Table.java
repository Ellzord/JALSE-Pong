package pong.entities;

import jalse.entities.Entity;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;
import jalse.entities.annotations.StreamEntities;

import java.awt.Dimension;
import java.util.UUID;
import java.util.stream.Stream;

public interface Table extends Entity {

    UUID ID = UUID.randomUUID();

    default Ball getBall() {
	return getEntityAsType(Ball.ID, Ball.class);
    }

    default Paddle getLeftPaddle() {
	return getEntityAsType(Paddle.LEFT_ID, Paddle.class);
    }

    default Paddle getRightPaddle() {
	return getEntityAsType(Paddle.RIGHT_ID, Paddle.class);
    }

    @GetAttribute("size")
    Dimension getSize();

    @SetAttribute("size")
    void setSize(Dimension size);

    @StreamEntities
    Stream<TableElement> streamElements();
}
