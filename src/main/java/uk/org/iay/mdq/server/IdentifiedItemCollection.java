/*
 * Copyright (C) 2014 Ian A. Young.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.metadata.Item;

/**
 * Class representing a {@link Collection} of {@link Item}s
 * associated with a {@link Collection} of identifiers.
 *
 * @param <T> type of {@link Item} in the collection
 */
class IdentifiedItemCollection<T> {
    
    /** The {@link Collection} of {@link Item}s. */
    @Nonnull
    private final Collection<Item<T>> items;
    
    /** The identifiers associated with the item collection. */
    @Nonnull
    private final Collection<String> identifiers;
    
    /**
     * Constructor.
     * 
     * Shorthand version with a single {@link Item}.
     *
     * @param item single item to be made into a collection
     * @param keys identifiers to be associated with the collection
     */
    protected IdentifiedItemCollection(@Nonnull final Item<T> item,
            @Nonnull final Collection<String> keys) {
        this(Collections.singletonList(item), keys);
    }
    
    /**
     * Constructor.
     * 
     * Shorthand version with a single identifier.
     *
     * @param collection items to be associated with the identifier
     * @param key identifier for the item collection.
     */
    protected IdentifiedItemCollection(@Nonnull final Collection<Item<T>> collection,
            @Nullable final String key) {
        this(collection, Collections.singletonList(key));
    }

    /**
     * Constructor.
     *
     * @param collection {@link Collection} of {@link Item}s to be associated with the identifiers
     * @param keys identifiers to be associated with the item collection
     */
    protected IdentifiedItemCollection(@Nonnull final Collection<Item<T>> collection,
            @Nonnull final Collection<String> keys) {
        items = collection;
        identifiers = new ArrayList<>(keys);
    }

    /**
     * Returns the items.
     * 
     * @return {@link Collection} of {@link Item}s.
     */
    @Nonnull
    public Collection<Item<T>> getItems() {
        return items;
    }

    /**
     * Returns the identifiers.
     * 
     * @return {@link Collection} of identifiers.
     */
    @Nonnull
    public Collection<String> getIdentifiers() {
        return identifiers;
    }

}