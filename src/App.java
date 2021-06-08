import fr.unistra.pelican.Image;

public class App {
    public static void main(String[] args) throws Exception {
        DAO dao = DAO.getInstance();
        dao.nettoyerBDD();
        String[] fileNames = FileExplorer.getToImportFilesPath();

        for (String fileName : fileNames) {
            ImageProjet p = new ImageProjet();
            Image img = p.lectureImage(fileName);
            int len = img.getXDim()*img.getYDim();
            double[][] histogrammes = p.histogrammeNormalise(p.histogramme(img), len);

            dao.ajouterImage(fileName, histogrammes);
        }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        ImageProjet p = new ImageProjet();
        Image img = p.lectureImage("./images/001.jpg");
        int len = img.getXDim()*img.getYDim();
        double[][] histogrammes = p.histogrammeNormalise(p.histogramme(img), len);
        System.out.println(dao.getImageProche(histogrammes));

    }
}
