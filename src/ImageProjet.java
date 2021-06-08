import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class ImageProjet {

	public Image lectureImage(String adresse) {
		// Charger une image en mémoire
		Image img = ImageLoader.exec(adresse);

		// Connaitre la hauteur et la largeur d'une image
		int largeur = img.getXDim();
		int hauteur = img.getYDim();

		System.out.println("Image chargee. Sa taille est " + largeur + " x " + hauteur + " pixels");

		// Afficher une image
		boolean color = img.getBDim() == 1 ? false : true;
		img.setColor(color); // si false => affichage de chaque canal, si true => affichage d'une image
								// couleur
		// Viewer2D.exec(img);

		return img;
	}

	public double[][] histogramme(Image img) {
		double[][] tabHisto = new double[3][256];

		for (int j = 0; j < 3; ++j) {
			for (int i = 0; i < 256; ++i) {
				tabHisto[j][i] = 0;
			}

		}

		int couleur;
		for (int i = 0; i < img.getBDim() && i < 3; ++i) { /* eviter le 4e channel de transparence*/
			for (int x = 0; x < img.getXDim(); ++x) {
				for (int y = 0; y < img.getYDim(); ++y) {

					couleur = img.getPixelXYBByte(x, y, i);
					tabHisto[i][couleur] = ++tabHisto[i][couleur];

				}
			}

		}
		/*
		 * // valide pr RGB try { HistogramTools.plotHistogramPerso(tabHisto[0],
		 * "rouge"); HistogramTools.plotHistogramPerso(tabHisto[1], "vert");
		 * HistogramTools.plotHistogramPerso(tabHisto[2], "bleu"); } catch (IOException
		 * e) {
		 * 
		 * e.printStackTrace(); }
		 */

		return tabHisto;
	}

	public double[][] histogrammeDiscretise(double[][] histos) {
		double[][] histosDiscretises = new double[3][26];

		for (int j = 0; j < 3; ++j) {
			for (int i = 0; i < 26; ++i) {
				histosDiscretises[j][i] = 0;
			}

		}

		for (int k = 0; k < 3; ++k) {
			for (int i = 1; i <= 26; ++i) {
				for (int j = 1; j <= 10; ++j) {
					if (i * j < 256)
						histosDiscretises[k][i - 1] += histos[k][i * j];
				}
			}

		}
		/*
		 * try { HistogramTools.plotHistogramPerso(histosDiscretises[0], "rouge");
		 * HistogramTools.plotHistogramPerso(histosDiscretises[1], "vert");
		 * HistogramTools.plotHistogramPerso(histosDiscretises[2], "bleu"); } catch
		 * (IOException e) {
		 * 
		 * e.printStackTrace(); }
		 */
		return histosDiscretises;

	}

	public double[][] histogrammeNormalise(double[][] histos, int len) {
		int l = histos[0].length;
		System.out.println(len);
		double[][] histosNormalises = new double[3][l];

		for (int j = 0; j < 3; ++j) {
			for (int i = 0; i < 26; ++i) {
				histosNormalises[j][i] = 0;
			}

		}

		for (int k = 0; k < 3; ++k) {
			for (int i = 0; i < 256; ++i) {
				histosNormalises[k][i] = histos[k][i] / len;

			}

		}
		/*
		 * try { HistogramTools.plotHistogramPerso(histosNormalises[0], "rouge");
		 * HistogramTools.plotHistogramPerso(histosNormalises[1], "vert");
		 * HistogramTools.plotHistogramPerso(histosNormalises[2], "bleu"); } catch
		 * (IOException e) {
		 * 
		 * e.printStackTrace(); }
		 */
		return histosNormalises;

	}

	public Image filtreMedian(Image img) {

		ByteImage new_img, new_img_bordure;

		if (img.getBDim() == 1) {
			new_img_bordure = new ByteImage(img.getXDim() + 2, img.getYDim() + 2, 1, 1, 1);
			for (int x = 1; x < new_img_bordure.getXDim() - 1; ++x) {
				for (int y = 1; y < new_img_bordure.getYDim() - 1; ++y) {

					int couleur = (img.getPixelXYBByte(x - 1, y - 1, 0));
					new_img_bordure.setPixelXYBByte(x, y, 0, couleur);
				}
			}
			ArrayList<Integer> couleurs = new ArrayList<>();
			new_img = new ByteImage(img.getXDim(), img.getYDim(), 1, 1, 1);
			for (int x = 0; x < new_img.getXDim(); ++x) {
				for (int y = 0; y < new_img.getYDim(); ++y) {

					for (int i = 0; i < 3; ++i) {
						for (int j = 0; j < 3; ++j) {
							couleurs.add(new_img_bordure.getPixelXYByte(x + i, y + j));
						}
					}
					Collections.sort(couleurs);
					new_img.setPixelXYBByte(x, y, 0, couleurs.get(5));
					couleurs.clear();
				}
			}

		} else {
			int couleurR, couleurG, couleurB;
			System.out.println("B Dim : " + img.getBDim());
			new_img_bordure = new ByteImage(img.getXDim() + 2, img.getYDim() + 2, 1, 1, 3);
			for (int x = 1; x < new_img_bordure.getXDim() - 1; ++x) {
				for (int y = 1; y < new_img_bordure.getYDim() - 1; ++y) {

					couleurR = (img.getPixelXYBByte(x - 1, y - 1, 0));
					new_img_bordure.setPixelXYBByte(x, y, 0, couleurR);
					couleurG = (img.getPixelXYBByte(x - 1, y - 1, 1));
					new_img_bordure.setPixelXYBByte(x, y, 1, couleurG);
					couleurB = (img.getPixelXYBByte(x - 1, y - 1, 2));
					new_img_bordure.setPixelXYBByte(x, y, 2, couleurB);
				}
			}
			ArrayList<Integer> couleursR = new ArrayList<>();
			ArrayList<Integer> couleursG = new ArrayList<>();
			ArrayList<Integer> couleursB = new ArrayList<>();
			new_img = new ByteImage(img.getXDim(), img.getYDim(), 1, 1, 3);
			for (int x = 0; x < new_img.getXDim(); ++x) {
				for (int y = 0; y < new_img.getYDim(); ++y) {

					for (int i = 0; i < 3; ++i) {
						for (int j = 0; j < 3; ++j) {
							couleursR.add(new_img_bordure.getPixelXYBByte(x + i, y + j, 0));
							couleursG.add(new_img_bordure.getPixelXYBByte(x + i, y + j, 1));
							couleursB.add(new_img_bordure.getPixelXYBByte(x + i, y + j, 2));
						}
					}
					Collections.sort(couleursR);
					Collections.sort(couleursG);
					Collections.sort(couleursB);
					new_img.setPixelXYBByte(x, y, 0, couleursR.get(5));
					new_img.setPixelXYBByte(x, y, 1, couleursG.get(5));
					new_img.setPixelXYBByte(x, y, 2, couleursB.get(5));
					couleursR.clear();
					couleursG.clear();
					couleursB.clear();
				}
			}
		}
		/*
		boolean color2 = new_img.getBDim() == 1 ? false : true;
		new_img.setColor(color2);
		Viewer2D.exec(new_img);*/
		return new_img;

	}

	public static double calculerDistance(double[] histogramme_1, double[] histogramme_2) {
        double resultat = 0;
        for (int i = 0; i < histogramme_1.length; i++) {
            resultat += histogramme_1[i] - histogramme_2[i];
        }
        return resultat;
    }
}
