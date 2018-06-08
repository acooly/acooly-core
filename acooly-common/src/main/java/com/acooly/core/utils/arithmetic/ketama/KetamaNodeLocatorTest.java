package com.acooly.core.utils.arithmetic.ketama;

import java.util.*;

/**
 * katama测试 Created by zhangpu on 2015/9/4.
 */
public class KetamaNodeLocatorTest {

    /**
     * key's count
     */
    private static final Integer EXE_TIMES = 1000000;

    static Random ran = new Random();

    /**
     * @param args
     */
//    public static void main(String[] args) {
//        KetamaNodeLocatorTest test = new KetamaNodeLocatorTest();
//        Map<KetamaNode, Integer> nodeRecord = new LinkedHashMap<KetamaNode, Integer>();
//        KetamaHashs ketama = KetamaHashs.INSTANCE("order_info");
//
//        List<String> allKeys = test.getAllStrings();
//        for (String key : allKeys) {
//            KetamaNode node = ketama.getLocator().getPrimary(key);
//            Integer times = nodeRecord.get(node);
//            if (times == null) {
//                nodeRecord.put(node, 1);
//            } else {
//                nodeRecord.put(node, times + 1);
//            }
//        }
//        System.out.println(
//                "Nodes count : "
//                        + ketama.getLocator().getNodes()
//                        + ", Keys count : "
//                        + EXE_TIMES
//                        + ", Normal percent : "
//                        + (float) 100 / ketama.getLocator().getNodes()
//                        + "%");
//        System.out.println("-------------------- boundary  ----------------------");
//        for (Map.Entry<KetamaNode, Integer> entry : nodeRecord.entrySet()) {
//            System.out.println(
//                    "Node name :"
//                            + entry.getKey()
//                            + " - Times : "
//                            + entry.getValue()
//                            + " - Percent : "
//                            + (float) entry.getValue() / EXE_TIMES * 100
//                            + "%");
//        }
//    }

    public List<KetamaNode> getNodes(int nodeCount) {
        List<KetamaNode> nodes = new ArrayList<KetamaNode>();

        for (int k = 1; k <= nodeCount; k++) {
            KetamaNode node = new KetamaNode("order_info" + k, "order_info" + k);
            nodes.add(node);
        }

        return nodes;
    }

    private List<String> getAllStrings() {
        List<String> allStrings = new ArrayList<String>(EXE_TIMES);

        for (int i = 0; i < EXE_TIMES; i++) {
            allStrings.add(generateRandomString(ran.nextInt(50)));
        }

        return allStrings;
    }

    private String generateRandomString(int length) {
        StringBuffer sb = new StringBuffer(length);

        for (int i = 0; i < length; i++) {
            sb.append((char) (ran.nextInt(95) + 32));
        }

        return sb.toString();
    }
}
