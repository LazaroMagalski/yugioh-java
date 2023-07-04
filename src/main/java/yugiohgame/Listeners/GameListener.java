package yugiohgame.Listeners;

import java.util.EventListener;

import yugiohgame.Events.GameEvent;

public interface GameListener extends EventListener {
	void notify(GameEvent event);
}
