import fr.unistra.pelican.Image;

public class App {
    public static void main(String[] args) throws Exception {
        DAO dao = DAO.getInstance();
        //dao.nettoyerBDD();
        String[] fileNames = FileExplorer.getToImportFilesPath();

        // for (String fileName : fileNames) {
        //     ImageProjet p = new ImageProjet();
        //     Image img = p.lectureImage(fileName);
        //     double[][] histogrammes = p.histogramme(img);

        //     dao.ajouterImage(fileName, histogrammes);
        // }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        ImageProjet c = new ImageProjet();
        Image img = c.lectureImage("./images/001.jpg");
        double[][] histogrammes = c.histogramme(img);
        System.out.println(dao.getImageProche(histogrammes));

    }
}
