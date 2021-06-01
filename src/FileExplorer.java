import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class FileExplorer {
    public static void main(String... args) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        Path dir = Paths.get("./images");
        Files.walk(dir) //Récupère tous les dossiers et fichiers sous ./images (un peu comme un ls -R)
            .map(path -> path.toFile()) //Fais un tableau contenant le retour de la fonction toFile() sur chaque path
            .filter(file -> !file.isDirectory()) //Filtre en dehors du tableau tous les fichiers étant des répertoire
            .forEach(file -> map.put(file.getPath(), file.getName()));    //Fais une liste contenant une map (chemin du fichier -> fichier)

        System.out.println(map.size());
    }
}
