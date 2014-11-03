package blossom.restful.service.business.document.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A document bean
 *
 * @author peploleum
 *
 */
public class BlossomDocument {
    private String id;
    private String title;
    private String content;
    private String decoratedContent;
    private Map<String, String> metadataProperties;
    private List<TaggedEntity> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TaggedEntity> getTags() {
        if (this.tags == null) {
            this.tags = new ArrayList<TaggedEntity>();
        }
        return this.tags;
    }

    public void setTags(final List<TaggedEntity> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Map<String, String> getMetadataProperties() {
        return this.metadataProperties;
    }

    public void setMetadataProperties(final Map<String, String> metadataProperties) {
        this.metadataProperties = metadataProperties;
    }

    public String getDecoratedContent() {
        return decoratedContent;
    }

    public void setDecoratedContent(String decoratedContent) {
        this.decoratedContent = decoratedContent;
    }

}
