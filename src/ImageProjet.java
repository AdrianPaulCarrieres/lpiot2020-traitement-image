import java.io.IOException;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;


public class ImageProjet {

	public Image lectureImage(String adresse) {
		// Charger une image en mÃ©moire
		Image img = ImageLoader.exec(adresse);

		// Connaitre la hauteur et la largeur d'une image
		int largeur = img.getXDim();
		int hauteur = img.getYDim();

		System.out.println("Image chargée. Sa taille est " + largeur + " x " + hauteur + " pixels");

		// Afficher une image
		boolean color = img.getBDim() == 1 ? false : true;
		img.setColor(color); // si false => affichage de chaque canal, si true => affichage d'une image
								// couleur
		// Viewer2D.exec(img);

		return img;
	}

	public void histogramme(Image img) {
		if (img.getBDim() == 1) {

			double[] tab = new double[256];
			for (int i = 0; i < 256; ++i) {
				tab[i] = 0;
			}
			int niveauGris;
			for (int x = 0; x < img.getXDim(); ++x) {
				for (int y = 0; y < img.getYDim(); ++y) {
					niveauGris = img.getPixelXYByte(x, y);
					tab[niveauGris] = ++tab[niveauGris];
				}
			}
		
			try {
				HistogramTools.plotHistogram(tab);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			double[] tabR = new double[256];
			double[] tabG = new double[256];
			double[] tabB = new double[256];
			for (int i = 0; i < 256; ++i) {
				tabR[i] = 0;
				tabG[i] = 0;
				tabB[i] = 0;
			}
			int couleurR, couleurG, couleurB;
			for (int x = 0; x < img.getXDim(); ++x) {
				for (int y = 0; y < img.getYDim(); ++y) {
					
					couleurR = img.getPixelXYBByte(x, y, 0);
					couleurG = img.getPixelXYBByte(x, y, 1);
					couleurB = img.getPixelXYBByte(x, y, 2);
					tabR[couleurR] = ++tabR[couleurR];
					tabG[couleurG] = ++tabG[couleurG];
					tabB[couleurB] = ++tabB[couleurB];
					
					
				}
			}
			try {
				HistogramTools.plotHistogram(tabR);
				HistogramTools.plotHistogram(tabG);
				HistogramTools.plotHistogram(tabB);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
