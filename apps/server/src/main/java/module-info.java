module it.polimi.ingsw.server {
    requires it.polimi.ingsw;

    requires java.logging;
    requires jopt.simple;
    requires java.xml;
    requires java.rmi;
    requires postgresql;
    requires java.sql;
    requires json;

    opens it.polimi.ingsw.server to it.polimi.ingsw;
}