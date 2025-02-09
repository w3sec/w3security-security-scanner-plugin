package io.w3security.jenkins;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.w3security.jenkins.tools.W3SecurityInstallation;
import io.w3security.jenkins.workflow.W3SecuritySecurityStep;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class W3SecurityMonitorTest {

  @Mock
  private Launcher launchMock;

  @Mock
  W3SecurityInstallation installationMock;

  @Mock
  Run<?, ?> buildMock;

  @Mock
  Launcher.ProcStarter starter;

  @Mock
  private TaskListener taskListenerMock;

  @SuppressWarnings("unchecked")
  @Before
  public void before() throws IOException, InterruptedException {
    MockitoAnnotations.openMocks(this);
    when(installationMock.getW3SecurityExecutable(any()))
      .thenReturn("w3security");
    when(launchMock.launch()).thenReturn(starter);
    when(starter.pwd(anyString())).thenReturn(starter);
    when(starter.cmds(anyString())).thenReturn(starter);
    when(starter.envs(any(Map.class))).thenReturn(starter);
    when(starter.stdout(any(OutputStream.class))).thenReturn(starter);
    when(starter.stderr(any())).thenReturn(starter);
    when(taskListenerMock.getLogger()).thenReturn(System.out);
    when(buildMock.getEnvironment(any())).thenReturn(new EnvVars());
  }

  @Test
  public void testMonitorShouldFailOnErrorIfConfigIsSet()
    throws IOException, InterruptedException { // mock setup
    when(starter.join()).thenReturn(2);

    W3SecurityContext context = W3SecurityContext.forFreestyleProject(
      buildMock,
      null,
      launchMock,
      taskListenerMock
    );
    W3SecuritySecurityStep config = new W3SecuritySecurityStep();
    config.setFailOnError(true);

    try (
      MockedStatic<PluginMetadata> pluginMetadataMockedStatic = Mockito.mockStatic(
        PluginMetadata.class
      )
    ) {
      pluginMetadataMockedStatic
        .when(PluginMetadata::getIntegrationVersion)
        .thenReturn("1.2.3");
      W3SecurityMonitor.monitorProject(
        context,
        config,
        installationMock,
        "token"
      );
      Assert.fail("Expected RuntimeException, but didn't get one");
    } catch (RuntimeException ignored) {
      // expected
    }
  }

  @Test
  public void testMonitorShouldNotFailOnErrorIfConfigIsNotSet()
    throws IOException, InterruptedException { // mock setup
    when(starter.join()).thenReturn(2);

    W3SecurityContext context = W3SecurityContext.forFreestyleProject(
      buildMock,
      null,
      launchMock,
      taskListenerMock
    );
    W3SecuritySecurityStep config = new W3SecuritySecurityStep();
    config.setFailOnError(false);

    try (
      MockedStatic<PluginMetadata> pluginMetadataMockedStatic = Mockito.mockStatic(
        PluginMetadata.class
      )
    ) {
      pluginMetadataMockedStatic
        .when(PluginMetadata::getIntegrationVersion)
        .thenReturn("1.2.3");
      W3SecurityMonitor.monitorProject(
        context,
        config,
        installationMock,
        "token"
      );
    }
  }

  @Test
  public void testMonitorShouldNotFailIfErrorCodeIs0AndConfigIsSet()
    throws IOException, InterruptedException { // mock setup
    when(starter.join()).thenReturn(0);

    W3SecurityContext context = W3SecurityContext.forFreestyleProject(
      buildMock,
      null,
      launchMock,
      taskListenerMock
    );
    W3SecuritySecurityStep config = new W3SecuritySecurityStep();
    config.setFailOnError(true);

    try (
      MockedStatic<PluginMetadata> pluginMetadataMockedStatic = Mockito.mockStatic(
        PluginMetadata.class
      )
    ) {
      pluginMetadataMockedStatic
        .when(PluginMetadata::getIntegrationVersion)
        .thenReturn("1.2.3");
      W3SecurityMonitor.monitorProject(
        context,
        config,
        installationMock,
        "token"
      );
    }
  }

  @Test
  public void testMonitorShouldNotFailIfErrorCodeIs0AndConfigIsNotSet()
    throws IOException, InterruptedException { // mock setup
    when(starter.join()).thenReturn(0);

    W3SecurityContext context = W3SecurityContext.forFreestyleProject(
      buildMock,
      null,
      launchMock,
      taskListenerMock
    );
    W3SecuritySecurityStep config = new W3SecuritySecurityStep();
    config.setFailOnError(false);

    try (
      MockedStatic<PluginMetadata> pluginMetadataMockedStatic = Mockito.mockStatic(
        PluginMetadata.class
      )
    ) {
      pluginMetadataMockedStatic
        .when(PluginMetadata::getIntegrationVersion)
        .thenReturn("1.2.3");
      W3SecurityMonitor.monitorProject(
        context,
        config,
        installationMock,
        "token"
      );
    }
  }
}
