import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    public void ajouterImage(String nomFichier, double[][] histogrammes) {
        Transaction tx = graphDb.beginTx();

        Map<String, Object> params = new HashMap<>();

        params.put("chemin", nomFichier);
        params.put("rouge", histogrammeToString(histogrammes[0]));
        params.put("vert", histogrammeToString(histogrammes[1]));
        params.put("bleu", histogrammeToString(histogrammes[2]));

        try {
            graphDb.execute("CREATE (i:Image {chemin: $chemin})-[:A_POUR]->(:RGB {rouge: $rouge, vert: $vert, bleu: $bleu})", params);
            tx.success();
        } catch (Exception e) {
            System.out.println("\nL'image existe déjà\n");
            tx.failure();
        } finally {
            tx.close();
        }
    }

    private String histogrammeToString(double[] histogramme){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < histogramme.length; i++){
            stringBuilder.append(histogramme[i]);
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }


    public void getHistogrammes(){
        HashMap<String, Float> map = new HashMap<>();

        Transaction tx = graphDb.beginTx();
        Result result = graphDb.execute("MATCH (i: Image)-->(h:RGB) RETURN i.chemin, h LIMIT 5");
        result.stream().forEach(resultat -> {
            //System.out.println(resultat.keySet());
            Node node = (Node) resultat.get("h");
            System.out.println(node.getProperty("rouge"));
            float moyenne = 0;
            map.put(resultat.get("i.chemin").toString(), moyenne);
        });
        result.stream().toArray(Node[]::new);

        
        tx.success();
        tx.close();
    }


}
