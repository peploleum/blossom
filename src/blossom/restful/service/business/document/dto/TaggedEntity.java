package blossom.restful.service.business.document.dto;

public class TaggedEntity {
    private String id;
    private String text;
    private String category;
    private int startOffset;
    private int endOffset;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startIndex) {
        this.startOffset = startIndex;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int stopIndex) {
        this.endOffset = stopIndex;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
