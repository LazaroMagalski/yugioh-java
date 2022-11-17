package yugiohgame;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class FieldView extends HBox implements CardViewListener, GameListener {
	private int jogador;
	private CardField cDeck;
	private Card selectedCard;
	private CardType cardType;

	public enum CardType {
		MONSTERCARD, SPELLCARD, TRAPCARD
	};


	public FieldView(int nroJog, CardType cardType) {
		super(4);
		this.setAlignment(Pos.CENTER);
		Game.getInstance().addGameListener(this);

		jogador = nroJog;
		selectedCard = null;
		this.cardType = cardType;
	
		cDeck = null;
		if (jogador == 1) {
			cDeck = Game.getInstance().getDeckJ1(cardType);
		} else {
			cDeck = Game.getInstance().getDeckJ2(cardType);
		}
		cDeck.addGameListener(this);

		makeField();
		
	}


	private void makeField() {
		
		this.getChildren().clear();
		for (Card card : cDeck.getCards()) {
			card.flip();
			CardView cv = new CardView(card);
			System.out.println(card.isFacedUp());
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
