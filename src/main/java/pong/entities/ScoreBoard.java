package pong.entities;

import java.util.UUID;

import jalse.entities.Entity;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

public interface ScoreBoard extends Entity {

    UUID ID = UUID.randomUUID();

    @GetAttribute
    UUID getLastWinner();

    @GetAttribute
    int getLeftScore();

    @GetAttribute
    int getRightScore();

    @GetAttribute
    boolean isLeftLastWinner();

    default void leftWins() {
	setLeftScore(getLeftScore() + 1);
	setLeftLastWinner(true);
    }

    default void rightWins() {
	setRightScore(getRightScore() + 1);
	setLeftLastWinner(false);
    }

    @SetAttribute
    void setLastWinner(UUID lastWinner);

    @SetAttribute
    void setLeftLastWinner(boolean leftLastWinner);

    @SetAttribute
    void setLeftScore(int score);

    @SetAttribute
    void setRightScore(int score);
}
