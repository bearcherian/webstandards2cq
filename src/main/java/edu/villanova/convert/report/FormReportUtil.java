package edu.villanova.convert.report;

import edu.villanova.convert.forms.Form;
import edu.villanova.convert.forms.input.Input;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by bear on 12/28/14.
 */
public class FormReportUtil {

    private static Logger reportLogger = LogManager.getLogger("FormReport");
    private static Logger logger = LogManager.getLogger(FormReportUtil.class);

    private static HashMap<String,Integer> domInputTypes = new HashMap<>();
    private static HashMap<String,Integer> domUActions = new HashMap<>();
    private static HashMap<String,Integer> domSFields = new HashMap<>();
    private static HashMap<String,Integer> domVuValues = new HashMap<>();
    private static HashMap<String,Integer> domVuTags = new HashMap<>();

    private static HashMap<String,Integer> inputTypes = new HashMap<>();
    private static HashMap<String,Integer> inputUActions = new HashMap<>();
    private static HashMap<String,Integer> inputSFields = new HashMap<>();
    private static HashMap<String,Integer> inputVuValues = new HashMap<>();
    private static HashMap<String,Integer> formVuTags = new HashMap<>();

    private static HashMap<String,Integer> ldapMemberAccounts = new HashMap<>();
    private static HashMap<String,Integer> ldapMemberGroups = new HashMap<>();

    public static void addInputStats(Input input) {
        //Input Types
        inputTypes = incrementStatMapValue(inputTypes,input.getType().getTypeValue());

        //U-Actions
        if (input.getName().startsWith("U-")) {
            inputUActions = incrementStatMapValue(inputUActions,input.getName());
        }

        //S-Fields
        if (input.getName().startsWith("S-")) {
            inputSFields = incrementStatMapValue(inputSFields,input.getName());
        }

        //VU Values
        if (input.getValue().contains("<vu:")) {
            inputVuValues = incrementStatMapValue(inputVuValues,input.getValue());
        }
    }

    public static void addDomStats(Document formDOM) {

        //VU Tags usage
        Elements vuElements = formDOM.select("*");
        for (Element element : vuElements) {

            //VU tags
            if (element.tagName().startsWith("vu:")){
                String vuTag = element.tagName();
                domVuTags = incrementStatMapValue(domVuTags,vuTag);

                //CHeck that this isn't for a archive or display results form
                if ("vu:isldapmember".equals(element.tagName())) {
                    //select the form inputs with name U-Action
                    Elements actionElements = element.select("form input[name=U-Action]");
                    for (Element actionElement : actionElements) {
                        String actionValue = actionElement.val();

                        //if no form action, or a save form action, log the accounts and groups
                        if (actionValue.equals(Form.SAVE_FORM)){
                            String[] accounts = element.attr("accounts").split(",");
                            for (String account : accounts ) {
                                ldapMemberAccounts = incrementStatMapValue(ldapMemberAccounts,account.trim());
                            }

                            String[] groups = element.attr("groups").split(",");
                            for (String group : groups) {
                                ldapMemberGroups = incrementStatMapValue(ldapMemberGroups,group.trim());
                            }

                            break;

                        }
                    }
                }
            }

            //Inputs tags
            if ("input".equals(element.tagName()) || "textarea".equals(element.tagName()) || "select".equals(element.tagName()) ){

                //Input Types
                String type = element.attr("type");
                if (element.tagName().equals("textarea") || element.tagName().equals("select")) {
                    type = element.tagName();
                }
                domInputTypes = incrementStatMapValue(domInputTypes,type);


                String name = element.attr("name");
                
                //U-Actions
                if (name.startsWith("U-")) {
                    domUActions = incrementStatMapValue(domUActions,name);
                }

                //S-Fields
                if (name.startsWith("S-")) {
                    domSFields = incrementStatMapValue(domSFields,name);
                }

                String value = element.attr("value");
                //VU Values
                if (value.contains("<vu:")) {
                    domVuValues = incrementStatMapValue(domVuValues,value);
                }
            }

        }

    }

    public static void logDomStats() {
        reportLogger.info("-------------------------");
        reportLogger.info("DOM Stats");
        reportLogger.info("-------------------------");

        outputStatMap("VU Tags", domVuTags);
        outputStatMap("Input Types", domInputTypes);
        outputStatMap("Input Values with VU Tags",domVuValues);
        outputStatMap("U-Actions values", domUActions);
        outputStatMap("S-Field values", domSFields);

    }

    public static void logInputStats() {
        reportLogger.info("-------------------------");
        reportLogger.info("Input Stats");
        reportLogger.info("-------------------------");

        outputStatMap("Input Types", inputTypes);
        outputStatMap("input values with VU Tags",inputVuValues);
        outputStatMap("U-Action Values",inputUActions);
        outputStatMap("S-Field values",inputSFields);
    }

    public static void logLDAPStats() {
        reportLogger.info("-------------------------");
        reportLogger.info("LDAP Stats");
        reportLogger.info("-------------------------");

        outputStatMap("LDAP Groups",ldapMemberGroups);
        outputStatMap("LDAP Accounts",ldapMemberAccounts);
    }

    private static void outputStatMap(String statTitle, HashMap<String, Integer> statMap) {
        if (!statMap.isEmpty()) {
            reportLogger.info("\n");
            reportLogger.info("-- " + statTitle + " --");
            SortedSet<String> keys = new TreeSet<>(statMap.keySet());
            for(String key : keys) {
                reportLogger.info(key+","+statMap.get(key));
            }
//            reportLogger.info(String.format("-----"));
        }
    }

    private static HashMap<String,Integer> incrementStatMapValue(HashMap<String,Integer> statMap, String statKey) {
        if (statKey.contains(",")){
            statKey = "\""+statKey+"\"";
        }
        if (statMap.containsKey(statKey)) {
            statMap.put(statKey,statMap.get(statKey) + 1);
        } else {
            statMap.put(statKey,1);
        }

        return statMap;
    }

}
