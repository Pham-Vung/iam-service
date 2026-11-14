-- Default system roles. PostgreSQL generates the UUID identifiers.
INSERT INTO roles (
    code,
    name,
    description,
    type,
    status,
    created_by
) VALUES
    (
        'SUPER_ADMIN',
        'Super Administrator',
        'System role with full IAM administration permissions',
        'SYSTEM',
        'ACTIVE',
        'flyway'
    ),
    (
        'USER',
        'User',
        'Default role for regular users',
        'SYSTEM',
        'ACTIVE',
        'flyway'
    );


-- Default IAM permissions. PostgreSQL generates the UUID identifiers.
INSERT INTO permissions (
    code,
    name,
    description,
    module,
    action,
    status,
    created_by
) VALUES
    ('USER_VIEW', 'View users', 'View user information and user lists', 'USER', 'VIEW', 'ACTIVE', 'flyway'),
    ('USER_CREATE', 'Create users', 'Create new user accounts', 'USER', 'CREATE', 'ACTIVE', 'flyway'),
    ('USER_UPDATE', 'Update users', 'Update user information and status', 'USER', 'UPDATE', 'ACTIVE', 'flyway'),
    ('USER_DELETE', 'Delete users', 'Soft-delete user accounts', 'USER', 'DELETE', 'ACTIVE', 'flyway'),
    ('ROLE_VIEW', 'View roles', 'View role information and role lists', 'ROLE', 'VIEW', 'ACTIVE', 'flyway'),
    ('ROLE_CREATE', 'Create roles', 'Create custom roles', 'ROLE', 'CREATE', 'ACTIVE', 'flyway'),
    ('ROLE_UPDATE', 'Update roles', 'Update custom role information and status', 'ROLE', 'UPDATE', 'ACTIVE', 'flyway'),
    ('ROLE_DELETE', 'Delete roles', 'Soft-delete custom roles', 'ROLE', 'DELETE', 'ACTIVE', 'flyway'),
    ('ROLE_ASSIGN', 'Assign roles', 'Assign roles to users', 'ROLE', 'ASSIGN', 'ACTIVE', 'flyway'),
    ('PERMISSION_VIEW', 'View permissions', 'View permission information and permission lists', 'PERMISSION', 'VIEW', 'ACTIVE', 'flyway'),
    ('PERMISSION_ASSIGN', 'Assign permissions', 'Assign permissions to roles', 'PERMISSION', 'ASSIGN', 'ACTIVE', 'flyway');


-- SUPER_ADMIN receives every permission seeded by this migration.
-- The USER role intentionally has no administration permissions.
INSERT INTO role_permissions (
    role_id,
    permission_id,
    created_by
)
SELECT
    r.id,
    p.id,
    'flyway'
FROM roles r
CROSS JOIN permissions p
WHERE LOWER(r.code) = LOWER('SUPER_ADMIN')
  AND r.status = 'ACTIVE'
  AND r.is_deleted = FALSE
  AND p.code IN (
      'USER_VIEW',
      'USER_CREATE',
      'USER_UPDATE',
      'USER_DELETE',
      'ROLE_VIEW',
      'ROLE_CREATE',
      'ROLE_UPDATE',
      'ROLE_DELETE',
      'ROLE_ASSIGN',
      'PERMISSION_VIEW',
      'PERMISSION_ASSIGN'
  )
  AND p.status = 'ACTIVE'
  AND p.is_deleted = FALSE;
