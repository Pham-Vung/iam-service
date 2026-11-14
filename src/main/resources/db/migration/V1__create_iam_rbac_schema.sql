CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    last_login_at TIMESTAMPTZ,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT ck_users_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED'))
);

CREATE UNIQUE INDEX uk_users_username_active
    ON users (LOWER(username))
    WHERE is_deleted = FALSE;

CREATE UNIQUE INDEX uk_users_email_active
    ON users (LOWER(email))
    WHERE is_deleted = FALSE AND email IS NOT NULL;

CREATE INDEX idx_users_status_active
    ON users (status)
    WHERE is_deleted = FALSE;


CREATE TABLE roles (
    id UUID PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(30) NOT NULL DEFAULT 'CUSTOM',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT ck_roles_type
        CHECK (type IN ('SYSTEM', 'CUSTOM')),
    CONSTRAINT ck_roles_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE UNIQUE INDEX uk_roles_code_active
    ON roles (LOWER(code))
    WHERE is_deleted = FALSE;

CREATE INDEX idx_roles_status_active
    ON roles (status)
    WHERE is_deleted = FALSE;


CREATE TABLE permissions (
    id UUID PRIMARY KEY,
    code VARCHAR(150) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    module VARCHAR(100) NOT NULL,
    action VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT ck_permissions_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE UNIQUE INDEX uk_permissions_code_active
    ON permissions (LOWER(code))
    WHERE is_deleted = FALSE;

CREATE UNIQUE INDEX uk_permissions_module_action_active
    ON permissions (LOWER(module), LOWER(action))
    WHERE is_deleted = FALSE;

CREATE INDEX idx_permissions_status_active
    ON permissions (status)
    WHERE is_deleted = FALSE;


CREATE TABLE user_roles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE UNIQUE INDEX uk_user_roles_active
    ON user_roles (user_id, role_id)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_user_roles_role_active
    ON user_roles (role_id)
    WHERE is_deleted = FALSE;


CREATE TABLE role_permissions (
    id UUID PRIMARY KEY,
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE UNIQUE INDEX uk_role_permissions_active
    ON role_permissions (role_id, permission_id)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_role_permissions_permission_active
    ON role_permissions (permission_id)
    WHERE is_deleted = FALSE;
