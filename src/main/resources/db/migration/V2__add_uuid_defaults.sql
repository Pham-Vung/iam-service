ALTER TABLE users
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE roles
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE permissions
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE user_roles
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE role_permissions
    ALTER COLUMN id SET DEFAULT gen_random_uuid();
