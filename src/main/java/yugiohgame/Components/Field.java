package yugiohgame.Components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import yugiohgame.Game;
import yugiohgame.Cards.Card;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.GameListener;
import yugiohgame.Views.FieldView;

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


	public void clearField(){
		cartas.clear();
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


	public void activateEffect(String effect, Card card) {
		System.out.println("Effect Field"+effect);
		GameEvent gameEvent= null;

		switch(effect) {
			case "Destroy all monsters":
				Field d1 = Game.getInstance().getFieldJ1(FieldView.CardType.MONSTERCARD);
				Field d2 = Game.getInstance().getFieldJ2(FieldView.CardType.MONSTERCARD);
				d1.clearField();
				d2.clearField();
				gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "1");
				System.out.println(observers);
				for (var observer : d1.getObservers()) {
					observer.notify(gameEvent);
				}
				gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "2");
				for (var observer : d2.getObservers()) {
					observer.notify(gameEvent);
				}
				
				
		}
		setSelectedCard(card);
		removeSel();
	}

	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

	public List<GameListener> getObservers(){ return observers; }
}
