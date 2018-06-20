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
    exports it.polimi.ingsw.controllers.proxies;
    exports it.polimi.ingsw.listeners;
    exports it.polimi.ingsw.net;
    exports it.polimi.ingsw.net.utils;
    exports it.polimi.ingsw.net.interfaces;
    exports it.polimi.ingsw.net.mocks;
    exports it.polimi.ingsw.net.requests;
    exports it.polimi.ingsw.net.responses;
    exports it.polimi.ingsw.net.providers;
    exports it.polimi.ingsw.net.interfaces.updates;
    exports it.polimi.ingsw.utils.logging;
    exports it.polimi.ingsw.utils.io.json;
    exports it.polimi.ingsw.controllers.proxies.socket;

    requires java.xml;
    requires java.rmi;
    requires json;
    requires java.logging;
}