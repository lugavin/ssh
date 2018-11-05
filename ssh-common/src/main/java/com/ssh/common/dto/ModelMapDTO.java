package com.ssh.common.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelMapDTO extends LinkedHashMap<String, Object> implements DTO {

    private static final long serialVersionUID = 20160523L;

    /**
     * Construct a new, empty {@code ModelMapDTO}.
     */
    public ModelMapDTO() {
    }

    /**
     * Construct a new {@code ModelMapDTO} containing the supplied attribute
     * under the supplied name.
     *
     * @see #addAttribute(String, Object)
     */
    public ModelMapDTO(String attributeName, Object attributeValue) {
        addAttribute(attributeName, attributeValue);
    }

    public ModelMapDTO(Map<String, ?> attributes) {
        addAllAttributes(attributes);
    }


    /**
     * Add the supplied attribute under the supplied name.
     *
     * @param attributeName  the name of the model attribute (never {@code null})
     * @param attributeValue the model attribute value (can be {@code null})
     */
    public ModelMapDTO addAttribute(String attributeName, Object attributeValue) {
        if (attributeName == null) {
            throw new IllegalArgumentException("Model attribute name must not be null");
        }
        put(attributeName, attributeValue);
        return this;
    }

    /**
     * Copy all attributes in the supplied {@code Map} into this {@code Map}.
     *
     * @see #addAttribute(String, Object)
     */
    public ModelMapDTO addAllAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            putAll(attributes);
        }
        return this;
    }

    /**
     * Copy all attributes in the supplied {@code Map} into this {@code Map},
     * with existing objects of the same name taking precedence (i.e. not getting
     * replaced).
     */
    public ModelMapDTO mergeAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            for (Map.Entry<String, ?> entry : attributes.entrySet()) {
                String key = entry.getKey();
                if (!containsKey(key)) {
                    put(key, entry.getValue());
                }
            }
        }
        return this;
    }

    /**
     * Does this model contain an attribute of the given name?
     *
     * @param attributeName the name of the model attribute (never {@code null})
     * @return whether this model contains a corresponding attribute
     */
    public boolean containsAttribute(String attributeName) {
        return containsKey(attributeName);
    }

}
