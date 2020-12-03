package com.example.gameexplorer.util;

public class StringUtil {

    public StringUtil() {

    }

    public static String removeHtml(String html){

        if(html.contains("<p>")) {
            html = html.replaceAll("<p>", "");
        }
        if(html.contains("</p>")) {
            html = html.replaceAll("</p>", "");
        }
        if(html.contains("<h3>")) {
            html = html.replaceFirst("<h3>", "");
        }
        if(html.contains("</h3>")) {
            html = html.replaceAll("</h3>", "");
        }
        if(html.contains("<br />")) {
            html = html.replaceAll("<br />", "");
        }
        if(html.contains("&#39;")){
            html = html.replaceAll("&#39;", "'");
        }

        return html;
    }
}
