package yugiohgame.Views;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import yugiohgame.Game;
import yugiohgame.Cards.Card;
import yugiohgame.Components.Deck;
import yugiohgame.Events.CardViewEvent;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.CardViewListener;
import yugiohgame.Listeners.GameListener;

public class DeckView extends HBox implements CardViewListener, GameListener {
	private int jogador;
	private Deck cDeck;
	private Card selectedCard;
	private TextField numberCards;


	public DeckView(int nroJog) {
		super(4);
		this.setAlignment(Pos.CENTER);
		Game.getInstance().addGameListener(this);

		jogador = nroJog;
		selectedCard = null;

		cDeck = null;
		if (jogador == 1) {
			cDeck = Game.getInstance().getBaralhoJ1();
		} else {
			cDeck = Game.getInstance().getBaralhoJ2();
		}
		cDeck.addGameListener(this);
		
		getTopo();

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
	private Deck getDeck(){
		return cDeck;
	}


	private void getTopo(){
		this.getChildren().clear();
		CardView topo = null;
		for (Card card : cDeck.getCards()) {
			CardView cv = new CardView(card);
			cv.setCardViewObserver(this);
			topo = cv;
		}
		numberCards = new TextField();
		numberCards.setText("" + cDeck.getNumberOfCards());
		this.getChildren().add(topo);
		this.getChildren().add(new Label("Cards Restantes: "));
		this.getChildren().add(numberCards);

	}


	@Override
	public void notify(GameEvent event) {
		if (event.getTarget() != GameEvent.Target.BARALHO) {
			return;
		}
		if (event.getAction() == GameEvent.Action.REMOVESEL) {
			removeSel();
		}
		if (event.getAction() == GameEvent.Action.PLAYERCHANGE){
			getTopo();
		}
	}

	@Override
	public void handle(CardViewEvent event) {
		Deck baralho = getDeck();
		Game.getInstance().addHand(baralho);
		getTopo();
	}
}
