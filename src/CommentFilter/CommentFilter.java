package CommentFilter;

import com.sun.javafx.image.impl.ByteIndexed;

/**
 * Created by satopi on 2017/05/09.
 */
public class CommentFilter {

    private String textPassedThroughFilter;

    public CommentFilter(String originalText){
        this.textPassedThroughFilter = new String(originalText.toLowerCase());
    };

    public String getTextPassedThroughFilter(){
        return this.textPassedThroughFilter;
    }

}
