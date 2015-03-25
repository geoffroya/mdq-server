/*
 * Copyright (C) 2015 Ian A. Young.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.org.iay.mdq.server;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.xml.namespace.QName;

import net.shibboleth.metadata.Item;
import net.shibboleth.metadata.ItemCollectionSerializer;
import net.shibboleth.metadata.dom.saml.SAMLMetadataSupport;
import net.shibboleth.utilities.java.support.xml.ElementSupport;

import org.w3c.dom.Element;

/**
 * An {@link ItemCollectionSerializer} that serializes a collection of SAML entities
 * into a JSON representation.
 */
class JSONEntityListCollectionSerializer implements ItemCollectionSerializer<Element> {

    /** QName of the IDPSSODescriptor element. */
    private static final QName IDP_SSO_DESCRIPTOR_NAME = new QName(SAMLMetadataSupport.MD_NS, "IDPSSODescriptor");

    /** QName of the SPSSODescriptor element. */
    private static final QName SP_SSO_DESCRIPTOR_NAME = new QName(SAMLMetadataSupport.MD_NS, "SPSSODescriptor");

    /** Configured JSON generator factory. */
    private final JsonGeneratorFactory factory;

    /**
     * Constructor.
     * 
     * @param prettyPrinting whether to generate pretty-printed JSON
     */
    public JSONEntityListCollectionSerializer(final boolean prettyPrinting) {
        final Map<String, String> generatorConfig = new HashMap<>();
        if (prettyPrinting) {
            generatorConfig.put(JsonGenerator.PRETTY_PRINTING, "true");
        }
        factory = Json.createGeneratorFactory(generatorConfig);
    }

    /**
     * Write a JSON object corresponding to a SAML role descriptor.
     * 
     * @param gen JSON generator to write to
     * @param role SAML role descriptor to process
     */
    private void writeRole(@Nonnull final JsonGenerator gen, @Nullable final Element role) {
        if (role != null) {
            gen.writeStartObject();
                gen.write("type", role.getLocalName());
            gen.writeEnd();
        }
    }

    /**
     * Write a JSON object corresponding to the {@link Item} to the {@link JsonGenerator}.
     * 
     * @param gen JSON generator to write to
     * @param entity the SAML entity descriptor
     */
    private void writeEntity(final JsonGenerator gen, final Element entity) {
        if (SAMLMetadataSupport.isEntityDescriptor(entity)) {
            gen.writeStartObject();
            gen.write("entityID", entity.getAttribute("entityID"));
            final Element idp = ElementSupport.getFirstChildElement(entity, IDP_SSO_DESCRIPTOR_NAME);
            final Element sp = ElementSupport.getFirstChildElement(entity, SP_SSO_DESCRIPTOR_NAME);
            if (idp != null || sp != null) {
                gen.writeStartArray("roles");
                    writeRole(gen, idp);
                    writeRole(gen, sp);
                gen.writeEnd();
            }
            gen.writeEnd();
        }
    }

    @Override
    public void serializeCollection(@Nonnull final Collection<Item<Element>> items,
            @Nonnull final OutputStream output) {
        final JsonGenerator gen = factory.createGenerator(output);
        gen.writeStartArray();
            for (final Item<Element> item : items) {
                writeEntity(gen, item.unwrap());
            }
        gen.writeEnd();
        gen.close();
    }
    
}
