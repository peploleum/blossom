package blossom.restful.service.business.document;

import java.util.logging.Logger;

import blossom.restful.service.business.document.dto.BlossomDocument;
import blossom.restful.service.business.document.dto.TaggedEntity;
import blossom.restful.service.business.document.model.DocumentsSingleton;

public class BlossomDocumentDao {

    private final static Logger LOGGER = Logger.getLogger(BlossomDocumentDao.class.getName());

    public BlossomDocumentDao() {

    }

    public BlossomDocument getBlossomDocumentById(final String id) {
        LOGGER.info("getting document " + id);
        final DocumentsSingleton documentModel = DocumentsSingleton.getINSTANCE();
        final BlossomDocument first = documentModel.getDocuments().iterator().next();
        return first;
    }

    public void createBlossomDocument(final BlossomDocument bd) {

    }

    public BlossomDocument addTag(final TaggedEntity taggedEntity) {
        LOGGER.info("adding tagged entity ");
        final DocumentsSingleton documentModel = DocumentsSingleton.getINSTANCE();
        final BlossomDocument first = documentModel.getDocuments().iterator().next();
        first.getTags().add(taggedEntity);
        computeSpansFromTagging(first);
        return first;
    }

    private void computeSpansFromTagging(final BlossomDocument doc) {
        LOGGER.info("Performing decoration for " + doc.getId());

        final String content = doc.getContent(); // content as html
        final char[] charArray = new char[content.length()];
        content.getChars(0, content.length(), charArray, 0);
        final StringBuilder decoratedContentBuilder = new StringBuilder();
        int offsetCount = 0;
        for (int i = 0; i < charArray.length; i++) {

            final TaggedEntity taggedEntityAtIndex = getTaggedEntityAtIndex(i, doc);
            if (taggedEntityAtIndex != null) {
                offsetCount = i;
                final String generateSpan = generateSpan(offsetCount, taggedEntityAtIndex);
                decoratedContentBuilder.append(generateSpan);
                // keeping track of where the last span is to perform selection easily on client
                // side (without having to manipulate dom)
                i += ((taggedEntityAtIndex.getEndOffset() - taggedEntityAtIndex.getStartOffset()) - 1);
            } else {
                decoratedContentBuilder.append(charArray[i]);
            }
        }
        doc.setDecoratedContent(decoratedContentBuilder.toString());
    }

    private TaggedEntity getTaggedEntityAtIndex(final int index, final BlossomDocument doc) {
        for (final TaggedEntity tag : doc.getTags()) {
            if (tag.getStartOffset() == index) {
                return tag;
            }
        }
        return null;
    }

    private String generateSpan(final int offset, final TaggedEntity tag) {
        LOGGER.info("generating span for " + tag.getText() + " id: " + tag.getId());
        final StringBuilder sb = new StringBuilder();
        sb.append("<span ");
        sb.append("id=\"");
        sb.append(tag.getId());
        sb.append("\"");
        sb.append(" offset=");
        sb.append(offset);
        sb.append(" style=\"color:blue\"");
        sb.append(">");
        sb.append(tag.getText());
        sb.append("</span>");
        LOGGER.info("span: " + sb.toString());
        return sb.toString();
    }
}
