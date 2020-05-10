package seuil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import code_prof.Lancher;

//source http://helios.mi.parisdescartes.fr/~mclement/teaching/image/tp1/Otsu_.java

public class Otsu {

	public static BufferedImage otsu(String path)throws IOException {
		File file = new File(path);
		BufferedImage ip = null;
		ip = ImageIO.read(file);
		/* Calcul de l'histogramme de l'image */
		int[] histogram = new int[256];
		for (int i = 0; i < ip.getHeight(); i++) {
			for (int j = 0; j < ip.getWidth(); j++) {
				int p = ip.getRGB(j, i);
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;
				int gris = (r + g + b) / 3;
				histogram[gris]++;
			}
		}

		/* Calcul du seuil "optimal" au sens d'Otsu */
		int otsu = 0;
		double omega1, mu1;
		double omega2, mu2;
		double sigma, maxSigma = 0;
		for (int t = 0; t < 256; t++) {

			/* En dessous du seuil courant */
			omega1 = 0; // somme des valeurs de l'histo de 0 Ã t
			mu1 = 0; // moyenne pondÃ©rÃ©e des valeurs de l'histo de 0 Ã t
			for (int i = 0; i <= t; i++) {
				omega1 += histogram[i];
				mu1 += histogram[i] * i;
			}
			mu1 /= omega1;

			/* Idem au dessus du seuil courant */
			omega2 = 0;
			mu2 = 0;
			for (int i = t + 1; i < 256; i++) {
				omega2 += histogram[i];
				mu2 += histogram[i] * i;
			}
			mu2 /= omega2;

			/* Calcul de la variance inter-classe */
			sigma = omega1 * omega2 * Math.pow(mu1 - mu2, 2);
			// On sauvegarde le seuil qui maximise la variance inter-classe
			if (sigma > maxSigma) {
				otsu = t;
				maxSigma = sigma;
			}

		}

		//System.out.println("Seuil Otsu : " + otsu);

		/* Une fois le seuil optimal trouvÃ©, on applique la binarisation */
		return Lancher.threshold(ip, otsu);

	}

}
