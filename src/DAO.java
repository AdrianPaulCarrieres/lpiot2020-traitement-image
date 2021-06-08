import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DAO {

    private static DAO instance;

    private GraphDatabaseFactory graphDbFactory;
    private GraphDatabaseService graphDb;

    public static DAO getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new DAO();
            return instance;
        }
    }

    public DAO() {
        this.graphDbFactory = new GraphDatabaseFactory();

        this.graphDb = graphDbFactory.newEmbeddedDatabase(new File("data/images"));

        Transaction tx = graphDb.beginTx();

        graphDb.execute("CREATE CONSTRAINT ON (image:Image) ASSERT image.chemin IS UNIQUE;");
        tx.success();
        tx.close();
    }

    public void nettoyerBDD() {
        Transaction tx2 = graphDb.beginTx();
        graphDb.execute("MATCH (n) DETACH DELETE n;");
        tx2.success();
        tx2.close();
    }

    public void ajouterImage(String nomFichier, double[][] histogrammes) {
        Transaction tx = graphDb.beginTx();

        Map<String, Object> params = new HashMap<>();

        params.put("chemin", nomFichier);
        params.put("rouge", histogrammeToString(histogrammes[0]));
        params.put("vert", histogrammeToString(histogrammes[1]));
        params.put("bleu", histogrammeToString(histogrammes[2]));

        try {
            graphDb.execute(
                    "CREATE (i:Image {chemin: $chemin})-[:A_POUR]->(:RGB {rouge: $rouge, vert: $vert, bleu: $bleu})",
                    params);
            tx.success();
        } catch (Exception e) {
            System.out.println("\nL'image existe déjà\n");
            tx.failure();
        } finally {
            tx.close();
        }
    }

    private String histogrammeToString(double[] histogramme) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < histogramme.length; i++) {
            stringBuilder.append(histogramme[i]);
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public String getImageProche(double[][] histogrammeAComparer) {
        HashMap<String, Float> map = new HashMap<>();

        Transaction tx = graphDb.beginTx();
        Result result = graphDb.execute("MATCH (i: Image)-->(h:RGB) RETURN i.chemin, h");
        result.stream().forEach(resultat -> {

            float moyenne = 0;

            Node node = (Node) resultat.get("h");
            double[] rouge = deProprieteAHistogramme(node.getProperty("rouge").toString());
            moyenne += ImageProjet.calculerDistance(histogrammeAComparer[0], rouge);

            double[] vert = deProprieteAHistogramme(node.getProperty("vert").toString());
            moyenne += ImageProjet.calculerDistance(histogrammeAComparer[1], vert);

            double[] bleu = deProprieteAHistogramme(node.getProperty("bleu").toString());
            moyenne += ImageProjet.calculerDistance(histogrammeAComparer[2], bleu);

            moyenne = moyenne / 3;

            map.put(resultat.get("i.chemin").toString(), moyenne);
        });

        tx.success();
        tx.close();

        LinkedHashMap<String, Float> sortedMap = map.entrySet().stream()
                .sorted((k1, k2) -> k1.getValue().compareTo(k2.getValue()))
                .filter(entry -> entry.getValue() != 0.0)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));



        System.out.println("Les 5 premières");
        Iterator iterator = sortedMap.entrySet().iterator();
        int count = 0;
        while (iterator.hasNext() && count < 5) {
            System.out.println(iterator.next().toString());
            count++;
        }
        System.out.println("Fin");
        String first = sortedMap.entrySet().iterator().next().getKey();
        return first;
    }

    private double[] deProprieteAHistogramme(String propriete) {
        String[] tab = propriete.split(";");
        double[] resultat = new double[tab.length];
        for (int i = 0; i < tab.length; i++) {
            resultat[i] = Double.parseDouble(tab[i]);
        }
        return resultat;
    }

}
