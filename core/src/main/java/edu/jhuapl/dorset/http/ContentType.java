/*
 * Copyright 2016 The Johns Hopkins University Applied Physics Laboratory LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.jhuapl.dorset.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ContentType {
    public static final ContentType TEXT_PLAIN =
                    new ContentType("text/plain", StandardCharsets.ISO_8859_1);
    public static final ContentType APPLICATION_JSON =
                    new ContentType("application/json", StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_FORM_URLENCODED =
                    new ContentType("application/x-www-form-urlencoded", StandardCharsets.ISO_8859_1);

    private final String mimeType;
    private final Charset charset;
    
    public ContentType(String mimeType, Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Charset getCharset() {
        return charset;
    }
}
