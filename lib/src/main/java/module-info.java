module it.polimi.ingsw {
    exports it.polimi.ingsw.core;
    exports it.polimi.ingsw.core.locations;
    exports it.polimi.ingsw.models;
    exports it.polimi.ingsw.utils;
    exports it.polimi.ingsw.utils.text;
    exports it.polimi.ingsw.utils.io;
    exports it.polimi.ingsw.views;
    exports it.polimi.ingsw.utils.streams;
    exports it.polimi.ingsw.controllers;
    exports it.polimi.ingsw.listeners;
    exports it.polimi.ingsw.net;
    exports it.polimi.ingsw.net.utils;
    exports it.polimi.ingsw.net.interfaces;
    exports it.polimi.ingsw.net.mocks;

    requires java.xml;
    requires javafx.graphics;
    requires java.rmi;
    requires json;
}