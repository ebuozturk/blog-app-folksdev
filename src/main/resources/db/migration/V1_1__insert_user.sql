INSERT INTO app_user (id,username,first_name,middle_name,last_name,email,birth_date,created_date,updated_date) VALUES (
'default-admin-uuid',
'ebuozturk',
'ebubekir',
'',
'ozturk',
'ebuozturk98@gmail.com',
'1998-01-08',
current_timestamp,
current_timestamp
) ON CONFLICT DO NOTHING;