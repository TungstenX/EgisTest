package za.co.ale.proof.egistest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Egis Test. This will screen scrape a predetermined web page and produce a
 * JSON output a per the requirements.<p>
 * Development time: 1 hour<p>
 * Testing: 15 minutes<p <p>
 * Pros: Quick development, using libraries as to not reinvent the wheel.<p>
 * Cons: Ultra specific to one web page. No negative testing<p>
 * @author Andre Labuschagne
 */
public class EgisTest {

    private static final Logger LOG = Logger.getLogger(EgisTest.class.getName());

    /**
     * The main entry point
     *
     * @param args [NOT USED]
     */
    public static void main(String[] args) {
        EgisTest egisTest = new EgisTest();
        try {
            System.out.println(egisTest.go());
        } catch (JsonProcessingException e) {
            LOG.log(Level.SEVERE, "Error while conferting to json string: {0}", e.toString());
            System.exit(-2);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while fetching page: {0}", e.toString());
            System.exit(-1);
        }
    }

    /**
     * Start the processing
     *
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    public String go() throws IOException, JsonProcessingException {
        Document doc = Jsoup.connect(Constants.URL).get();
        return processOuterDiv(doc);
    }

    /**
     * Process the Document by traversing its nodes
     *
     * @param doc A JSoup Document object
     * @return
     */
    private String processOuterDiv(Document doc) throws JsonProcessingException {
        Map<String, List<String>> areas = new HashMap<>();
        Element content = doc.getElementById(Constants.OUTER_DIV_ID);
        Element article = content.child(0);
        Elements kids = article.children();
        for (Element kid : kids) {
            //Get the heading
            if (kid.tagName().equals("h2")) {
                //Check if heading is in the list
                String heading = getHeading2Text(kid);
                if (Arrays.asList(Constants.HEADINGS).contains(heading)) {
                    System.out.println("H2: " + heading);
                    areas.put(heading, processTable(kid));
                }
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(areas);
        return jsonInString;
    }

    /**
     * Traverse the table's elements to find the value of the first column and
     * add it to a list
     *
     * @param firstSibling This is the h2 element
     * @return a list of string values of the first column of each row
     */
    private List<String> processTable(Element firstSibling) {
        List<String> ret = new LinkedList<>();
        Element nextSibling = firstSibling.nextElementSibling();
        Element tbody = nextSibling.child(1);
        Elements trs = tbody.children();
        for (Element tr : trs) {
            ret.add(getTDText(tr.child(0)));
        }
        return ret;
    }

    /**
     * Generic helper to find the own text of an Element. Will look in the
     * <code>tagName</code> tag if it can't find the own text
     *
     * @param e the Element to check
     * @param tagName the tag name if the ownText() of the Element returns an
     * empty string
     * @return The own text of the Element
     */
    private String getElementText(Element e, String tagName) {
        String elementtext = e.ownText().trim();
        if (StringUtils.isBlank(elementtext)) {
            //the heading is in <strong> tags
            Elements kids = e.children();
            for (Element kid : kids) {
                if (kid.tagName().equals(tagName)) {
                    elementtext = kid.ownText().trim();
                    break;// out of loop
                }
            }
        }
        return elementtext;
    }

    /**
     * Helper method to find the heading own text
     * @param e the Element to check
     * @return the Heading text
     */
    private String getHeading2Text(Element e) {
        return getElementText(e, "strong");
    }

    /**
     * Helper method to find the column text
     * @param e the Element to check
     * @return the column text
     */
    private String getTDText(Element e) {
        return getElementText(e, "div");
    }
}
