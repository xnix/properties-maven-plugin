package se.xnix;

import static org.apache.maven.plugins.annotations.LifecyclePhase.INITIALIZE;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "properties", requiresDependencyResolution = TEST, defaultPhase = INITIALIZE, threadSafe = true)
public class PropertiesMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    @Override
    public void execute() {
        var properties = project.getProperties();
        project.getDependencies().stream().map(Property::fromDependency).forEach(p -> properties.put(p.getKey(), p.getValue()));
    }
}
