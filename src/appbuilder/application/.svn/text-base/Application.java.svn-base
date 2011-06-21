package appbuilder.application;

import java.util.Collection;
import java.util.Map;

import appbuilder.application.data.DataSource;
import appbuilder.application.util.Toolkit;
import java.util.List;

/**
 * This class models the basic metadata needed to build a simple CRUD 
 * application.
 * 
 * @author Bruno Gama Catão
 * @since 2007-12-27
 */
public class Application {

    private String name;
    private String description;
    private String version;
    private String author;
    private DataSource dataSource;
    private String basePackage;
    private String baseDirectory;
    private String sourceDirectory;
    private String buildDirectory;
    private String distributionDirectory;
    private String libDirectory;
    private String mainClass;
    private Map<String, String> messages;
    private List<String> libraries;

    public Application(String name) {
        this.name = name;
        this.messages = Toolkit.createMap();
        this.libraries = Toolkit.createList();
    }
    
    public void addLibrary(String library) {
        libraries.add(library);
    }
    
    public List<String> getLibraries() {
        return libraries;
    }

    public void addMessage(String key, String value) {
        messages.put(key, value);
    }

    public String getMessage(String key) {
        return messages.get(key);
    }

    public boolean containsMessage(String key) {
        return messages.containsKey(key);
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public Collection<String> getMessageKeys() {
        return messages.keySet();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public String getBuildDirectory() {
        return buildDirectory;
    }

    public void setBuildDirectory(String buildDirectory) {
        this.buildDirectory = buildDirectory;
    }

    public String getDistributionDirectory() {
        return distributionDirectory;
    }

    public void setDistributionDirectory(String distributionDirectory) {
        this.distributionDirectory = distributionDirectory;
    }

    public String getLibDirectory() {
        return libDirectory;
    }

    public void setLibDirectory(String libDirectory) {
        this.libDirectory = libDirectory;
    }
    
    public String getMainClass() {
        return mainClass;
    }
    
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }
}
