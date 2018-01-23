package com.liyang.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;

public class ExcelMapUtil extends ExcelUtil {
    public ExcelMapUtil() {
    }

    public ExcelMapUtil(String excelName) {
        super(excelName);
    }

    public ExcelMapUtil(String[] objectProperty) {
        super(objectProperty);
    }

    @Override
    public Object handler(Object itemObj, String propertyName) {
        if (!(itemObj instanceof HashMap)) {
            throw new IllegalArgumentException("itemObj instanceof HashMap");
        }
        HashMap hashMap = (HashMap) itemObj;
        return hashMap.get(propertyName);
    }
}
