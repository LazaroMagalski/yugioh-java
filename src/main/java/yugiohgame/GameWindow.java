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

public class GameWindow extends Application implements GameListener {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Game.getInstance().addGameListener(this);

		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

		primaryStage.setWidth(1200);
		primaryStage.setHeight(650);


		primaryStage.setTitle("Batalha de Cartas");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(25, 25, 25, 25));

		HandView deckJ1 = new HandView(1);
		BaralhoView baralhoJ1 = new BaralhoView(1);
		DeckView deckJ3 = new DeckView(1);
		DeckView deckJ5 = new DeckView(5);
		ScrollPane sd1 = new ScrollPane();
		ScrollPane sd3 = new ScrollPane();
		ScrollPane sd5 = new ScrollPane();
		ScrollPane sd8 = new ScrollPane();
		sd1.setPrefSize(600, 128);
		sd1.setContent(deckJ1);
		grid.add(sd1, 1, 0);
		sd3.setPrefSize(600, 128);
		sd3.setContent(deckJ3);
		grid.add(sd3, 1, 1);
		sd5.setPrefSize(600, 128);
		sd5.setContent(deckJ5);
		grid.add(sd5, 1, 2);
		sd8.setPrefSize(128, 128);
		sd8.setContent(baralhoJ1);
		grid.add(sd8, 0, 0);

		PlacarView placar = new PlacarView();
		grid.add(placar, 0, 2);

		Button butClean = new Button("Clean");
		grid.add(butClean, 2, 2);
		butClean.setOnAction(e -> Game.getInstance().removeSelected());

		DeckView deckJ2 = new DeckView(2);
		DeckView deckJ4 = new DeckView(4);
		HandView deckJ6 = new HandView(2);
		BaralhoView baralhoJ2 = new BaralhoView(2);
		ScrollPane sd2 = new ScrollPane();
		ScrollPane sd4 = new ScrollPane();
		ScrollPane sd6 = new ScrollPane();
		ScrollPane sd9 = new ScrollPane();
		sd2.setPrefSize(600, 128);
		sd2.setContent(deckJ2);
		grid.add(sd2, 1, 4);
		sd4.setPrefSize(600, 128);
		sd4.setContent(deckJ4);
		grid.add(sd4, 1, 5);
		sd6.setPrefSize(600, 128);
		sd6.setContent(deckJ6);
		grid.add(sd6, 1, 6);
		sd9.setPrefSize(128, 128);
		sd9.setContent(baralhoJ2);
		grid.add(sd9, 0, 6);

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
			case INVPLAY:
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção !!");
				alert.setHeaderText("Jogada inválida!!");
				alert.setContentText("Era a vez do jogador " + eg.getArg());
				alert.showAndWait();
				break;
			case MUSTCLEAN:
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção !!");
				alert.setHeaderText(null);
				alert.setContentText("Utilize o botao \"Clean\"");
				alert.showAndWait();
				break;
			case ENDGAME:
				String text = "Fim de Jogo !!\n";
				if (Game.getInstance().getPtsJ1() > Game.getInstance().getPtsJ2()) {
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
				// Esse evento não vem para cá
			}
		}
	}

}
