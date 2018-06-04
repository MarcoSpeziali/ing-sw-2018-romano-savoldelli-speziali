package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Encapsulates the client's machine information.
 */
public class ClientMachineInfo implements JSONSerializable {
    private static final long serialVersionUID = -2994818791895152190L;

    /**
     * The java version.
     */
    private String javaVersion;

    /**
     * The java vendor.
     */
    private String javaVendor;

    /**
     * The jvm version.
     */
    private String javaVMVersion;

    /**
     * The operating system name.
     */
    private String osName;

    /**
     * The operating system version.
     */
    private String osVersion;

    /**
     * The operating system architecture.
     */
    private String osArchitecture;


    /**
     * The user name.
     */
    private String userName;

    /**
     * The user locale.
     */
    private Locale userLocale;

    /**
     * @return the java version
     */
    public String getJavaVersion() {
        return javaVersion;
    }

    /**
     * Sets the java version.
     * @param javaVersion the java version
     */
    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    /**
     * @return the java vendor
     */
    public String getJavaVendor() {
        return javaVendor;
    }

    /**
     * Sets the java vendor.
     * @param javaVendor the java vendor
     */
    public void setJavaVendor(String javaVendor) {
        this.javaVendor = javaVendor;
    }

    /**
     * @return the jvm version
     */
    public String getJavaVMVersion() {
        return javaVMVersion;
    }

    /**
     * Sets the jvm version.
     * @param javaVMVersion the jvm version
     */
    public void setJavaVMVersion(String javaVMVersion) {
        this.javaVMVersion = javaVMVersion;
    }

    /**
     * @return the operating system name
     */
    public String getOsName() {
        return osName;
    }

    /**
     * Sets the operating system name.
     * @param osName the operating system name
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * @return the operating system version
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * Sets the operating system version.
     * @param osVersion the operating system version
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * @return the operating system architecture
     */
    public String getOsArchitecture() {
        return osArchitecture;
    }

    /**
     * Sets the operating system architecture.
     * @param osArchitecture the operating system architecture
     */
    public void setOsArchitecture(String osArchitecture) {
        this.osArchitecture = osArchitecture;
    }

    /**
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the user locale
     */
    public Locale getUserLocale() {
        return userLocale;
    }

    /**
     * Sets the user locale.
     * @param userLocale the user locale
     */
    public void setUserLocale(Locale userLocale) {
        this.userLocale = userLocale;
    }

    /**
     * @return an instance of {@link ClientMachineInfo} initialized with the system properties.
     */
    public static ClientMachineInfo generate() {
        return new ClientMachineInfo(
                System.getProperty("java.version"),
                System.getProperty("java.vendor"),
                System.getProperty("java.vm.version"),
                System.getProperty("os.name"),
                System.getProperty("os.arch"),
                System.getProperty("os.version"),
                System.getProperty("user.name"),
                Locale.getDefault()
        );
    }

    public ClientMachineInfo() { }

    /**
     * @param javaVersion the java version
     * @param javaVendor the java vendor
     * @param javaVMVersion the jvm version
     * @param osName the operating system name
     * @param osVersion the operating system version
     * @param osArchitecture the operating system architecture
     * @param userName the user name
     * @param userLocale the user locale
     */
    public ClientMachineInfo(String javaVersion, String javaVendor, String javaVMVersion, String osName, String osVersion, String osArchitecture, String userName, Locale userLocale) {
        this.javaVersion = javaVersion;
        this.javaVendor = javaVendor;
        this.javaVMVersion = javaVMVersion;

        this.osName = osName;
        this.osVersion = osVersion;
        this.osArchitecture = osArchitecture;

        this.userName = userName;
        this.userLocale = userLocale;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.javaVersion = jsonObject.getString("java.version");
        this.javaVendor = jsonObject.getString("java.vendor");
        this.javaVMVersion = jsonObject.getString("java.vm.version");

        this.osName = jsonObject.getString("os.name");
        this.osArchitecture = jsonObject.getString("os.arch");
        this.osVersion = jsonObject.getString("os.version");

        this.userName = jsonObject.getString("user.name");
        String rawUserLocale = jsonObject.getString("user.locale");
        this.userLocale = Locale.forLanguageTag(rawUserLocale);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("java.version", this.javaVersion);
        jsonObject.put("java.vendor", this.javaVendor);
        jsonObject.put("java.vm.version", this.javaVMVersion);

        jsonObject.put("os.name", this.osName);
        jsonObject.put("os.arch", this.osArchitecture);
        jsonObject.put("os.version", this.osVersion);

        jsonObject.put("user.name", this.userName);
        jsonObject.put("user.locale", this.userLocale);

        return jsonObject;
    }
}
