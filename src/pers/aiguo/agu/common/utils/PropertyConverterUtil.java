package pers.aiguo.agu.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PropertyConverterUtil<T> {
    private final List<PropertyMapping<T>> propertyMappings;
    private final Map<String, Function<List<String>, Map<String, String>>> attributeToCodeToNameMapProviders;

    private PropertyConverterUtil(List<PropertyMapping<T>> propertyMappings,
                                  Map<String, Function<List<String>, Map<String, String>>> attributeToCodeToNameMapProviders) {
        this.propertyMappings = propertyMappings;
        this.attributeToCodeToNameMapProviders = attributeToCodeToNameMapProviders;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public List<T> convertList(List<T> vos) {
        // 从所有的 vos 中提取 code 列表
        List<String> codeList = vos.stream()
                .flatMap(vo -> propertyMappings.stream().map(mapping -> mapping.getCodeExtractor().apply(vo)))
                .collect(Collectors.toList());

        // 为每个属性获取 codeToNameMap
        Map<String, Map<String, String>> attributeToCodeToNameMap = new HashMap<>();
        for (PropertyMapping<T> mapping : propertyMappings) {
            String attributeName = mapping.getAttributeName();
            Function<List<String>, Map<String, String>> codeToNameMapProvider = attributeToCodeToNameMapProviders.get(attributeName);
            if (codeToNameMapProvider != null) {
                attributeToCodeToNameMap.put(attributeName, codeToNameMapProvider.apply(codeList));
            }
        }

        // 执行转换
        return vos.stream()
                .map(vo -> convert(vo, attributeToCodeToNameMap))
                .collect(Collectors.toList());
    }

    private T convert(T vo, Map<String, Map<String, String>> attributeToCodeToNameMap) {
        if (vo != null) {
            for (PropertyMapping<T> mapping : propertyMappings) {
                String attributeName = mapping.getAttributeName();
                Map<String, String> codeToNameMap = attributeToCodeToNameMap.get(attributeName);

                if (codeToNameMap != null) {
                    String code = mapping.getCodeExtractor().apply(vo);
                    String name = codeToNameMap.get(code);
                    mapping.getNameSetter().accept(vo, name);
                }
            }
        }
        return vo;
    }

    public static class Builder<T> {
        private final List<PropertyMapping<T>> propertyMappings = new ArrayList<>();
        private final Map<String, Function<List<String>, Map<String, String>>> attributeToCodeToNameMapProviders = new HashMap<>();

        public Builder<T> propertyMapping(String attributeName, Function<T, String> codeExtractor,
                                          BiConsumer<T, String> nameSetter,
                                          Function<List<String>, Map<String, String>> codeToNameMapProvider) {
            propertyMappings.add(new PropertyMapping<>(attributeName, codeExtractor, nameSetter));
            attributeToCodeToNameMapProviders.put(attributeName, codeToNameMapProvider);
            return this;
        }

        public PropertyConverterUtil<T> build() {
            return new PropertyConverterUtil<>(propertyMappings, attributeToCodeToNameMapProviders);
        }
        public List<T> convertList(List<T> vos) {
            PropertyConverterUtil<T> converter = build();
            return converter.convertList(vos);
        }
    }

    private static class PropertyMapping<T> {
        private final String attributeName;
        private final Function<T, String> codeExtractor;
        private final BiConsumer<T, String> nameSetter;

        private PropertyMapping(String attributeName, Function<T, String> codeExtractor,
                                BiConsumer<T, String> nameSetter) {
            this.attributeName = attributeName;
            this.codeExtractor = codeExtractor;
            this.nameSetter = nameSetter;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public Function<T, String> getCodeExtractor() {
            return codeExtractor;
        }

        public BiConsumer<T, String> getNameSetter() {
            return nameSetter;
        }
    }
}
