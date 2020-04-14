package se.xnix;

import org.apache.maven.model.Dependency;

class Property {

    private final String key;
    private final String value;

    private Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    static Property fromDependency(Dependency dependency) {
        return new Property(createVersionKey(dependency), dependency.getVersion());
    }

    String getKey() {
        return key;
    }

    String getValue() {
        return value;
    }

    private static String createVersionKey(Dependency dependency) {
        return dependency.getGroupId() + ":" + dependency.getArtifactId() + ":" + dependency.getType() + ":version";
    }
}
