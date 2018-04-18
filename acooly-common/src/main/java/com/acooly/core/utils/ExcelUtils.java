package com.acooly.core.utils;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.CellValue;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Excel 处理工具
 *
 * @author zhangpu
 */
public class ExcelUtils {

    public static void write(
            String[] headerNames, String[] propertyNames, List<Object> dtos, OutputStream stream) {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(stream);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            int row = 0;
            // 写入header
            Label label = null;
            for (int i = 0; i < headerNames.length; i++) {
                label = new Label(i, row, headerNames[i]);
                sheet.addCell(label);
            }
            // 写入数据
            row++;
            CellValue cell = null;
            for (Object instance : dtos) {
                for (int i = 0; i < propertyNames.length; i++) {
                    Object cellObject = Reflections.invokeGetter(instance, propertyNames[i]);

                    if (cellObject == null) {
                        cell = new Blank(i, row);
                    } else {
                        if (cellObject.getClass().isAssignableFrom(Date.class)) {
                            cell = new DateTime(i, row, (Date) cellObject);
                        } else if (cellObject.getClass().isAssignableFrom(Double.class)) {
                            cell = new jxl.write.Number(i, row, (Double) cellObject);
                        } else if (cellObject.getClass().isAssignableFrom(Float.class)) {
                            cell = new jxl.write.Number(i, row, (Float) cellObject);
                        } else if (cellObject.getClass().isAssignableFrom(Long.class)) {
                            cell = new jxl.write.Number(i, row, (Long) cellObject);
                        } else if (cellObject.getClass().isAssignableFrom(Integer.class)) {
                            cell = new jxl.write.Number(i, row, (Integer) cellObject);
                        } else {
                            cell = new Label(i, row, cellObject.toString());
                        }
                    }
                    sheet.addCell(cell);
                }
                row++;
            }
            workbook.write();
        } catch (Exception e) {
            throw new RuntimeException("Write to Excel with jxl fault.", e);
        } finally {
            try {
                workbook.close();
            } catch (Exception e2) {
                // ig
            }
        }
    }

    static class PojoEntity {

        private Long id;
        private String name;
        private int type;
        private float rate;
        private double balance;
        private Date createTime;

        public PojoEntity() {
            super();
        }

        public PojoEntity(Long id, String name, int type, float rate, double balance, Date createTime) {
            super();
            this.id = id;
            this.name = name;
            this.type = type;
            this.rate = rate;
            this.balance = balance;
            this.createTime = createTime;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public float getRate() {
            return rate;
        }

        public void setRate(float rate) {
            this.rate = rate;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }

  /*public static void main(String[] args) throws Exception {
  	List<String> headerNames = new ArrayList<String>();
  	headerNames.add("编号");
  	headerNames.add("名称");
  	headerNames.add("类型");
  	headerNames.add("比例");
  	headerNames.add("余額");
  	headerNames.add("创建时间");

  	List<String> propertyNames = new ArrayList<String>();
  	propertyNames.add("id");
  	propertyNames.add("name");
  	propertyNames.add("type");
  	propertyNames.add("rate");
  	propertyNames.add("balance");
  	propertyNames.add("createTime");

  	List<Object> entities = new ArrayList<Object>();
  	for (int i = 1; i <= 10; i++) {
  		entities.add(new PojoEntity(Long.valueOf(i), "name" + i, i, 0.75f, 12.3212d, new Date()));
  	}

  	FileOutputStream stream = new FileOutputStream("test.xls", false);
  	ExcelUtils.write(headerNames.toArray(new String[]{}), propertyNames.toArray(new String[]{}), entities, stream);

  	if (stream != null) {
  		System.out.println("Close stream after finished excel");
  		stream.close();
  	}
  }*/
}
