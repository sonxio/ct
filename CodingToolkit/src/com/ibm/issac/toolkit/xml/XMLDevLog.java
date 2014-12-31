package com.ibm.issac.toolkit.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.issac.toolkit.DevLog;

public class XMLDevLog {
	public static void printNodeList(NodeList nodeList) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			DevLog.debug("-----------");
			// DevLog.debug(nodes.item(i).getNodeValue());
			Node cn = nodeList.item(i);
			NamedNodeMap nnm = cn.getAttributes();
			for (int j = 0; j < nnm.getLength(); j++) {
				DevLog.debug(nnm.item(j).getNodeName() + " : " + nnm.item(j).getNodeValue());
			}
		}
	}
}
