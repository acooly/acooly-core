package com.acooly.core.utils.mapper;

import com.acooly.core.utils.Reflections;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CSV文件简单Mapper
 *
 * @author zhangpu
 */
public class CsvMapper {

  public static List<List<String>> unmarshals(List<String> lines) {
    List<List<String>> results = new ArrayList<List<String>>(lines.size());
    for (String line : lines) {
      results.add(unmarshal(line));
    }
    return results;
  }

  /**
   * 反编列CSV行为集合
   *
   * @param line
   * @return
   */
  public static List<String> unmarshal(String line) {
    List<String> result = new LinkedList<String>();
    String regex = "(\"[^\"]*(\"{2})*[^\"]*\")?[^,]*[,]?";
    Matcher matcher = Pattern.compile(regex).matcher(line);
    String str;
    while (matcher.find()) {
      str = matcher.group();
      if (StringUtils.isBlank(str)) {
        continue;
      }
      str = StringUtils.removeEnd(str, ",");
      if (StringUtils.startsWith(str, "\"")) {
        str = StringUtils.removeStart(str, "\"");
      }
      if (StringUtils.endsWith(str, "\"")) {
        str = StringUtils.removeEnd(str, "\"");
      }
      str = StringUtils.replace(str, "\"\"", "\"");
      result.add(str);
    }
    return result;
  }

  /**
   * 编列集合为CSV行数据
   *
   * @param data
   * @return
   */
  public static String marshal(List<String> data) {
    StringBuilder sb = new StringBuilder();
    String field;
    for (int i = 0; i < data.size(); i++) {
      field = StringUtils.trimToEmpty(data.get(i));
      if (StringUtils.contains(field, "\"")) {
        field = StringUtils.replace(field, "\"", "\"\"");
        field = "\"" + field + "\"";
      } else if (StringUtils.contains(field, ",")) {
        field = "\"" + field + "\"";
      }
      sb.append(field).append(i == data.size() - 1 ? "" : ",");
    }
    return sb.toString();
  }

  public static String marshal(Object data, String[] propertyNames) {
    List<String> list = Reflections.invokeGetterToString(data, propertyNames);
    return marshal(list);
  }

  public static List<String> marshals(List<Object> datas, String[] propertyNames) {
    List<String> lines = new ArrayList<String>(datas.size());
    for (Object data : datas) {
      lines.add(marshal(data, propertyNames));
    }
    return lines;
  }

  public static List<String> marshals(List<List<String>> datas) {
    List<String> lines = new ArrayList<String>(datas.size());
    for (List<String> data : datas) {
      lines.add(marshal(data));
    }
    return lines;
  }

  /**
   * 典型测试main方法
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    String line = "a,b,c,,\"asdfasdf,asdfas\",\"asd\"\"12\"\"123\"";
    System.out.println(line);
    List<String> result = CsvMapper.unmarshal(line);
    System.out.println(result.size() + ": " + result);
    System.out.println(CsvMapper.marshal(result));
  }
}
