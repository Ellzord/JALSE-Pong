package pong.entities;

import java.awt.Point;

public interface Paddle extends TableElement {

    default void moveDown() {
	setMoveDelta(new Point(0, getSpeedIncrement()));
    }

    default void moveUp() {
	setMoveDelta(new Point(0, -getSpeedIncrement()));
    }
}
