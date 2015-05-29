package pong.entities;

import jalse.entities.Entity;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

import java.util.UUID;

public interface ScoreBoard extends Entity {

    UUID ID = UUID.randomUUID();

    @GetAttribute
    UUID getLastWinner();

    @GetAttribute
    Integer getLeftScore();

    @GetAttribute
    Integer getRightScore();

    @GetAttribute
    Boolean isLeftLastWinner();

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
    void setLeftLastWinner(Boolean leftLastWinner);

    @SetAttribute
    void setLeftScore(Integer score);

    @SetAttribute
    void setRightScore(Integer score);
}
