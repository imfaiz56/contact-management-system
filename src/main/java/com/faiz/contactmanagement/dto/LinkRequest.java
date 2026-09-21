package com.faiz.contactmanagement.dto;

import jakarta.validation.constraints.NotNull;

public class LinkRequest {

    @NotNull(message = "relatedToId is required")
    private Long relatedToId;

    private String relationshipLabel; // e.g. "Manager", "Colleague", "Friend"

    public Long getRelatedToId() {
        return relatedToId;
    }

    public void setRelatedToId(Long relatedToId) {
        this.relatedToId = relatedToId;
    }

    public String getRelationshipLabel() {
        return relationshipLabel;
    }

    public void setRelationshipLabel(String relationshipLabel) {
        this.relationshipLabel = relationshipLabel;
    }
}
