import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import java.io.File;

public class DAO {

    private static DAO instance;

    public static DAO getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new DAO();
            return instance;
        }
    }

    public DAO() {
        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();

        GraphDatabaseService graphDb = graphDbFactory.newEmbeddedDatabase(new File("data/images"));

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

}
