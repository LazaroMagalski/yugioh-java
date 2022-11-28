package yugiohgame.Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import yugiohgame.Game;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.GameListener;

public class PlacarView extends GridPane implements GameListener {
	private TextField ptsJ1, ptsJ2;
	private Label lbJ1, lbJ2;

	public PlacarView() {
		this.setAlignment(Pos.CENTER);
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(25, 25, 25, 25));

		Game.getInstance().addGameListener(this);

		ptsJ1 = new TextField();
		ptsJ2 = new TextField();

		ptsJ1.setText("" + Game.getInstance().getPtsJ1());
		ptsJ2.setText("" + Game.getInstance().getPtsJ2());
		lbJ1 = new Label("LP Jogador 1:");
		lbJ2 = new Label("LP Jogador 2:");

		this.add(lbJ1, 0, 0);
		this.add(ptsJ1, 1, 0);
		this.add(lbJ2, 0, 1);
		this.add(ptsJ2, 1, 1);
		lbJ1.setStyle("-fx-font-weight: bold");
		lbJ2.setStyle("-fx-font-weight: regular");
	}

	@Override
	public void notify(GameEvent event) {
		ptsJ1.setText("" + Game.getInstance().getPtsJ1());
		ptsJ2.setText("" + Game.getInstance().getPtsJ2());
		if(Game.getInstance().getPlayer()==1){
			lbJ1.setStyle("-fx-font-weight: bold");
			lbJ2.setStyle("-fx-font-weight: regular");
		}
		else{
			lbJ1.setStyle("-fx-font-weight: regular");
			lbJ2.setStyle("-fx-font-weight: bold");
		}
	}
}