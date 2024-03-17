package pers.aiguo.agu.common.utils.test;

import pers.aiguo.agu.common.utils.PropertyConverterUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        // 假设有一些 pers.aiguo.agu.common.utils.test.YourVO1 的实例
        List<YourVO1> vos1 = new ArrayList<>();
        vos1.add(new YourVO1("C1Code", "P1Code", null, null));
        vos1.add(new YourVO1("C2Code", "P2Code", null, null));

        // 使用 PropertyConverter 进行转换
        List<YourVO1> convertedVos1 = PropertyConverterUtil.<YourVO1>builder()
                .propertyMapping("company", YourVO1::getCompanyCode, YourVO1::setCompanyName, list -> new HashMap<String,String>(){
                    {
                        put("C1Code","C1name");
                        put("C2Code","C2name");
                    }
                })
                .propertyMapping("product", YourVO1::getProductCode, YourVO1::setProductName, list -> new HashMap<String,String>(){
                    {
                        put("P1Code","P1name");
                        put("P2Code","P2name");
                    }
                })
                .convertList(vos1);

        // 输出转换后的结果
        for (YourVO1 vo : convertedVos1) {
            System.out.println(vo.getCompanyName() + ", " + vo.getProductName());
        }

        // 假设有一些 pers.aiguo.agu.common.utils.test.YourVO2 的实例
        List<YourVO2> vos2 = new ArrayList<>();
        vos2.add(new YourVO2("O1", "A1", null, null));
        vos2.add(new YourVO2("O2", "A2", null, null));

        // 使用 PropertyConverter 进行转换
        List<YourVO2> convertedVos2 = PropertyConverterUtil.<YourVO2>builder()
                .propertyMapping("other", YourVO2::getOtherCode, YourVO2::setOtherName, list -> Collections.emptyMap())
                .propertyMapping("another", YourVO2::getAnotherCode, YourVO2::setAnotherName, list -> Collections.emptyMap())
                .build()
                .convertList(vos2);

        // 输出转换后的结果
        for (YourVO2 vo : convertedVos2) {
            System.out.println(vo.getOtherName() + ", " + vo.getAnotherName());
        }
    }
}

// pers.aiguo.agu.common.utils.test.YourVO1 类
class YourVO1 {
    private String companyCode;
    private String productCode;
    private String companyName;
    private String productName;

    public YourVO1(String companyCode, String productCode, String companyName, String productName) {
        this.companyCode = companyCode;
        this.productCode = productCode;
        this.companyName = companyName;
        this.productName = productName;
    }

    // 省略 getter 和 setter

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

// pers.aiguo.agu.common.utils.test.YourVO2 类
class YourVO2 {
    private String otherCode;
    private String anotherCode;
    private String otherName;
    private String anotherName;

    public YourVO2(String otherCode, String anotherCode, String otherName, String anotherName) {
        this.otherCode = otherCode;
        this.anotherCode = anotherCode;
        this.otherName = otherName;
        this.anotherName = anotherName;
    }

    public String getOtherCode() {
        return otherCode;
    }

    public void setOtherCode(String otherCode) {
        this.otherCode = otherCode;
    }

    public String getAnotherCode() {
        return anotherCode;
    }

    public void setAnotherCode(String anotherCode) {
        this.anotherCode = anotherCode;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }
// 省略 getter 和 setter
}
