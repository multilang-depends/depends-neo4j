package depends.neo4j;

import static org.neo4j.driver.v1.Values.parameters;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import depends.matrix.relation.DependenciesRelation;
import depends.matrix.relation.DependencyPair;
import depends.matrix.relation.DependencyValue;

public class Neo4jNativeWritter implements AutoCloseable {
	private final Driver driver;
	private boolean isPurgeDb = false;
	public Neo4jNativeWritter(DependsNeo4jCommand app) {
		isPurgeDb = app.isPurgeDb();
		driver = GraphDatabase.driver(app.getUri(), AuthTokens.basic(app.getUser(), app.getPassword()));
	}

	public void write(DependenciesRelation relations) {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx)

				{
					if (isPurgeDb)
						deleteAllDataInDB(tx);
					writeData(tx, relations);
					return "";
				}

				private void deleteAllDataInDB(Transaction tx) {
					tx.run("MATCH (n) DETACH DELETE n");
				}

				private void writeData(Transaction tx, DependenciesRelation relations) {
					String[] variables = relations.getVariables();
					int id = 0;
					for (String entityName : variables) {
						tx.run("CREATE (e:Entity{id: $id, name: $name}) ", parameters("id", id, "name", entityName));
						id++;
					}

					for (DependencyPair pair : relations.getPairs().values()) {
						for (DependencyValue dep : pair.getDependencies()) {
							String cql = "MATCH (e1:Entity),(e2:Entity) " + "WHERE e1.id=$id1 AND e2.id=$id2 "
									+ "CREATE (e1)-[r: " + dep.getType() + "{weight:$weight}]->(e2) " + "RETURN r";
							tx.run(cql,
									parameters("id1", pair.getFrom(), "id2", pair.getTo(), "weight", dep.getWeight()));
						}
					}
				}
			});
		}
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}
}
