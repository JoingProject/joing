-- ********************************************************************************************************************
--     WebPC - SERVER - DATA
-- ********************************************************************************************************************

insert into locales (IDIOM, COUNTRY)  -- ID_LOCALE = 2
     values ('es', 'ES');

-- ------------------------------------------------------------------

insert into users (ACCOUNT, PASSWORD, EMAIL, FIRST_NAME, SECOND_NAME, ID_LOCALE, IS_MALE, QUOTA)
     values('peyrona@joing.org','admin','peyrona@gmail.com', 'Francisco','Morero Peyrona', 2, 1, 0);

-- For inf about ID_APP_GROUP, refer to -> org.joing.common.dto.app.AppGroup.java

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (1, 2, 'Accesorios');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (2, 2, 'Educación');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (3, 2, 'Juegos');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (4, 2, 'Graficos');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (5, 2, 'Internet');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (6, 2, 'Multimedia');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (7, 2, 'Oficina');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (8, 2, 'Programación');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (9, 2, 'Sistema');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (10, 2, 'Otros');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (99, 2, 'Escritorios');

-- -----------------------------------------------------------------------------

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('desktops', 'PDE.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('accessories', 'Notes.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('accessories', 'BasicCalculator.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('graphics', 'Images.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('games', 'Tetris.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('games', 'Pacman.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('system', 'ProxyConfig.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('system', 'SystemMonitor.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('accessories', 'TinyExplorer.jar');

insert into applications (EXTRA_PATH, EXECUTABLE)
     values ('accessories', 'YACE.jar');

-- ------------------------------------------------------------------

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (1, 99);  -- App 'PDE' belongs to Group 'Desktops'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (2, 1);   -- App 'Notes' belongs to Group 'Accessories'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (3, 1);   -- App 'Calculator' belongs to Group 'Accessories'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (4, 4);   -- App 'Images' belongs to Group 'Graphics'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (5, 3);   -- App 'Tetris' belongs to Group 'Games'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (6, 3);   -- App 'pacman' belongs to Group 'Games'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (7, 9);   -- App 'ProxyConfig' belongs to Group 'System'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (8, 9);   -- App 'SystemMonitor' belongs to Group 'System'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (9, 1);   -- App 'File Explorer' belongs to Group 'Accesories'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (10, 1);   -- App 'Community Explorer' belongs to Group 'Accesories'

-- ------------------------------------------------------------------

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 1, 1);
 
insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 2, 1);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 3, 1);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 4, 1);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 5, 1);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 6, 1);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 7, 0);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 8, 0);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 9, 1);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, ALLOW_REMOTE)
     values ('peyrona@joing.org', 10, 1);

-- ------------------------------------------------------------------

insert into files ( ID_ORIGINAL, ACCOUNT, OWNER, FILE_NAME, FILE_PATH, LOCKED_BY, 
                    IS_DIR, IS_HIDDEN, IS_PUBLIC, IS_READABLE, IS_MODIFIABLE, IS_DELETEABLE, 
                    IS_EXECUTABLE, IS_DUPLICABLE, IS_ALTERABLE, IS_IN_TRASHCAN, NOTES )
           values ( NULL, 'peyrona@joing.org', 'system@joing.org', '/', '', NULL, 
                    1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 
                   'Root & Home Directory');

insert into files ( ID_ORIGINAL, ACCOUNT, OWNER, FILE_NAME, FILE_PATH, LOCKED_BY, 
                    IS_DIR, IS_HIDDEN, IS_PUBLIC, IS_READABLE, IS_MODIFIABLE, IS_DELETEABLE, 
                    IS_EXECUTABLE, IS_DUPLICABLE, IS_ALTERABLE, IS_IN_TRASHCAN, NOTES )
           values ( NULL, 'peyrona@joing.org', 'system@joing.org', 'Desktop', '/', NULL,
                    1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 
                   'The desktop');

insert into files ( ID_ORIGINAL, ACCOUNT, OWNER, FILE_NAME, FILE_PATH, LOCKED_BY, 
                    IS_DIR, IS_HIDDEN, IS_PUBLIC, IS_READABLE, IS_MODIFIABLE, IS_DELETEABLE, 
                    IS_EXECUTABLE, IS_DUPLICABLE, IS_ALTERABLE, IS_IN_TRASHCAN, NOTES )
           values ( NULL, 'peyrona@joing.org', 'peyrona@joing.org', 'The very 1st file', '/', NULL, 
                    0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 
                   'The very first file (not dir). Originaly stored in root dir');

-- *****************************************   EOF  *******************************************************************