package it.polimi.ingsw.utils.io;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public final class XmlUtils {
    private XmlUtils() {}

    public static Map<String, Object> xmlToMap(Node node) {
        Map<String, Object> hashMap = new HashMap<>();

        NamedNodeMap attributes = node.getAttributes();

        for (int i = 0; attributes != null &&  i < attributes.getLength(); i++) {
            hashMap.put("@" + attributes.item(i).getNodeName(), attributes.item(i).getNodeValue());
        }

        NodeList children = node.getChildNodes();
        Map<String, List<Map<String, Object>>> temporaryChildren = new HashMap<>();

        for (int i = 0; i < children.getLength(); i++) {
            if (!temporaryChildren.containsKey(children.item(i).getNodeName())) {
                temporaryChildren.put(children.item(i).getNodeName(), new ArrayList<>());
            }

            temporaryChildren.get(children.item(i).getNodeName())
                    .add(xmlToMap(children.item(i)));
        }

        temporaryChildren.forEach((key, maps) -> {
            if (maps.size() == 1) {
                hashMap.put(key, maps.get(0));
            }
            else {
                hashMap.put(key, maps.toArray());
            }
        });

        if (children.getLength() == 0) {
            hashMap.put("#text", node.getTextContent());
        }

        return hashMap;
    }

    public static Map<String, Object> getMap(Map<String, Object> objectMap, String key) {
        //noinspection unchecked
        return (Map<String, Object>) objectMap.get(key);
    }

    public static Map<String, Object>[] getMapArray(Map<String, Object> objectMap, String key) {
        Object[] objects = (Object[]) objectMap.get(key);
        //noinspection unchecked
        return Arrays.stream(objects).toArray(Map[]::new);
    }
}
