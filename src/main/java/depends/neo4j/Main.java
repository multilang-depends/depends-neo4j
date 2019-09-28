package depends.neo4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import depends.matrix.loader.DependencyMatrixJsonLoader;
import depends.matrix.loader.TypeMapping;
import depends.matrix.relation.DependenciesRelation;
import picocli.CommandLine;

public class Main {

	public static void main(String[] args) {
		try {
			DependsNeo4jCommand app = CommandLine.populateCommand(new DependsNeo4jCommand(), args);
			if (app.isHelp()) {
				CommandLine.usage(new DependsNeo4jCommand(), System.out);
				System.exit(0);
			}
			executeCommand(app);
			System.exit(0);
		} catch (Exception e) {
			System.err.println("Exception encountered" + e.getMessage());
			CommandLine.usage(new DependsNeo4jCommand(), System.out);
			System.exit(0);
		}
		
	}

	private static void executeCommand(DependsNeo4jCommand app) throws Exception {
		Neo4jNativeWritter writter = new Neo4jNativeWritter(app);
		DependencyMatrixJsonLoader loader =new DependencyMatrixJsonLoader();
		Path path = Paths.get(app.getResultFile());
		DependenciesRelation matrix = loader.loadDependencyMatrix(path,new ArrayList<>(), new TypeMapping(), 0);
		writter.write(matrix);
		writter.close();
	}

}
