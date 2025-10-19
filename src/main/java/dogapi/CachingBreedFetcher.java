package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private Map<String,List<String>> SubBreeds;
    private BreedFetcher fetcher;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.callsMade = 0;
        this.SubBreeds = new HashMap<>();
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        try{
            if (SubBreeds.containsKey(breed)) {
                return SubBreeds.get(breed);
            } else {
                this.callsMade++;
                List<String> sublst = fetcher.getSubBreeds(breed);
                SubBreeds.put(breed, sublst);
                return sublst;
            }
        } catch (Exception e) {
            throw new BreedNotFoundException(breed);
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}