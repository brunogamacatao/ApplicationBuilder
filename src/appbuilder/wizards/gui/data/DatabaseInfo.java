package appbuilder.wizards.gui.data;

public class DatabaseInfo implements Comparable<DatabaseInfo> {
    private int order;
    private String name;
    private String driver;
    private String url;
    private String driverPath;

    public DatabaseInfo() {
        this(0, "");
    }

    public DatabaseInfo(int order, String name) {
        this(order, name, "", "", "");
    }

    public DatabaseInfo(int order, String name, String driver, String url,
            String driverPath) {
        this.order = order;
        this.name = name;
        this.driver = driver;
        this.url = url;
        this.driverPath = driverPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    @Override
    public int compareTo(DatabaseInfo info) {
        return this.getOrder() - info.getOrder();
    }
}
