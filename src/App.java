import fr.unistra.pelican.Image;

public class App {
    public static void main(String[] args) throws Exception {
        ImageProjet p = new ImageProjet();
        //Utiliser le file explorer
        Image img = p.lectureImage("./images/maldive.jpg");
        //Cr�er histo de couleurs
        p.histogramme(img);
    }
}
