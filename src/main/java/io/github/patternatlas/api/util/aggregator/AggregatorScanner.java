package io.github.patternatlas.api.util.aggregator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class AggregatorScanner {

    public static Aggregator findMatchingAggregatorImpl(String sourceType, String targetType) {
        try {
            if (targetType == null) {
                targetType = "";
            }

            List<Class> classList = findAggregatorImplementations(AggregatorScanner.class.getPackage().getName());
            for (Class implCandidate : classList) {
                AggregatorMetadata annotation = (AggregatorMetadata) implCandidate.getAnnotation(AggregatorMetadata.class);
                List<Object> sourceTypes = Arrays.asList(annotation.sourceTypes());
                List<Object> targetTypes = Arrays.asList(annotation.targetTypes());

                if (sourceTypes.contains(sourceType) && targetTypes.contains(targetType)) {
                    return (Aggregator) implCandidate.newInstance();
                }
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private static List<Class> findAggregatorImplementations(String basePackage) throws IOException, ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        List<Class> candidates = new ArrayList<Class>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + "/" + "**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (isCandidate(metadataReader)) {
                    candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }
        return candidates;
    }

    private static String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    private static boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException {
        try {
            Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
            if (c.getAnnotation(AggregatorMetadata.class) != null) {
                return true;
            }
        } catch (Throwable e) {
        }
        return false;
    }
}
