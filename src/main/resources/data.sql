INSERT INTO users (first_name,last_name, password,email, role,dtype)
SELECT 'admin', 'admin', '$2a$10$3DdC4IK5mBdikYas./VbVehFID2w29MmbZYL56l5iC9u70EdTsg1y','admin@abc.com','admin',-1
WHERE NOT EXISTS (SELECT * FROM users WHERE email='admin@abc.com');