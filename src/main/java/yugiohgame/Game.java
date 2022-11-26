package yugiohgame;

import java.util.LinkedList;
import java.util.List;

import yugiohgame.Views.FieldView.CardType;
import yugiohgame.Cards.Card;
import yugiohgame.Cards.MonsterCard;
import yugiohgame.Components.Deck;
import yugiohgame.Components.Field;
import yugiohgame.Components.Hand;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.GameListener;
import yugiohgame.Views.FieldView;

public class Game {
	private static Game game = new Game();
	private int ptsJ1, ptsJ2;
	private Field spellJ1, monsterJ1, spellJ2, monsterJ2;
	private Hand handJ1, handJ2;
	private Deck baralhoJ1, baralhoJ2;
	private int player;
	private int jogadas;
	private List<GameListener> observers;


	public static Game getInstance() {
		return game;
	}


	private Game() {
		ptsJ1 = 8000;
		ptsJ2 = 8000;
		spellJ1 = new Field();
		monsterJ1 = new Field();
		
		spellJ2 = new Field();
		monsterJ2 = new Field();

		handJ1 = new Hand(1);
		handJ2 = new Hand(2);

		baralhoJ1 = new Deck(1);
		baralhoJ2 = new Deck(2);
		handJ1.addCardHand(baralhoJ1, 5);
		handJ2.addCardHand(baralhoJ2, 5);

		player = 1;
		jogadas = Field.NCARDS;
		observers = new LinkedList<>();
	}


	public int nextPlayer() {
		player++;
		if (player == 3) {
			player = 1;
		}
		
		return player;
	}


	public int getPtsJ1() {
		return ptsJ1;
	}


	public int getPtsJ2() {
		return ptsJ2;
	}


	public Field getDeckJ1(CardType cardType) {
		if (cardType == FieldView.CardType.MONSTERCARD) return monsterJ1;
		if (cardType == FieldView.CardType.SPELLCARD) return spellJ1;
		return null;
	}


	public Field getDeckJ2(CardType cardType) {
		if (cardType == FieldView.CardType.MONSTERCARD) return monsterJ2;
		if (cardType == FieldView.CardType.SPELLCARD) return spellJ2;
		return null;
	}


	public Hand getHandJ1() {
		return handJ1;
	}


	public Hand getHandJ2() {
		return handJ2;
	}


	public Deck getBaralhoJ1() {
		return baralhoJ1;
	}

	public Deck getBaralhoJ2() {
		return baralhoJ2;
	}


	public void play(Field field) {
		GameEvent gameEvent = null;

		if (field == monsterJ1){
			if (monsterJ2.getNumberOfCards() == 0){
				MonsterCard monster1 = (MonsterCard) monsterJ1.getSelectedCard();
				ptsJ2 -= monster1.getAtkPoints();
				gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
			}
			nextPlayer();

		} else if (field == monsterJ2) {
			// Verifica quem ganhou a rodada
			MonsterCard monster2 = (MonsterCard) monsterJ2.getSelectedCard();
			if (monsterJ1.getNumberOfCards() == 0) {
				ptsJ1 -= monster2.getAtkPoints();
				gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
			}else{
				MonsterCard monster1 = (MonsterCard) monsterJ1.getSelectedCard();
				if (monster1.getAtkPoints() > monster2.getAtkPoints()) {
					ptsJ2 -= monster1.getAtkPoints() - monster2.getAtkPoints();
					monsterJ2.removeSel();
					gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
				} else if (monster1.getAtkPoints() < monster2.getAtkPoints()) {
					ptsJ1 -= monster2.getAtkPoints() - monster1.getAtkPoints();
					monsterJ1.removeSel();
					gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
				} else{
					gameEvent = null;
				}
			}
			// Próximo jogador
			nextPlayer();
			}
		
		if (ptsJ1 <= 0 || baralhoJ1.getNumberOfCards() == 0){
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "2");
		}

		if (ptsJ2 <= 0 || baralhoJ2.getNumberOfCards() == 0){
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "1");
		}

		if(gameEvent != null){
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


	public void addHand(Deck c) {
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
