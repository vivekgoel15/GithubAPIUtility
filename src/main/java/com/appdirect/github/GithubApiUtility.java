package com.appdirect.github;

import com.appdirect.rest.RestHandler;
import com.appdirect.utils.GithubApiHelper;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringOptionHandler;

/**
 * This utility can be used to change assignee of Github pull requests or add
 * label to pull requests. Created with IntelliJ IDEA.
 * User: vigoel
 */
public class GithubApiUtility {

    private static final Logger LOGGER = Logger.getLogger(GithubApiUtility.class);

    @Option(name = "-username", required = true, usage = "Github API user", handler = StringOptionHandler.class)
	private static String username;

	@Option(name = "-accessToken", required = true, usage = "Github API private access token", handler = StringOptionHandler.class)
	private static String accessToken;

	/**
	 * Main function to begin the flow of utility operations.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		GithubApiUtility parser = new GithubApiUtility();
		parser.doMain(args);
	}

	/**
	 * Performs operations e.g. assignee update or label add on pull requests.
	 * 
	 * @param args
	 */
	public void doMain(String args[]) {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
            LOGGER.error("Invalid arguments", e);
			System.exit(1);
		}
        GithubApiHelper.setRestHandler(new RestHandler(username, accessToken));
        GithubApiHelper.processPRs();
	}

}