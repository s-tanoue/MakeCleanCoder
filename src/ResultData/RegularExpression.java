package ResultData;

import java.util.regex.Pattern;

/**
 * Created by satopi on 2017/05/09.
 */
public class RegularExpression {

    private Pattern pattern ;
    private String kind; //DESIGNコメント，DEFECTコメントなどの種類．
    private boolean caseSensitive;

    public RegularExpression(String target,String kind,boolean caseSensitive) {
        this.pattern = caseSensitive? Pattern.compile(target,Pattern.CASE_INSENSITIVE) : Pattern.compile(target);
        this.kind= kind;
        this.caseSensitive = caseSensitive;
    }

    public RegularExpression(Pattern pattern,String kind,boolean caseSensitive) {
        this.pattern = pattern;
        this.kind = kind;
        this.caseSensitive = caseSensitive;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }


    public boolean getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        caseSensitive = caseSensitive;
    }



}
