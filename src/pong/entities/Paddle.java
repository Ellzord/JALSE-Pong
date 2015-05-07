package pong.entities;

import java.awt.Point;
import java.util.UUID;

public interface Paddle extends TableElement {

    UUID LEFT_ID = UUID.randomUUID();

    UUID RIGHT_ID = UUID.randomUUID();

    default void moveDown() {
	setMoveDelta(new Point(0, getSpeedIncrement()));
    }

    default void moveUp() {
	setMoveDelta(new Point(0, -getSpeedIncrement()));
    }
}
