package code_samy_tiago;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import code_prof.*;
import seuil.KMeans;

public class CalculMarche {

	public static void main(String[] args) throws IOException {
		// 1) on créé les images binaires en choisissant le seuil grâce à la méthode des k-means
		// avec k=2
		for (int i = 1; i < 7; i++) {
			String src = "/Users/samymostafa/Desktop/bdd_images/escalier_" + i + ".jpg";
			String dst = "/Users/samymostafa/Desktop/bdd_images/binary_" + i + ".jpg";
			int k = 2;
			String m = "-i";
			int mode = 1;
			if (m.equals("-i")) {
				mode = KMeans.MODE_ITERATIVE;
			} else if (m.equals("-c")) {
				mode = KMeans.MODE_CONTINUOUS;
			}
			KMeans kmeans = new KMeans();
			BufferedImage dstImage = kmeans.calculate(KMeans.loadImage(src), k, mode);
			KMeans.saveImage(dst, dstImage);
		}

		// 2) on rogne les images binaires de manière à garder la portion centrale
		for (int i = 1; i < 7; i++) {
			File path = new File("/Users/samymostafa/Desktop/bdd_images/binary_" + i + ".jpg");
			BufferedImage binary = ImageIO.read(path);
			BufferedImage rognee = binary.getSubimage(binary.getWidth() / 2 - 25, 0, 50, binary.getHeight());
			KMeans.saveImage("/Users/samymostafa/Desktop/bdd_images/rognee_" + i + ".jpg", rognee);
		}

		// 3)Fermeture des images binaires
		for (int i = 1; i < 7; i++) {
			File path = new File("/Users/samymostafa/Desktop/bdd_images/rognee_" + i + ".jpg");
			BufferedImage rognee = ImageIO.read(path);
			BufferedImage close = MorphMath.close(rognee, 6);
			KMeans.saveImage("/Users/samymostafa/Desktop/bdd_images/close_t6_" + i + ".jpg", close);
		}
		
		// 4l calcule du nombre de marche
		for (int j = 1; j < 7; j++) {
			File path = new File("/Users/samymostafa/Desktop/bdd_images/close_t6_"+j+".jpg");
			BufferedImage erode = ImageIO.read(path);
			int precedent = Color.white.getRGB(), actuel = 0, somme = 0;
			for (int i = 1; i < erode.getHeight(); i++) {
				actuel = erode.getRGB(0, i);
				if (actuel != precedent && actuel == Color.white.getRGB()) {
					somme++;
				}
				precedent = actuel;
			}
			System.out.println("Escalier "+j+" : "+somme+"marches");
		}
	}

}
