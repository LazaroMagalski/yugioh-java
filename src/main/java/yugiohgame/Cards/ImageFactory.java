package yugiohgame.Cards;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageFactory {
	private static ImageFactory imgf = new ImageFactory();
	private Map<String, Image> images;

	public static ImageFactory getInstance() {
		return imgf;
	}

	private ImageFactory() {
		images = new HashMap<>();
	}

	private String id2File(String imgId) {
		switch (imgId) {
		case "imgBck":
			return ("/imagens/back_yugi.jpg");
		default:
			return ("/imagens/"+imgId+".jpg");
		}
	}

	public ImageView createImage(String imgId) {
		Image img = images.get(imgId);
		if (img == null) {
//			img = new Image(id2File(imgId));
			img = new Image(getClass().getResourceAsStream(id2File(imgId)));
			images.put(imgId, img);
		}

		ImageView imgv = new ImageView(img);
		imgv.setFitHeight(128);
		imgv.setPreserveRatio(true);
		imgv.setSmooth(true);
		imgv.setCache(true);
		return imgv;
	}
}
