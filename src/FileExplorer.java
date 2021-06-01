import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileExplorer {
    public static void main(String... args) throws Exception {
        String[] tab = getToImportFilesPath();
        for(int i = 0; i < tab.length ; i++){
            System.out.println(tab[i]);
        }
    }

    public static String[] getToImportFilesPath() throws IOException {
        Path dir = Paths.get("./images");
        return Files.walk(dir) // Récupère tous les dossiers et fichiers sous ./images (un peu comme un ls -R)
                .map(path -> path.toFile()) // Fais un tableau contenant le retour de la fonction toFile() sur chaque
                                            // path
                .filter(file -> !file.isDirectory()) // Filtre en dehors du tableau tous les fichiers étant des
                                                     // répertoire
                .map(file -> file.toPath().toString()) // Fais un tableau contenant le chemin des fichiers uniquement
                .toArray(String[]::new); //

    }
}
