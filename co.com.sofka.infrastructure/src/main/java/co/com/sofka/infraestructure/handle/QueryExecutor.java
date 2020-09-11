package co.com.sofka.infraestructure.handle;

import co.com.sofka.domain.generic.ViewModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * The type Query executor.
 */
public class QueryExecutor implements QueryHandler<Map<String, String>> {
    private static final Logger logger = Logger.getLogger(QueryExecutor.class.getName());

    /**
     * The Handles.
     */
    protected Map<String, Function<Map<String, String>, ?>> handles = new ConcurrentHashMap<>();

    /**
     * Put.
     *
     * @param queryPath the query path
     * @param function  the function
     */
    protected void put(String queryPath, Function<Map<String, String>, ?> function) {
        handles.put(queryPath, function);
    }

    @Override
    public ViewModel get(String path, Map<String, String> params) {

        if (!handles.containsKey(path)) {
            throw new ExecutionNoFound(path);
        }
        var result = handles.get(path).apply(params);
        logger.info("View model applied OK --> " + params);

        return (ViewModel) result;
    }

    @Override
    public List<ViewModel> find(String path, Map<String, String> params) {
        if (!handles.containsKey(path)) {
            throw new ExecutionNoFound(path);
        }
        var result = handles.get(path).apply(params);
        logger.info("View model list applied OK --> " + params);

        return (List<ViewModel>) result;
    }
}
