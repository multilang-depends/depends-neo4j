package depends.neo4j;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "depends-neo4j")
public class DependsNeo4jCommand {
	@Parameters(index = "0", description = "The result file(json format) to be imported")
    private String resultFile;
    @Option(names = {"-h","--help"}, usageHelp = true, description = "display this help and exit")
    boolean help;
   
    @Option(names = {"--username"}, description = "user name of neo4j db")
    private String user = "neo4j";
    @Option(names = {"--password"}, description = "password of neo4j db")
	private String password = "test";
    
    @Option(names = {"--uri"}, description = "uri of neo4j db")
	private String uri = "bolt://localhost:7687";

    @Option(names = {"--purge-db"}, description = "clean all data in the db")
    private boolean purgeDb=false;
    
    
	public boolean isHelp() {
		return help;
	}


	public String getResultFile() {
		return resultFile;
	}


	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}


	public String getUser() {
		return user;
	}


	public String getUri() {
		return uri;
	}


	public boolean isPurgeDb() {
		return purgeDb;
	}
	
}
