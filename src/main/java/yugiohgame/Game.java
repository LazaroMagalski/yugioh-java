package yugiohgame;

import java.util.LinkedList;
import java.util.List;

public class Game {
	private static Game game = new Game();
	private int ptsJ1, ptsJ2;
	private CardDeck deckJ1, deckJ2;
	private CardHand handJ1, handJ2;
	private CardBaralho baralhoJ1, baralhoJ2;
	private int player;
	private int jogadas;
	private List<GameListener> observers;
	
	public static Game getInstance() {
		return game;
	}

	private Game() {
		ptsJ1 = 8000;
		ptsJ2 = 8000;
		deckJ1 = new CardDeck();
		deckJ2 = new CardDeck();
		handJ1 = new CardHand(1);
		handJ2 = new CardHand(2);
		baralhoJ1 = new CardBaralho(1);
		baralhoJ2 = new CardBaralho(2);
		handJ1.addCardHand(baralhoJ1, 5);
		handJ2.addCardHand(baralhoJ2, 5);
		player = 1;
		jogadas = CardDeck.NCARDS;
		observers = new LinkedList<>();
	}

	private void nextPlayer() {
		player++;
		if (player == 4) {
			player = 1;
		}
	}

	public int getPtsJ1() {
		return ptsJ1;
	}

	public int getPtsJ2() {
		return ptsJ2;
	}

	public CardDeck getDeckJ1() {
		return deckJ1;
	}

	public CardDeck getDeckJ2() {
		return deckJ2;
	}

	public CardHand getHandJ1() {
		return handJ1;
	}

	public CardHand getHandJ2() {
		return handJ2;
	}
	public CardBaralho getBaralhoJ1() {
		return baralhoJ1;
	}

	public CardBaralho getBaralhoJ2() {
		return baralhoJ2;
	}


	public void playHand(CardHand hand) {
		System.out.println(hand.getNumberOfCards());
		GameEvent gameEvent = null;
		if (player == 3) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTCLEAN, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}
		if (hand == handJ1){
			if (player != 1){
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			}else {
				nextPlayer();
			}
		} else if (hand == handJ2) {
			if (player != 2) {
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			} else {
				// Verifica quem ganhou a rodada
				if (handJ1.getSelectedCard().getValue() > handJ2.getSelectedCard().getValue()) {
					ptsJ2 -= handJ1.getSelectedCard().getValue() - handJ2.getSelectedCard().getValue();
				} else if (handJ1.getSelectedCard().getValue() < handJ2.getSelectedCard().getValue()) {
					ptsJ1 -= handJ2.getSelectedCard().getValue() - handJ1.getSelectedCard().getValue();
				}
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// Próximo jogador
				nextPlayer();
			}
		}
	}


	public void play(CardDeck deckAcionado) {
		GameEvent gameEvent = null;
		if (player == 3) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTCLEAN, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}
		if (deckAcionado == deckJ1) {
			if (player != 1) {
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			} else {
				// Vira a carta
				deckJ1.getSelectedCard().flip();
				// Proximo jogador
				nextPlayer();
			}
		} else if (deckAcionado == deckJ2) {
			if (player != 2) {
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			} else {
				// Vira a carta
				deckJ2.getSelectedCard().flip();
				// Verifica quem ganhou a rodada
				if (deckJ1.getSelectedCard().getValue() > deckJ2.getSelectedCard().getValue()) {
					ptsJ1++;
				} else if (deckJ1.getSelectedCard().getValue() < deckJ2.getSelectedCard().getValue()) {
					ptsJ2++;
				}
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// Próximo jogador
				nextPlayer();
			}
		}
	}

	// Acionada pelo botao de limpar
	public void removeSelected() {
		GameEvent gameEvent = null;
		if (player != 3) {
			return;
		}
		jogadas--;
		if (jogadas == 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		deckJ1.removeSel();
		deckJ2.removeSel();
		nextPlayer();
	}
	
	public void addHand(CardBaralho c) {
		System.out.println(c.getNumberOfCards());
		GameEvent gameEvent = null;
		if (c == baralhoJ1) {
			if (handJ1.getNumberOfCards() < 7){
				handJ1.addCardHand(c, 1);
				gameEvent = new GameEvent(this, GameEvent.Target.HAND, GameEvent.Action.DRAWCARD, "1");
			}else{
				return;
			}
		}
		if (c == baralhoJ2) {
			if (handJ2.getNumberOfCards() < 7){
				handJ2.addCardHand(c, 1);
				gameEvent = new GameEvent(this, GameEvent.Target.HAND, GameEvent.Action.DRAWCARD, "2");
			}else{
				return;
			}
		}
		
		System.out.println(gameEvent);
		for (var observer : observers) {
			System.out.println(observer+"\n");
			observer.notify(gameEvent);
		}
	}
	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}
}
