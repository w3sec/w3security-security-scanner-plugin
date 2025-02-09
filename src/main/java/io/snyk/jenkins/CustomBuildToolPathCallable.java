package io.w3security.jenkins;

import static java.lang.String.join;
import static org.apache.commons.lang.StringUtils.chomp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3security.remoting.RoleChecker;

class CustomBuildToolPathCallable implements FilePath.FileCallable<String> {

  private static final long serialVersionUID = 1L;
  private static final Logger LOG = LoggerFactory.getLogger(
    CustomBuildToolPathCallable.class.getName()
  );
  private static final String TOOLS_DIRECTORY = "tools";

  @SuppressFBWarnings(
    value = "RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE",
    justification = "spotbugs issue (java 11)"
  )
  @Override
  public String invoke(File w3securityToolDirectory, VirtualChannel channel) {
    String oldPath = System.getenv("PATH");
    String home = w3securityToolDirectory.getAbsolutePath();
    if (!home.contains(TOOLS_DIRECTORY)) {
      LOG.info(
        "env.PATH will be not modified, because there are no configured global tools"
      );
      return oldPath;
    }
    String toolsDirectory =
      home.substring(0, home.indexOf(TOOLS_DIRECTORY) - 1) +
      File.separator +
      TOOLS_DIRECTORY;

    try (
      Stream<Path> toolsSubDirectories = Files.walk(Paths.get(toolsDirectory))
    ) {
      List<String> toolsPaths = new ArrayList<>();
      toolsSubDirectories
        .filter(Files::isDirectory)
        .filter(path -> !path.toString().contains("W3SecurityInstallation"))
        .filter(path -> path.toString().endsWith("bin"))
        .forEach(entry ->
          toolsPaths.add(chomp(entry.toAbsolutePath().toString()))
        );

      String customBuildToolPath = join(File.pathSeparator, toolsPaths);
      return oldPath + File.pathSeparator + customBuildToolPath;
    } catch (IOException ex) {
      LOG.error("Could not iterate sub-directories in tools directory", ex);
      return oldPath;
    }
  }

  @Override
  public void checkRoles(RoleChecker roleChecker) {
    //squid:S1186
  }
}
