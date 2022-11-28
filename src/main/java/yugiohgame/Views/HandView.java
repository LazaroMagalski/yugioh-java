package yugiohgame.Views;


import java.util.List;

import javax.swing.JOptionPane;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import yugiohgame.Cards.Card;
import yugiohgame.Cards.MonsterCard;
import yugiohgame.Cards.SpellCard;
import yugiohgame.Cards.TrapCard;
import yugiohgame.Components.Field;
import yugiohgame.Components.Hand;
import yugiohgame.Events.CardViewEvent;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.CardViewListener;
import yugiohgame.Listeners.GameListener;
import yugiohgame.Views.FieldView.CardType;
import yugiohgame.Game;

public class HandView extends HBox implements CardViewListener, GameListener {
	private int jogador;
	private Hand cHand;
	private Card selectedCard;

	public HandView(int nroJog) {
		super(4);
		this.setAlignment(Pos.CENTER);
		Game.getInstance().addGameListener(this);

		jogador = nroJog;
		selectedCard = null;

		cHand = null;
		if (jogador == 1) {
			cHand = Game.getInstance().getHandJ1();
		} else {
			cHand = Game.getInstance().getHandJ2();
		}
		cHand.addGameListener(this);

		for (Card card : cHand.getCards()) {
			CardView cv = new CardView(card);
			card.flip();
			cv.setCardViewObserver(this);
			this.getChildren().add(cv);
		}
	}

	private void removeSel() {
		ObservableList<Node> cards = getChildren();
		for (int i = 0; i < cards.size(); i++) {
			CardView cv = (CardView) cards.get(i);
			if (cv.getCard() == selectedCard) {
				getChildren().remove(cv);
				selectedCard = null;
			}
		}
	}

	private void drawCard(String jogador) {
		int numberCards = cHand.getNumberOfCards();
		if (Integer.parseInt(jogador) == this.jogador){
		
			List<Card> hand = cHand.getCards();

			Card drawedCard = hand.get(numberCards-1);
			CardView cv = new CardView(drawedCard);
			drawedCard.flip();
			cv.setCardViewObserver(this);
			this.getChildren().add(cv);
		}
			
	}


	private int getJogador(){
		return jogador;
	}


	@Override
	public void notify(GameEvent event) {
		if (event == null) return;
		if (event.getTarget() != GameEvent.Target.HAND) {
			return;
		}
		if (event.getAction() == GameEvent.Action.DRAWCARD) {
			drawCard(event.getArg());
		}
	}

	@Override
	public void handle(CardViewEvent event) {
		int j = getJogador();
		if (Game.getInstance().getPlayer() != j) { return; }

		CardView cv = event.getCardView();
		selectedCard = cv.getCard();

		if (selectedCard instanceof MonsterCard && Boolean.TRUE.equals(!cHand.getCondition())) { 
			JOptionPane.showMessageDialog(null, "VOCÊ JÁ INVOCOU UM MONSTRO NESSA RODADA");
			return; 
		}

		int numberCards = 0;
		CardType cardType = null;
		Field field = null;
		
		if (selectedCard instanceof MonsterCard) {
					if(j == 1) numberCards = (Game.getInstance().getFieldJ1(FieldView.CardType.MONSTERCARD).getNumberOfCards());
					if(j == 2) numberCards = (Game.getInstance().getFieldJ2(FieldView.CardType.MONSTERCARD).getNumberOfCards());
					cardType = FieldView.CardType.MONSTERCARD;

		}else if (selectedCard instanceof SpellCard || selectedCard instanceof TrapCard){
					if(j == 1) numberCards = (Game.getInstance().getFieldJ1(FieldView.CardType.SPELLCARD).getNumberOfCards());
					if(j == 2) numberCards = (Game.getInstance().getFieldJ2(FieldView.CardType.SPELLCARD).getNumberOfCards());
					cardType = FieldView.CardType.SPELLCARD;
		}

		if (numberCards < 5) {
			cHand.setSelectedCard(selectedCard);
			if (selectedCard instanceof TrapCard) { selectedCard.flip(); }
			Game.getInstance().addField(selectedCard, j, cardType);
			if (selectedCard instanceof MonsterCard) { cHand.setCondition(false); }
			if (selectedCard instanceof SpellCard){
				SpellCard c = (SpellCard) selectedCard;
				String effect = c.getEffect();
				if (j==1) {
					field = Game.getInstance().getFieldJ1(cardType);
				}else{
					field = Game.getInstance().getFieldJ2(cardType);
				}
				field.activateEffect(effect,c,j);
			}
			removeSel();
		}
	}
}
