package pong.entities;

import jalse.entities.Entity;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

import java.util.UUID;

public interface ScoreBoard extends Entity {

    UUID ID = UUID.randomUUID();

    @GetAttribute("lastWinner")
    UUID getLastWinner();

    @GetAttribute("leftScore")
    Integer getLeftScore();

    @GetAttribute("rightScore")
    Integer getRightScore();

    default void leftWins() {
	setLeftScore(getLeftScore() + 1);
	setLastWinner(Paddle.LEFT_ID);
    }

    default void rightWins() {
	setRightScore(getRightScore() + 1);
	setLastWinner(Paddle.RIGHT_ID);
    }

    @SetAttribute("lastWinner")
    void setLastWinner(UUID lastWinner);

    @SetAttribute("leftScore")
    void setLeftScore(Integer score);

    @SetAttribute("rightScore")
    void setRightScore(Integer score);
}
