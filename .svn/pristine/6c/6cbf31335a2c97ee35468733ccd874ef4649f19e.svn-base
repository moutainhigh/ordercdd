package com.liyang.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class ExcelUtil {
    private Workbook workbook = new XSSFWorkbook();
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private String excelName;
    private String[] objectProperties;
    private String[] headings;

    public ExcelUtil() {
    }

    public ExcelUtil(String excelName) {
        this.excelName = excelName;
    }

    public ExcelUtil(String[] objectProperty) {
        this.objectProperties = objectProperty;
    }

    public void write() {
        try {
            workbook.write(byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<Resource> responseEntity() {
        Resource file = new ByteArrayResource(byteArrayOutputStream.toByteArray());
        try {
            if (excelName == null) {
                excelName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
            excelName = URLEncoder.encode(excelName, "utf8");
        } catch (UnsupportedEncodingException ignored) {
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + excelName + ".xlsx" + "\"").body(file);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public void export(Collection collection) {
        if (collection == null || collection.isEmpty() || objectProperties == null) {
            return;
        }
        Sheet sheet = workbook.createSheet("Sheet1");

        generateHeadings(sheet);

        Iterator iterator = collection.iterator();
        int rowNum = 1;//从第二行开始
        Object fieldValue;
        while (iterator.hasNext()) {

            Row row = sheet.createRow(rowNum);

            Object o = iterator.next();
            if (o == null) {
                continue;
            }
            int cellNum = 0;
            for (String property : objectProperties) {
                Cell cell = row.createCell(cellNum);
                try {
                    fieldValue=  handler(o,property);
                    setCellValue(fieldValue,cell);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    cellNum++;
                }
            }
            rowNum++;
        }

        write();
    }

    public String[] getObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(String[] objectProperties) {
        this.objectProperties = objectProperties;
    }

    public Object handler(Object itemObj, String propertyName)
    {
        Class oClass = itemObj.getClass();
        Object fieldValue=null;
        try {

            String fieldGetName = "get"
                    + propertyName.substring(0, 1).toUpperCase()
                    + propertyName.substring(1);

            Method method = oClass.getMethod(fieldGetName);

            fieldValue= method.invoke(itemObj);


        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
            ignored.printStackTrace();
        }
        return fieldValue;
    }

    public void setCellValue(Object fieldValue,Cell cell)
    {
        if (fieldValue == null) {
            return;
        }
        if (fieldValue instanceof Byte) {
            cell.setCellValue((char) fieldValue);
        } else if (fieldValue instanceof Short) {
            cell.setCellValue((short) fieldValue);
        } else if (fieldValue instanceof Integer) {
            cell.setCellValue((int) fieldValue);
        } else if (fieldValue instanceof Long) {
            cell.setCellValue((long) fieldValue);
        } else if (fieldValue instanceof Float) {
            cell.setCellValue((Float) fieldValue);
        } else if (fieldValue instanceof Double) {
            cell.setCellValue((double) fieldValue);
        } else if (fieldValue instanceof Boolean) {
            cell.setCellValue((boolean) fieldValue);
        } else if (fieldValue instanceof String) {
            cell.setCellValue((String) fieldValue);
        } else if (fieldValue instanceof Date) {
            cell.setCellValue((Date) fieldValue);
        } else {
            cell.setCellValue(fieldValue.toString());
        }
    }

    public String[] getHeadings() {
        return headings;
    }

    public void setHeadings(String[] headings) {
        this.headings = headings;
    }
    public void generateHeadings(Sheet sheet)
    {
        if(headings == null || (headings.length == 0)){
            return;
        }
        Row firstRow =sheet.createRow(0);
        int cellNum=0;
        for (String heading:headings){
            firstRow.createCell(cellNum).setCellValue(heading);
            cellNum++;
        }

    }
}
