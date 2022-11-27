package yugiohgame.Components;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import yugiohgame.Cards.Card;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.GameListener;

public class Hand {
	public static final int NCARDS = 7;
	private List<Card> cartas;
	private Card selected;
	private List<GameListener> observers;
	private Deck baralho;
	
	public Hand(int nroJogador) {
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
	public void addCardHand(Deck baralho, int nCards) {
		for (int i = 0; i < nCards; i++) {
			Card c = baralho.drawCard();
			c.flip();
			cartas.add(c);
		}
	}

	public void addHand(Card card) {
		card.flip();
		cartas.add(card);
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

	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

}