package yugiohgame.Views;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import yugiohgame.Game;
import yugiohgame.Cards.Card;
import yugiohgame.Components.Field;
import yugiohgame.Events.CardViewEvent;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.CardViewListener;
import yugiohgame.Listeners.GameListener;

public class FieldView extends HBox implements CardViewListener, GameListener {
	private int jogador;
	private Field cDeck;
	private Card selectedCard;

	public enum CardType {
		MONSTERCARD, SPELLCARD, TRAPCARD
	};


	public FieldView(int nroJog, CardType cardType) {
		super(4);
		this.setAlignment(Pos.CENTER);
		Game.getInstance().addGameListener(this);

		jogador = nroJog;
		selectedCard = null;
	
		cDeck = null;
		
		if (jogador == 1) {
			cDeck = Game.getInstance().getFieldJ1(cardType);
		} else {
			cDeck = Game.getInstance().getFieldJ2(cardType);
		}
		cDeck.addGameListener(this);

		makeField();
		
	}


	private void makeField() {
		
		this.getChildren().clear();

		for (Card card : cDeck.getCards()) {
			card.flip();
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

	@Override
	public void notify(GameEvent event) {
		if (event.getTarget() != GameEvent.Target.DECK) {
			return;
		}
		if (event.getAction() == GameEvent.Action.REMOVESEL) {
			removeSel();
			makeField();
		}
		if (event.getAction() == GameEvent.Action.SUMMONCARD) {
			makeField();
		}
	}

	@Override
	public void handle(CardViewEvent event) {
		CardView cv = event.getCardView();
		selectedCard = cv.getCard();
		cDeck.setSelectedCard(selectedCard);
		Game.getInstance().play(cDeck);
	}
}
