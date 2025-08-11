package com.greenland.collabalarm.model

enum class Role {
    OWNER,      // full control
    EDITOR,     // can edit directly
    PROPOSER,   // can propose changes; owner/editor must approve
    VIEWER      // read-only
}
