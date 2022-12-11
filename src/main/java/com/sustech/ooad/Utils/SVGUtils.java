package com.sustech.ooad.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class SVGUtils {
    // 解析XML文件并打印其中的每个元素
    public static void parseSvgFile(String filePath) {
        try {
            // 创建一个DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析XML文件，获得Document对象
            Document doc = builder.parse(new File(filePath));

            // 获取文档的根元素
            Element root = doc.getDocumentElement();
            // 获取根元素的所有子元素
            NodeList nodes = root.getChildNodes();

            // 遍历每个子元素
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // 获取元素的名称和值
                    String name = element.getNodeName();
                    String value = element.getTextContent();
                    System.out.println(name + "=" + value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
