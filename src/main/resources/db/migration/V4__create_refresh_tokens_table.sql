CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    family_id UUID NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ,
    revoked_reason VARCHAR(50),
    replaced_by_token_id UUID,
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT ck_refresh_tokens_revoked_reason
        CHECK (revoked_reason IS NULL OR revoked_reason IN (
            'ROTATED',
            'LOGOUT',
            'REUSE_DETECTED',
            'ADMIN_REVOKED'
        )),
    CONSTRAINT ck_refresh_tokens_revocation
        CHECK (
            (revoked_at IS NULL AND revoked_reason IS NULL)
            OR
            (revoked_at IS NOT NULL AND revoked_reason IS NOT NULL)
        )
);

CREATE UNIQUE INDEX uk_refresh_tokens_token_hash
    ON refresh_tokens (token_hash);

CREATE INDEX idx_refresh_tokens_user_active
    ON refresh_tokens (user_id)
    WHERE revoked_at IS NULL;

CREATE INDEX idx_refresh_tokens_family_active
    ON refresh_tokens (family_id)
    WHERE revoked_at IS NULL;

CREATE INDEX idx_refresh_tokens_expires_at
    ON refresh_tokens (expires_at);
