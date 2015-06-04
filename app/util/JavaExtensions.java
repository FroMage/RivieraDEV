package util;

import play.templates.BaseTemplate.RawData;

public class JavaExtensions extends play.templates.JavaExtensions {
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

}
