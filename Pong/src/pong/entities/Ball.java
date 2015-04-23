package pong.entities;

import java.awt.Point;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public interface Ball extends TableElement {

    UUID ID = UUID.randomUUID();

    default void randomMoveLeft() {
	final int speed = getSpeed();
	setMoveDelta(new Point(-speed, ThreadLocalRandom.current().nextBoolean() ? -speed : speed));
    }

    default void randomMoveRight() {
	final int speed = getSpeed();
	setMoveDelta(new Point(speed, ThreadLocalRandom.current().nextBoolean() ? -speed : speed));
    }
}
