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
    }

    private void transaction() {
        Transaction tx = graphDb.beginTx();
        Node owner = graphDb.createNode(Label.label("Person"));

        owner.setProperty("firstName", "baeldung");
        owner.setProperty("lastName", "baeldung");

        Node car = graphDb.createNode(Label.label("Car"));
        car.setProperty("make", "tesla");
        car.setProperty("model", "model3");

        owner.createRelationshipTo(car, RelationshipType.withName("owner"));

        Result result = graphDb.execute(
                "MATCH (c:Car) <-[owner]- (p:Person) " + "WHERE c.make = 'tesla'" + "RETURN p.firstName, p.lastName");

        result.stream().forEach(resultat -> System.out.println(resultat));

        tx.success();
        tx.close();
    }

    private void ajouterImage(String nomFichier, double[][] histogrammes) {
        Transaction tx = graphDb.beginTx();

        Map<String, Object> params = new HashMap<>();

        params.put("chemin", nomFichier);
        params.put("rouge", histogrammeToString(histogrammes[0]));
        params.put("vert", histogrammeToString(histogrammes[1]));
        params.put("bleu", histogrammeToString(histogrammes[2]));

        graphDb.execute("CREATE (i:Image {chemin: $chemin})-[:A_POUR]->(:RGB {rouge: $rouge, vert: $vert, bleu: $bleu})", params);

        Result result = graphDb.execute("MATCH (i: Image) RETURN i.chemin");

        result.stream().forEach(resultat -> System.out.println(resultat));
    }

    private String histogrammeToString(double[] histogramme){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < histogramme.length; i++){
            stringBuilder.append(histogramme[i]);
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }
}
