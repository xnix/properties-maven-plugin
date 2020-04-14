package se.xnix;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_RESOURCES;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Goal which print a depinfo.properties file with all dependency versions
 */
@Mojo(name = "property-file", defaultPhase = GENERATE_RESOURCES)
public class PropertyFileMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    @Parameter(name = "outputFile", property = "depfile.outputFile", defaultValue = "${project.build.outputDirectory}/depfile.properties")
    private String outputFile;

    public void execute() throws MojoExecutionException {

        try {
            var file = new File(outputFile);
            createParentDirIfNotExists(file);
            writePropertyFile(file, createProperties(project.getDependencies()));
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + outputFile, e);
        }
    }

    private static void createParentDirIfNotExists(File file) throws IOException {
        var parentDir = Path.of(file.getParent());
        if (Files.notExists(parentDir)) {
            Files.createDirectories(parentDir);
        }
    }

    private static Properties createProperties(List<Dependency> dependencies) {
        var properties = new Properties();
        dependencies.stream().map(Property::fromDependency).forEach(d -> properties.put(d.getKey(), d.getValue()));
        return properties;
    }

    private static void writePropertyFile(File file, Properties properties) throws IOException {
        try (var w = new FileWriter(file)) {
            properties.store(w, null);
        }
    }

}
