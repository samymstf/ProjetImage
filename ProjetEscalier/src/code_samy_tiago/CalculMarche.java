package code_samy_tiago;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import code_prof.*;
import seuil.*;
/*
 * Le code ci-dessous est fait pour être lancé sur l'ensemble des images de la base de données, 
 * c'est a dire les 6 images que le groupe 3 avait sélectionné plus les 7 images de la base de données commune.
 * Il faut soit nommer les images de la forme "escalier_1.jpg", "escalier_2.jpg"...
 * soit modifier le nom du fichier dans le code ligne 46
 * Il faut également modifier la valeur de la constance PATH qui correspond au chemin du dossier 
 * contenant les images. 
 */
public class CalculMarche {
	
	public final static String PATH = "/Users/samymostafa/Desktop/bdd_images/";
	
	public static void main(String[] args) throws IOException {
		/*
		// 1) on créé les images binaires en choisissant le seuil grâce à la méthode des
		// k-means
		// avec k=2
		for (int i = 1; i < 14; i++) {
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
		}*/
		
		// 1) Création des images binaires en choisissant le seuil optimal grâce à la méthode d'Otsu
		for (int i = 1; i < 14; i++) {
			String src = PATH + "escalier_" + i + ".jpg";
			String dst = PATH + "binary_" + i + ".jpg";
			BufferedImage dstImage = Otsu.otsu(src);
			KMeans.saveImage(dst, dstImage);
		}

		// 2) Rognage des images binaires de manière à garder la portion centrale
		for (int i = 1; i < 14; i++) {
			File path = new File(PATH + "binary_" + i + ".jpg");
			BufferedImage binary = ImageIO.read(path);
			BufferedImage rognee = binary.getSubimage(binary.getWidth() / 2 - 25, 0, 50, binary.getHeight());
			KMeans.saveImage(PATH + "rognee_" + i + ".jpg", rognee);
		}

		// 3) Fermeture des images binaires avec un élément structurant de taille 6
		for (int i = 1; i < 14; i++) {
			File path = new File(PATH + "rognee_" + i + ".jpg");
			BufferedImage rognee = ImageIO.read(path);
			BufferedImage close = MorphMath.close(rognee, 6);
			KMeans.saveImage(PATH + "close_t6_" + i + ".jpg", close);
		}

		// 5) Calcul du nombre de marches
		for (int j = 1; j < 14; j++) {
			File path = new File(PATH + "open_t2_" + j + ".jpg");
			BufferedImage erode = ImageIO.read(path);
			int precedent = Color.white.getRGB(), actuel = 0, somme = 0;
			for (int i = 1; i < erode.getHeight(); i++) {
				actuel = erode.getRGB(0, i);
				if (actuel != precedent && actuel == Color.white.getRGB()) {
					somme++;
				}
				precedent = actuel;
			}
			System.out.println("Escalier " + j + " : " + somme + "marches");
		}
	}
}
