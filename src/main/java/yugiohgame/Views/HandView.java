package yugiohgame.Views;

import java.lang.reflect.Field;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import yugiohgame.Cards.Card;
import yugiohgame.Cards.MonsterCard;
import yugiohgame.Cards.SpellCard;
import yugiohgame.Cards.TrapCard;
import yugiohgame.Components.Hand;
import yugiohgame.Events.CardViewEvent;
import yugiohgame.Events.GameEvent;
import yugiohgame.Events.GameEvent.Action;
import yugiohgame.Events.GameEvent.Target;
import yugiohgame.Listeners.CardViewListener;
import yugiohgame.Listeners.GameListener;
import yugiohgame.Views.FieldView.CardType;
import yugiohgame.Game;

public class HandView extends HBox implements CardViewListener, GameListener {
	private int jogador;
	private Hand cDeck;
	private Card selectedCard;

	public HandView(int nroJog) {
		super(4);
		this.setAlignment(Pos.CENTER);
		Game.getInstance().addGameListener(this);
		jogador = nroJog;
		selectedCard = null;

		cDeck = null;
		if (jogador == 1) {
			cDeck = Game.getInstance().getHandJ1();
		} else {
			cDeck = Game.getInstance().getHandJ2();
		}
		cDeck.addGameListener(this);

		for (Card card : cDeck.getCards()) {
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
		int numberCards = cDeck.getNumberOfCards();
		if (Integer.parseInt(jogador) == this.jogador){
		
			List<Card> hand = cDeck.getCards();

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
		CardView cv = event.getCardView();
		int j = getJogador();
		int numberCards = 0;
		selectedCard = cv.getCard();
		CardType cardType = null;

		if (selectedCard instanceof MonsterCard) {
					if(j == 1) numberCards = (Game.getInstance().getDeckJ1(FieldView.CardType.MONSTERCARD).getNumberOfCards());
					if(j == 2) numberCards = (Game.getInstance().getDeckJ2(FieldView.CardType.MONSTERCARD).getNumberOfCards());
					cardType = FieldView.CardType.MONSTERCARD;

		}else if (selectedCard instanceof SpellCard || selectedCard instanceof TrapCard){
					if(j == 1) numberCards = (Game.getInstance().getDeckJ1(FieldView.CardType.SPELLCARD).getNumberOfCards());
					if(j == 2) numberCards = (Game.getInstance().getDeckJ2(FieldView.CardType.SPELLCARD).getNumberOfCards());
					cardType = FieldView.CardType.SPELLCARD;
		}

		if (numberCards < 5) {
			cDeck.setSelectedCard(selectedCard);
			Game.getInstance().addField(selectedCard, j, cardType);
			removeSel();
		}
	}
}
