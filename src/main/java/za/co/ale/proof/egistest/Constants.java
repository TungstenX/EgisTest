package za.co.ale.proof.egistest;

/**
 * Common place for all the constant values
 * @author Andre Labuschange
 */
public interface Constants {
    /**
     * The target url
     */
    String URL = "https://github.com/egis/handbook/blob/master/Tech-Stack.md";
    /**
     * Find the outer div using readme id
     */
    String OUTER_DIV_ID = "readme";
    /**
     * The headings for the areas as required
     * Not Monitoring as it was not specified as an requirement
     */
    String[] HEADINGS = {"Programming Stack", "Build Stack", "Infrastructure"};
}
