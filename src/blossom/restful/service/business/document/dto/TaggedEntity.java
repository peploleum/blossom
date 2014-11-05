package blossom.restful.service.business.document.dto;

public class TaggedEntity {
    private String id;
    private String text;
    private String category;
    private int startOffset;
    private int endOffset;

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(final String value) {
        this.text = value;
    }

    public int getStartOffset() {
        return this.startOffset;
    }

    public void setStartOffset(final int startIndex) {
        this.startOffset = startIndex;
    }

    public int getEndOffset() {
        return this.endOffset;
    }

    public void setEndOffset(final int stopIndex) {
        this.endOffset = stopIndex;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    private String getColor() {
        switch (category) {
        case "Character":
            return "red";
        case "Event":
            return "blue";
        case "Equipment":
            return "green";
        case "Organisation":
            return "orange";
        case "Place":
            return "yellow";
        default:
            return "brown";
        }
    }

    public String toHtmlString(final int offset) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<span ");
        sb.append("id=\"");
        sb.append(getId());
        sb.append("\"");
        sb.append(" offset=");
        sb.append(offset);
        sb.append(" style=\"color:");
        sb.append(getColor());
        sb.append("\"");
        sb.append(">");
        sb.append(getText());
        sb.append("</span>");
        return sb.toString();
    }

}
