package yugiohgame;

import java.util.LinkedList;
import java.util.List;

import yugiohgame.FieldView.CardType;

public class Game {
	private static Game game = new Game();
	private int ptsJ1, ptsJ2;
	private CardField spellJ1, monsterJ1, spellJ2, monsterJ2;
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
		spellJ1 = new CardField();
		monsterJ1 = new CardField();
		
		spellJ2 = new CardField();
		monsterJ2 = new CardField();

		handJ1 = new CardHand(1);
		handJ2 = new CardHand(2);

		baralhoJ1 = new CardBaralho(1);
		baralhoJ2 = new CardBaralho(2);
		handJ1.addCardHand(baralhoJ1, 5);
		handJ2.addCardHand(baralhoJ2, 5);

		player = 1;
		jogadas = CardField.NCARDS;
		observers = new LinkedList<>();
	}


	private void nextPlayer() {
		player++;
		if (player == 3) {
			player = 1;
		}
	}


	public int getPtsJ1() {
		return ptsJ1;
	}


	public int getPtsJ2() {
		return ptsJ2;
	}


	public CardField getDeckJ1(CardType cardType) {
		if (cardType == FieldView.CardType.MONSTERCARD) return monsterJ1;
		if (cardType == FieldView.CardType.SPELLCARD) return spellJ1;
		return null;
	}


	public CardField getDeckJ2(CardType cardType) {
		if (cardType == FieldView.CardType.MONSTERCARD) return monsterJ2;
		if (cardType == FieldView.CardType.SPELLCARD) return spellJ2;
		return null;
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


	public void play(CardField field) {
		GameEvent gameEvent = null;
		if (field == monsterJ1){
			if (player != 1){
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "1");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			}else {
				nextPlayer();
			}
		} else if (field == monsterJ2) {
			if (player != 2) {
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			} else {
				// Verifica quem ganhou a rodada
				if (monsterJ1.getSelectedCard().getValue() > monsterJ2.getSelectedCard().getValue()) {
					ptsJ2 -= monsterJ1.getSelectedCard().getValue() - monsterJ2.getSelectedCard().getValue();
					monsterJ2.removeSel();
					gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
				} else if (monsterJ1.getSelectedCard().getValue() < monsterJ2.getSelectedCard().getValue()) {
					ptsJ1 -= monsterJ2.getSelectedCard().getValue() - monsterJ1.getSelectedCard().getValue();
					monsterJ1.removeSel();
					gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
				} else{
					gameEvent = null;
				}
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// Próximo jogador
				nextPlayer();
			}
		}
		if (ptsJ1 <= 0 || baralhoJ1.getNumberOfCards() == 0){
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "2");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		if (ptsJ2 <= 0 || baralhoJ2.getNumberOfCards() == 0){
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "1");
			for (var observer : observers) {
				observer.notify(gameEvent);
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
		spellJ1.removeSel();
		monsterJ1.removeSel();
		nextPlayer();
	}


	public void addHand(CardBaralho c) {
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
		
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}


	public void addField(Card c, int j) {
		GameEvent gameEvent = null;
		if (j == 1) {
			
			getDeckJ1(FieldView.CardType.MONSTERCARD).addCard(c);
			gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "1");
			handJ1.removeSel();

		}
		if (j == 2) {

			getDeckJ2(FieldView.CardType.MONSTERCARD).addCard(c);
			gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "2");
			handJ2.removeSel();
		}

		if (gameEvent != null) {
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
	}


	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}
}
