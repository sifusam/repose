package org.openrepose.commons.utils.http.header;

import java.util.List;

/**
 *
 * @author zinic
 */
public interface HeaderChooser <T extends HeaderValue> {

   T choosePreferredHeaderValue(Iterable<T> headerValues);
   
   List<T> choosePreferredHeaderValues(Iterable<T> headerValues);
}
