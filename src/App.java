import fr.unistra.pelican.Image;

public class App {
    public static void main(String[] args) throws Exception {
        ImageProjet p = new ImageProjet();
        //Utiliser le file explorer
        Image img = p.lectureImage("C:\\Users\\cleme\\git\\lpiot2020-traitement-image\\.\\images\\maldive.jpg");
        //Créer histo de couleurs
        p.histogramme(img);
    }
}
