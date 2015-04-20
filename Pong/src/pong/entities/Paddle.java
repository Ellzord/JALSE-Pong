package pong.entities;

import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

public interface Paddle extends TableElement {

    @GetAttribute("score")
    Integer getScore();

    @SetAttribute("score")
    void setScore(Integer score);
}
