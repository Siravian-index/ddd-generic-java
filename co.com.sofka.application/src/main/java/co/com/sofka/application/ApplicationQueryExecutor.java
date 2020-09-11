package co.com.sofka.application;

import co.com.sofka.business.annotation.QueryHandles;
import co.com.sofka.business.annotation.QueryPath;
import co.com.sofka.business.repository.QueryMapperRepository;
import co.com.sofka.business.sync.ViewModelExecutor;
import co.com.sofka.infraestructure.handle.QueryExecutor;
import io.github.classgraph.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Application query executor.
 */
public class ApplicationQueryExecutor extends QueryExecutor {
    private static final Logger logger = Logger.getLogger(ApplicationQueryExecutor.class.getName());
    private final QueryMapperRepository queryMapperRepo;
    private final String packageQueries;

    /**
     * Instantiates a new Application query executor.
     *
     * @param packageQueries  the package queries
     * @param queryMapperRepo the query mapper repo
     */
    public ApplicationQueryExecutor(
            String packageQueries,
            QueryMapperRepository queryMapperRepo) {
        this.queryMapperRepo = queryMapperRepo;
        this.packageQueries = packageQueries;
        initialize();
    }

    private void initialize() {
        try (ScanResult result = new ClassGraph()
                .enableAllInfo()
                .whitelistPackages(packageQueries)
                .scan()) {
            ClassInfoList classInfos = result.getClassesWithAnnotation(QueryHandles.class.getName());
            classInfos.parallelStream().forEach(handleClassInfo -> {
                try {

                    AnnotationInfo annotationInfo = handleClassInfo.getAnnotationInfo(QueryPath.class.getName());
                    String path = getPath(handleClassInfo, annotationInfo);

                    var handle = (ViewModelExecutor) handleClassInfo.loadClass()
                            .getDeclaredConstructor()
                            .newInstance();
                    put(path, handle.witchQueryMapperRepository(queryMapperRepo));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    logger.log(Level.SEVERE, "An error caused by the mapper generic and cast view model", e);
                }
            });
        }
    }

    private String getPath(ClassInfo handleClassInfo, AnnotationInfo annotationInfo) {
        return Optional.ofNullable(annotationInfo).map(annotation -> {
            AnnotationParameterValueList paramVals = annotation.getParameterValues();
            return (String) paramVals.getValue("name");
        }).orElse(handleClassInfo
                .loadClass()
                .getCanonicalName()
                .toLowerCase().replace(packageQueries + ".", "")
        );
    }

}
