
package com.tamil.tts.search.node;


import java.util.Collection;
import java.util.Map;

import com.tamil.tts.search.base.AbstractTrieNode;
import com.tamil.tts.search.base.TrieNode;


abstract class AbstractMapNode<V> extends AbstractTrieNode<V> {

    private final Map<String, TrieNode<V>> children;

    AbstractMapNode(String character) {
        super(character);
        children = createMap();
    }

    private Map<String, TrieNode<V>> createMap() {
        return onCreateMap();
    }

    private AbstractMapNode<V> createNode(String character) {
        return onCreateNewNode(character);
    }

    protected abstract Map<String, TrieNode<V>> onCreateMap();

    protected abstract AbstractMapNode<V> onCreateNewNode(String character);

    @Override
    public TrieNode<V> addChild(final String character) {
        final AbstractMapNode<V> leafNode = createNode(character);
        children.put(character, leafNode);
        return leafNode;
    }

    @Override
    public TrieNode<V> getChild(final String character) {
        if (children.containsKey(character)) {
            return children.get(character);
        } else {
            return null;
        }
    }

    @Override
    public void removeChild(final String character) {
        TrieNode<V> removedNode = children.remove(character);
        if (removedNode != null) {
            removedNode.clear();
            removedNode = null;
        }
    }

    @Override
    public boolean containsChild(final String c) {
        return children.containsKey(c);
    }

    @Override
    public Collection<TrieNode<V>> getChildren() {
        return children.values();
    }

    @Override
    public boolean isEnd() {
        return children.values().isEmpty();
    }

    @Override
    public void clear() {
        super.clear();
        children.clear();
    }
}
