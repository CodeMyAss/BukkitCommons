/*
 * Copyright 2014 AgentTroll
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

package com.gmail.woodyc40.commons.collect;

import java.util.Map;
import java.util.Set;

/**
 * Used for building new {@link com.gmail.woodyc40.commons.collect.HashStructMap}s and {@link
 * com.gmail.woodyc40.commons.collect.HashStructSet}s using properties specified
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public class StructBuilder {
    /** The hash strategy */
    private AbstractHashStruct.HashStrategy hash    = AbstractHashStruct.HashStrategy.A_TROLL;
    /** The initial size */
    private int                             size    = 16;
    /** The resizing threshold */
    private int                             resize  = 14;
    /** Weak reference key collection configuration */
    private boolean                         weakKey = false;
    /** Weak reference value collection configuration */
    private boolean                         weakVal = false;
    /** Whether or not to enable concurrency */
    private boolean concurrent; // TODO

    /**
     * Sets the hash strategy
     *
     * @param strategy the strategy to use
     * @return the instance of the builder it was added to
     */
    public StructBuilder hash(AbstractHashStruct.HashStrategy strategy) {
        this.hash = strategy;
        return this;
    }

    /**
     * Sets the initial size
     *
     * @param size the size to set to
     * @return the instance of the builder it was added to
     */
    public StructBuilder size(int size) {
        this.size = size;
        return this;
    }

    /**
     * Sets the resize threshold
     *
     * @param resize the size to resize the map at, doubled each time it is resized
     * @return the instance of the builder it was added to
     */
    public StructBuilder resize(int resize) {
        this.resize = resize;
        return this;
    }

    public StructBuilder weakKeys() {
        this.weakKey = true;
        return this;
    }

    public StructBuilder weakValues() {
        this.weakVal = true;
        return this;
    }

    /**
     * Whether or not to use concurrent collections
     *
     * @param concurrent {@code true} to enable concurrency support
     * @return the instance of the builder it was added to
     */
    public StructBuilder concurrent(boolean concurrent) {
        this.concurrent = concurrent;
        return this;
    }

    /**
     * Builds the resulting map
     *
     * @param <K> the key to use
     * @param <V> the value to use
     * @return the map built with the specified properties
     */
    public <K, V> Map<K, V> buildMap() {
        AbstractHashStruct<K, V> hashStruct = new AbstractHashStruct() {
            {
                this.setResizeThresh(StructBuilder.this.resize);
            }

            @Override protected AbstractHashStruct.Node[] buckets() {
                return new AbstractHashStruct.Node[StructBuilder.this.size];
            }

            @Override protected AbstractHashStruct.HashStrategy hashStrategy() {
                return StructBuilder.this.hash;
            }
        };

        if (this.weakKey && this.weakVal)
            return new WeakStructMap<>(WeakStructMap.WeakConfiguration.BOTH);
        if (this.weakKey)
            return new WeakStructMap<>(WeakStructMap.WeakConfiguration.KEYS);
        if (this.weakVal)
            return new WeakStructMap<>(WeakStructMap.WeakConfiguration.VALUES);
        return new HashStructMap<>(hashStruct);
    }

    /**
     * Builds the resulting set
     *
     * @param <E> the element type
     * @return the set built with the specified properties
     */
    public <E> Set<E> buildSet() {
        AbstractHashStruct<E, Object> hashStruct = new AbstractHashStruct() {
            {
                this.setResizeThresh(StructBuilder.this.resize);
            }

            @Override protected AbstractHashStruct.Node[] buckets() {
                return new AbstractHashStruct.Node[StructBuilder.this.size];
            }

            @Override protected AbstractHashStruct.HashStrategy hashStrategy() {
                return StructBuilder.this.hash;
            }
        };

        // TODO weak implementations
        return new HashStructSet<>(hashStruct);
    }
}
