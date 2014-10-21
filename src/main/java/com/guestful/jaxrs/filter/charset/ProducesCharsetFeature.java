/**
 * Copyright (C) 2013 Guestful (info@guestful.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guestful.jaxrs.filter.charset;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class ProducesCharsetFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        Charset annot = resourceInfo.getResourceMethod().getAnnotation(Charset.class);
        if (annot == null) {
            annot = resourceInfo.getResourceClass().getAnnotation(Charset.class);
        }
        if (annot != null) {
            context.register(new ProducesCharsetFilter(annot.value()));
        }
    }

    @Priority(Priorities.HEADER_DECORATOR)
    public static class ProducesCharsetFilter implements ContainerResponseFilter {

        private final String charset;

        ProducesCharsetFilter(String charset) {
            this.charset = charset;
        }

        @Override
        public void filter(ContainerRequestContext request, ContainerResponseContext responseContext) throws IOException {
            Object ct = responseContext.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
            if(ct != null) {
                responseContext.getHeaders().putSingle(HttpHeaders.CONTENT_TYPE, ct + "; charset=" + charset);
            }
        }
    }

}
