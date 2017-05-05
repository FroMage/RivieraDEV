package util;

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;

import play.templates.BaseTemplate;
import play.templates.BaseTemplate.RawData;
import play.utils.HTML;

public class JavaExtensions extends play.templates.JavaExtensions {
	public static RawData urlPathEncode(String str){
		return new RawData(urlEncode(str).replace("+", "%20"));
	}
	
    public static RawData jsonEscape(String str) {
        // quick check
        char[] chars = str.toCharArray();
        boolean needsEncoding = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(Character.isHighSurrogate(c)){
                // skip the next one
                i++;
                continue;
            }
            // every char allowed except " / and control chars 
            // \" \\ \/ \b \f \n \r \t
            if(c == '"'
                    || c == '\\'
                    || c == '\b'
                    || c == '\f'
                    || c == '\n'
                    || c == '\r'
                    || c == '\t'
                    || c <= 0x1f){
                needsEncoding = true;
                break;
            }
        }
        if(!needsEncoding)
            return new RawData(str);
        // now use code points to be safe
        StringBuilder b = new StringBuilder(str.length());
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(Character.isHighSurrogate(c)){
                // skip the whole pair
                b.append(c);
                b.append(chars[++i]);
                continue;
            }
            if(c == '"')
                b.append("\\\"");
            else if(c == '\\')
                b.append("\\\\");
            else if(c == '\b')
                b.append("\\b");
            else if(c == '\f')
                b.append("\\f");
            else if(c == '\n')
                b.append("\\n");
            else if(c == '\r')
                b.append("\\r");
            else if(c == '\t')
                b.append("\\t");
            else if(c <= 0x1f)
                b.append("\\u").append(String.format("%04x", (int)c));
            else
                b.append(c);
        }
        return new RawData(b.toString());
    }

	public static Object md(String mdString) {
	    // in theory this is not required anymore now that txtmark has "panic mode"
	    // but I don't see any problems with it so let's leave it in
	    String escaped = HTML.htmlEscape(mdString);
	    
	    Configuration config = Configuration.builder()
	            .forceExtentedProfile()
	            .setEnablePanicMode(true)
	            .build();
	    String html = Processor.process(escaped, config);        
	    html = cleanupEscapingMess(html);

	    return new BaseTemplate.RawData(html);
	}
	
	private static String cleanupEscapingMess(String html) {
	    return html.replaceAll("&amp;(quot|lt|gt|amp|mdash);", "&$1;");
    }
}
