import fr.unistra.pelican.Image;

public class App {
    public static void main(String[] args) throws Exception {

        //String[] fileNames = FileExplorer.getToImportFilesPath();

        /*for(String fileName : fileNames) {
            ImageProjet p = new ImageProjet();
            Image img = p.lectureImage(fileName);
            p.histogramme(img);
        }*/

        DAO dao = DAO.getInstance();
        
    }
}
