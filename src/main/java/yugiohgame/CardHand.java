package yugiohgame;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardHand {
	public static final int NCARDS = 5;
	private List<Card> cartas;
	private Card selected;
	private List<GameListener> observers;
	private CardBaralho baralho;
	
	public CardHand(int nroJogador) {
		cartas = new ArrayList<>(NCARDS);
		selected = null;
		String caminhoAtual = "";
		
		observers = new LinkedList<>();
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(cartas);
	}

	public int getNumberOfCards() {
		return cartas.size();
	}
	public void addCardHand(CardBaralho baralho, int nCards) {
		for (int i = 0; i < nCards; i++) {
			Card c = baralho.drawCard();
			c.flip();
			cartas.add(c);
		}
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
