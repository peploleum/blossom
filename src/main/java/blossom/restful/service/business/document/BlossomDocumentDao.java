package blossom.restful.service.business.document;

import java.util.logging.Logger;

import blossom.exception.TopLevelBlossomException;
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

    public BlossomDocument addTag(final TaggedEntity taggedEntity) throws TopLevelBlossomException {
        LOGGER.info("adding tagged entity ");
        if (taggedEntity.getText() == null || taggedEntity.getText().isEmpty())
            throw new TopLevelBlossomException("tag must not be empty");
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
        boolean isOpen = false;
        for (int i = 0; i < charArray.length; i++) {

            final TaggedEntity taggedEntityAtIndex = getTaggedEntityAtIndex(i, doc);
            if (taggedEntityAtIndex != null) {
                if (isOpen) {
                    isOpen = false;
                    decoratedContentBuilder.append(closeSpan());
                }
                final String generateSpan = taggedEntityAtIndex.toHtmlString(i);
                decoratedContentBuilder.append(generateSpan);
                // keeping track of where the last span is to perform selection easily on client
                // side (without having to manipulate dom)
                i += ((taggedEntityAtIndex.getEndOffset() - taggedEntityAtIndex.getStartOffset()) - 1);
                decoratedContentBuilder.append(openSpan(i + 1));
                isOpen = true;
            } else {
                decoratedContentBuilder.append(charArray[i]);
            }
        }
        doc.setDecoratedContent(decoratedContentBuilder.toString());
    }

    private String openSpan(final int offset) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<span offset=");
        sb.append(offset);
        sb.append(">");
        return sb.toString();
    }

    private String closeSpan() {
        final StringBuilder sb = new StringBuilder();
        sb.append("</span>");
        return sb.toString();
    }

    private TaggedEntity getTaggedEntityAtIndex(final int index, final BlossomDocument doc) {
        for (final TaggedEntity tag : doc.getTags()) {
            if (tag.getStartOffset() == index) {
                return tag;
            }
        }
        return null;
    }
}
