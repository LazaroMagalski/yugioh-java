package yugiohgame.Components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import yugiohgame.Game;
import yugiohgame.Cards.Card;
import yugiohgame.Cards.MonsterCard;
import yugiohgame.Cards.SpellCard;
import yugiohgame.Cards.TrapCard;
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


	public void activateEffect(String effect, Card card, int jogador) {
		System.out.println("Effect Field"+effect);
		GameEvent gameEvent= null;
		Field field = null;
		SpellCard s = (SpellCard) card;
		int modifier = s.getModifier();

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
				break;
			case "Destroy the 1 face-up monster your opponent controls that has the lowest ATK":

				if (jogador==1){ 
					field = Game.getInstance().getFieldJ2(FieldView.CardType.MONSTERCARD); 
				} else { 
					field = Game.getInstance().getFieldJ1(FieldView.CardType.MONSTERCARD); 
				} 

				int aux = 9999999;
				MonsterCard auxMonster = null;

				for (Card c : field.getCards()) {
					MonsterCard monster = (MonsterCard) c;
					if (monster.getAtkPoints() < aux) { 
						aux = monster.getAtkPoints();
						auxMonster = monster; 
					}
				}

				field.setSelectedCard(auxMonster);
				field.removeSel();
				break;
			case "Increase Attack points by":
				
				if (jogador==1){ 
					field = Game.getInstance().getFieldJ1(FieldView.CardType.MONSTERCARD); 
				} else { 
					field = Game.getInstance().getFieldJ2(FieldView.CardType.MONSTERCARD); 
				}

				for (Card c : field.getCards()){
					MonsterCard monster = (MonsterCard) c;
					monster.addAtkPoints(modifier);
				}
				break;
			case "Reduce opponent's LP by":
				if (jogador==1){ 
					Game.getInstance().reduceLP(modifier, 2);
				}else{
					Game.getInstance().reduceLP(modifier, 1);
				}
				break;

			case "Increase your Life Points by":
				if (jogador==1){ 
					Game.getInstance().addLP(modifier, 1);
				}else{
					Game.getInstance().addLP(modifier, 2);
				}
				break;
			default:
				break;
		}
		setSelectedCard(card);
		removeSel();
	}

	public Boolean activateTrap(String effect, Card card, int jogador) {
		System.out.println("Effect Field"+effect);
		Boolean negateAttack = false;
		GameEvent gameEvent= null;
		Field field = null;
		TrapCard t = (TrapCard) card;
		int modifier = t.getModifier();

		switch(effect) {
			case "Negate the attack of your opponent monsters and gain Life Points equal to the ATK of the monster":
				if (jogador==1){ 
					MonsterCard mc = Game.getInstance().getMC2();
					int atk = mc.getAtkPoints();
					Game.getInstance().addLP(atk, 1);
				
				}else{
					MonsterCard mc = Game.getInstance().getMC1();
					int atk = mc.getAtkPoints();
					Game.getInstance().addLP(atk, 2);
				
				}
				negateAttack = true;
				break;
			case "When your opponent attack with a monster with 1000 or more ATK destroy the monster":
				MonsterCard auxMonster = null;
				if (jogador==1){ 
					field = Game.getInstance().getFieldJ2(FieldView.CardType.MONSTERCARD); 
					auxMonster = Game.getInstance().getMC2();
				} else { 
					field = Game.getInstance().getFieldJ1(FieldView.CardType.MONSTERCARD); 
					auxMonster = Game.getInstance().getMC1();
				} 

				System.out.println(auxMonster.toString());
				if(auxMonster.getAtkPoints() >= 1000){
					field.setSelectedCard(auxMonster);
					field.removeSel();
					gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
				}
			negateAttack = true;
			break;
		}
		return negateAttack;
	}
	
	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

	public List<GameListener> getObservers(){ return observers; }

	public String toString(){
		String text = "";
		for(Card c:cartas){
			text+= c.toString();
		}
		return text;
	}
}
