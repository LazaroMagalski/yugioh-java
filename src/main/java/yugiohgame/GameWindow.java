package yugiohgame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.GameListener;
import yugiohgame.Views.DeckView;
import yugiohgame.Views.FieldView;
import yugiohgame.Views.HandView;
import yugiohgame.Views.PlacarView;

public class GameWindow extends Application implements GameListener {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Game.getInstance().addGameListener(this);
	
		//primaryStage.setWidth(1200);
		//primaryStage.setHeight(650);

		primaryStage.setTitle("Batalha de Cartas");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(25, 25, 25, 25));

		HandView handJ1 = new HandView(1);
		DeckView baralhoJ1 = new DeckView(1);
		FieldView fieldSpell1 = new FieldView(1,FieldView.CardType.SPELLCARD);
		FieldView fieldMonster1 = new FieldView(1, FieldView.CardType.MONSTERCARD);

		ScrollPane sdHand1 = new ScrollPane();
		ScrollPane sdSpell1 = new ScrollPane();
		ScrollPane sdMonster1 = new ScrollPane();
		ScrollPane sdBaralho1 = new ScrollPane();

		sdHand1.setPrefSize(600, 128);
		sdHand1.setContent(handJ1);
		grid.add(sdHand1, 1, 0);

		sdSpell1.setPrefSize(600, 128);
		sdSpell1.setContent(fieldSpell1);
		grid.add(sdSpell1, 1, 1);

		sdMonster1.setPrefSize(600, 128);
		sdMonster1.setContent(fieldMonster1);
		grid.add(sdMonster1, 1, 2);

		sdBaralho1.setPrefSize(128, 128);
		sdBaralho1.setContent(baralhoJ1);
		grid.add(sdBaralho1, 0, 0);

		PlacarView placar = new PlacarView();
		grid.add(placar, 0, 2);


		Button butClean = new Button("Clean");
		grid.add(butClean, 2, 2);
		butClean.setOnAction(e -> Game.getInstance().removeSelected());

		FieldView fieldSpell2 = new FieldView(2, FieldView.CardType.SPELLCARD);
		FieldView fieldMonster2 = new FieldView(2, FieldView.CardType.MONSTERCARD);
		HandView handJ2 = new HandView(2);
		DeckView baralhoJ2 = new DeckView(2);

		ScrollPane sdSpell2 = new ScrollPane();
		ScrollPane sdField2 = new ScrollPane();
		ScrollPane sdHand2 = new ScrollPane();
		ScrollPane sdBaralho2 = new ScrollPane();

		sdSpell2.setPrefSize(600, 128);
		sdSpell2.setContent(fieldSpell2);
		grid.add(sdSpell2, 1, 5);

		sdField2.setPrefSize(600, 128);
		sdField2.setContent(fieldMonster2);
		grid.add(sdField2, 1, 4);

		sdHand2.setPrefSize(600, 128);
		sdHand2.setContent(handJ2);
		grid.add(sdHand2, 1, 6);

		sdBaralho2.setPrefSize(128, 128);
		sdBaralho2.setContent(baralhoJ2);
		grid.add(sdBaralho2, 0, 6);

		Scene scene = new Scene(grid);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void notify(GameEvent eg) {
		Alert alert;
		if (eg == null) return;
		if (eg.getTarget() == GameEvent.Target.GWIN) {
			switch (eg.getAction()) {
			case ENDGAME:
				String text = "Fim de Jogo !!\n";
				if (eg.getArg().equals("1")) {
					text += "O jogador 1 ganhou !! :-)";
				} else {
					text += "O jogador 2 ganhou !! :-)";
				}
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Parabens !!");
				alert.setHeaderText(null);
				alert.setContentText(text);
				alert.showAndWait();
				break;
			case REMOVESEL:
			case SUMMONCARD:
			case DRAWCARD:
			default:
				// Esse evento não vem para cá
			}
		}
	}

}
