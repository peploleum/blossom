package blossom.restful.service.business.document.dto;

import java.util.List;
import java.util.Map;

/**
 * A document bean
 * 
 * @author peploleum
 *
 */
public class BlossomDocument {
    private String title;
    private String content;
    private Map<String, String> metadataProperties;
    private List<TaggedEntity> tags;

    public List<TaggedEntity> getTags() {
        return tags;
    }

    public void setTags(List<TaggedEntity> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getMetadataProperties() {
        return metadataProperties;
    }

    public void setMetadataProperties(Map<String, String> metadataProperties) {
        this.metadataProperties = metadataProperties;
    }

}
