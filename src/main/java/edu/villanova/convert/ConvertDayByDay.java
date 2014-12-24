package edu.villanova.convert;

import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class ConvertDayByDay {

    static final String FILE_DIRECTORY = "/Users/bcherian/Documents/VIllanova/daybyday/";
    static final String TARGET_DIRECTORY = "/Users/bcherian/cq/vltWorkspace/villanova/jcr_root/misc/bcherian/daybyday/";

    public static void main(String[] args) {
        for (int month = 1;month <=12; month++) {
            for(int day = 1; day <= 31; day++) {
                String monthDate = String.format("%02d",month) + String.format("%02d",day);
                File monthDateDir = new File(TARGET_DIRECTORY + monthDate);

                String filename = FILE_DIRECTORY + monthDate + ".html";
                System.out.println("Parsing " + filename);

                String fileContent = CQUtil.getHTMLContent(filename) ;
                if (fileContent != null){
                    monthDateDir.mkdir();
                    String contentFilePath = monthDateDir.getPath() + "/.content.xml";

                    String title;
                    String content;
                    String contentSource;
                    String prayer = "";
                    String prayerSource = "";
                    Document document = Jsoup.parseBodyFragment(fileContent);

                    Elements titleElements = document.select(".dayDate");
                    title = titleElements.get(0).html();

                    Elements contentElements = document.select(".dailyContent table tbody tr td");
                    content = contentElements.get(0).html();
                    contentSource = contentElements.get(1).html();
                    if (contentElements.size() > 2) {
                        prayer = contentElements.get(2).html();
                        prayerSource = contentElements.get(3).html();
                    }

                    try {
                        PrintWriter pw = new PrintWriter(contentFilePath,"UTF-8");
                        pw.write(
                                contentXML(CQUtil.formattedString(title.replace("<br>"," - ")),
                                        CQUtil.formattedString(content),
                                        CQUtil.formattedString(contentSource),
                                        CQUtil.formattedString(prayer),
                                        CQUtil.formattedString(prayerSource))
                        );
                        pw.close();
                    } catch (FileNotFoundException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                } else {
                    System.out.println("Null content for " + monthDate);
                }
            }
        }
    }

    static String contentXML(String title, String content, String contentSource, String prayer, String prayerSource) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jcr:root xmlns:sling=\"http://sling.apache.org/jcr/sling/1.0\" xmlns:cq=\"http://www.day.com/jcr/cq/1.0\" xmlns:jcr=\"http://www.jcp.org/jcr/1.0\" xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\"\n" +
                "    jcr:primaryType=\"cq:Page\">\n" +
                "    <jcr:content\n" +
                "        cq:template=\"/apps/villanova/templates/contentpage\"\n" +
                "        hideInNav=\"true\"\n" +
                "        jcr:primaryType=\"cq:PageContent\"\n" +
                "        jcr:title=\"" + title + "\"\n" +
                "        sling:resourceType=\"villanova/components/page/contentpage\">\n" +
                "        <pagecontent\n" +
                "            jcr:primaryType=\"nt:unstructured\"\n" +
                "            sling:resourceType=\"foundation/components/parsys\">\n" +
                "            <textimage\n" +
                "                cq:cssClass=\"noalign nostyle\"\n" +
                "                jcr:primaryType=\"nt:unstructured\"\n" +
                "                sling:resourceType=\"foundation/components/textimage\"\n" +
                "                text=\"&lt;blockquote class='pull-right'>&lt;p>" + content + "&lt;/p>&lt;small>" + contentSource + "&lt;/small>&lt;/blockquote>" +
                "                   &lt;p>&lt;/p>&lt;blockquote class='pull-right'>&lt;p>" + prayer + "&lt;/p>&lt;small>" + prayerSource + "&lt;/small>&lt;/blockquote>\"\n" +
                "                textIsRich=\"true\">\n" +
                "                <image\n" +
                "                    jcr:primaryType=\"nt:unstructured\"\n" +
                "                    sling:resourceType=\"foundation/components/image\"\n" +
                "                    imageRotate=\"0\"/>\n" +
                "            </textimage>\n" +
                "        </pagecontent>\n" +
                "    </jcr:content>\n" +
                "</jcr:root>\n";
    }

}
