package pong.actions;

import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.entities.Entity;

import java.awt.Dimension;
import java.awt.Point;

import pong.entities.Table;

public class MoveElements implements Action<Entity> {

    private static int bounded(final int value, final int min, final int max) {
	return value < min ? min : value > max ? max : value;
    }

    @Override
    public void perform(final ActionContext<Entity> context) throws InterruptedException {
	final Table table = context.getActor().asType(Table.class);
	final Dimension tableSize = table.getSize();
	// Move all elements
	table.streamElements().forEach(element -> {
	    final Point moveDelta = element.getMoveDelta();
	    if (moveDelta.x == 0 && moveDelta.y == 0) {
		// No movement
		return;
	    }

	    // Original
	    final Point elementPos = element.getPosition();
	    final Dimension elementSize = element.getSize();

	    // Calculate bounded x & y
	    final int x = bounded(elementPos.x + moveDelta.x, 0, tableSize.width - elementSize.width);
	    final int y = bounded(elementPos.y + moveDelta.y, 0, tableSize.height - elementSize.height);

	    if (elementPos.x != x || elementPos.y != y) {
		// Update if changed
		element.setPosition(new Point(x, y));
	    }
	});
    }
}
