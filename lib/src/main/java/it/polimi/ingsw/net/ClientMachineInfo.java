package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.util.Locale;

// TODO: document
public class ClientMachineInfo implements JSONSerializable {
    private static final long serialVersionUID = -2994818791895152190L;

    private String javaVersion;
    private String javaVendor;
    private String javaVMVersion;

    private String osName;
    private String osVersion;
    private String osArchitecture;

    private String userName;
    private Locale userLocale;

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getJavaVendor() {
        return javaVendor;
    }

    public void setJavaVendor(String javaVendor) {
        this.javaVendor = javaVendor;
    }

    public String getJavaVMVersion() {
        return javaVMVersion;
    }

    public void setJavaVMVersion(String javaVMVersion) {
        this.javaVMVersion = javaVMVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsArchitecture() {
        return osArchitecture;
    }

    public void setOsArchitecture(String osArchitecture) {
        this.osArchitecture = osArchitecture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Locale getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(Locale userLocale) {
        this.userLocale = userLocale;
    }

    public ClientMachineInfo() { }

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
