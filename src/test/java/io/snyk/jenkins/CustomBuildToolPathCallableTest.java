package io.w3security.jenkins;

import static org.mockito.Mockito.when;

import hudson.remoting.VirtualChannel;
import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CustomBuildToolPathCallableTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private File w3securityToolDirectory;

  @Mock
  private VirtualChannel channel;

  @Test
  public void invoke_shouldNotThrownAnyExceptions_ifToolDirectoryNotContainsToolsWord() {
    when(w3securityToolDirectory.getAbsolutePath())
      .thenReturn("/path-without-t.o.o.l.s-word");
    CustomBuildToolPathCallable customBuildToolPath = new CustomBuildToolPathCallable();

    customBuildToolPath.invoke(w3securityToolDirectory, channel);
  }

  @Test
  public void invoke_shouldNotThrownAnyExceptions_ifToolDirectoryContainsToolsWord() {
    when(w3securityToolDirectory.getAbsolutePath())
      .thenReturn(
        "/opt/jenkins/tools/io.w3security.tools.W3SecurityInstallation"
      );
    CustomBuildToolPathCallable customBuildToolPath = new CustomBuildToolPathCallable();

    customBuildToolPath.invoke(w3securityToolDirectory, channel);
  }
}
