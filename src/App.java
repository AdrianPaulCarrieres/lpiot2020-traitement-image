import fr.unistra.pelican.Image;

public class App {
    public static void main(String[] args) throws Exception {
        DAO dao = DAO.getInstance();

        String[] fileNames = FileExplorer.getToImportFilesPath();

        // for(String fileName : fileNames) {
        //     ImageProjet p = new ImageProjet();
        //     Image img = p.lectureImage(fileName);
        //     double[][] histogrammes = p.histogramme(img);

        //     dao.ajouterImage(fileName, histogrammes);
        // }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        dao.getHistogrammes();

        
        
    }
}
