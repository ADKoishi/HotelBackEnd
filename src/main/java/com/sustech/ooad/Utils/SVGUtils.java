package com.sustech.ooad.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SVGUtils {
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static final DocumentBuilder builder;

    static {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> resolvePolygonPoints(String points, int width, int height){
        String[] pointsArr = points.split(" ");
        List<Integer> percentageCoordinates = new ArrayList<>();
        int widthOn = 1;
        int heightOn = 0;
        for (String point : pointsArr) {
            percentageCoordinates.add((int)(Double.parseDouble(point)/(widthOn * width + heightOn * height)*100));
            widthOn = widthOn == 1 ? 0 : 1;
            heightOn = heightOn == 1 ? 0 : 1;
        }
        return percentageCoordinates;
    }

    public static List<List<Integer>> parseSvgFile(String filePath) throws IOException, SAXException {
        Document document = builder.parse(filePath);
        Element svgElement = document.getDocumentElement();
        int width = Integer.parseInt(svgElement.getAttribute("width").replaceAll("px",""));
        int height = Integer.parseInt(svgElement.getAttribute("height").replaceAll("px",""));
        NodeList gList = document.getElementsByTagName("g");
        List<List<Integer>> percentageCoordinates = new ArrayList<>();
        if (!(gList.getLength() > 0))
            return percentageCoordinates;

        Element gElement = (Element) gList.item(0);
        NodeList children = gElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if (child.getNodeName().equals("polygon")){
                String pointsStr = ((Element) child).getAttribute("points");
                percentageCoordinates.add(resolvePolygonPoints(pointsStr, width, height));
            }
            else if (child.getNodeName().equals("rect")){
                String xStr = ((Element) child).getAttribute("x");
                String yStr = ((Element) child).getAttribute("y");
                String widthStr = ((Element) child).getAttribute("width");
                String heightStr = ((Element) child).getAttribute("height");
                String pointsStr = "";
                pointsStr += xStr + " " + yStr;
                double newX = Double.parseDouble(xStr) + Double.parseDouble(widthStr);
                double newY = (Double.parseDouble(yStr) + Double.parseDouble(heightStr));
                pointsStr += " " + newX + " " + yStr;
                pointsStr += " " + newX + " " + newY;
                pointsStr += " " + xStr + " " + newY;
                percentageCoordinates.add(resolvePolygonPoints(pointsStr, width, height));
            }
        }
        return percentageCoordinates;
    }
}
