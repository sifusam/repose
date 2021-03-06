package org.openrepose.filters.translation.httpx.node;

import org.openrepose.core.httpx.AcceptHeader;
import org.openrepose.core.httpx.RequestHeaders;
import org.openrepose.core.httpx.SimpleParameter;
import org.openrepose.commons.utils.StringUtilities;
import org.openrepose.commons.utils.http.media.MediaRangeParser;
import org.openrepose.commons.utils.http.media.MediaType;
import org.openrepose.filters.translation.httpx.ObjectFactoryUser;

import java.util.List;
import java.util.Map;

/**
 * @author fran
 */
public class AcceptHeaderNode extends ObjectFactoryUser implements Node {
    private final String requestAcceptHeader;
    private final RequestHeaders headers;

    public AcceptHeaderNode(String requestAcceptHeader, RequestHeaders headers) {
        this.requestAcceptHeader = requestAcceptHeader;
        this.headers = headers;
    }

    @Override
    public void build() {
        AcceptHeader acceptHeader = getObjectFactory().createAcceptHeader();

        if (StringUtilities.isNotBlank(requestAcceptHeader)) {
            final List<MediaType> mediaRanges = new MediaRangeParser(requestAcceptHeader).parse();

            for (org.openrepose.commons.utils.http.media.MediaType range : mediaRanges) {
                org.openrepose.core.httpx.MediaRange mediaRange = getObjectFactory().createMediaRange();

                mediaRange.setType(range.getMimeType().getType());
                mediaRange.setSubtype(range.getMimeType().getSubType());

                for (Map.Entry<String, String> entry : range.getParameters().entrySet()) {
                    SimpleParameter simpleParameter = getObjectFactory().createSimpleParameter();

                    simpleParameter.setName(entry.getKey());
                    simpleParameter.setValue(entry.getValue());

                    mediaRange.getParameter().add(simpleParameter);
                }

                acceptHeader.getMediaRange().add(mediaRange);
            }
        }

        headers.setAccept(acceptHeader);
    }
}
