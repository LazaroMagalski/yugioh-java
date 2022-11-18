package yugiohgame.Components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import yugiohgame.Cards.Card;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.GameListener;

public class Field {
	public static final int NCARDS = 1;
	private List<Card> cartas;
	private Card selected;
	private List<GameListener> observers;

	public Field() {
		cartas = new ArrayList<>(NCARDS);
		selected = null;
		observers = new LinkedList<>();
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(cartas);
	}

	public int getNumberOfCards() {
		return cartas.size();
	}

	public void removeSel() {
		if (selected == null) {
			return;
		}
		cartas.remove(selected);
		selected = null;
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.REMOVESEL, "");
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void setSelectedCard(Card card) {
		selected = card;
	}

	public Card getSelectedCard() {
		return selected;
	}


	public void addCard(Card c) {
		cartas.add(c);
	}


	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

}
