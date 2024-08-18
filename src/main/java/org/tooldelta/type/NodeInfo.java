package org.tooldelta.type;

public class NodeInfo {
    public String ServerName;
    public String CoreVersion;
    public String NodeUID;

    public NodeInfo(String ServerName, String CoreVersion, String NodeUID) {
        this.ServerName = ServerName;
        this.CoreVersion = CoreVersion;
        this.NodeUID = NodeUID;
    }

}
